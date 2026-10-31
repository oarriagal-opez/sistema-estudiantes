package gt.edu.umg.sistema.estudiantes.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import gt.edu.umg.sistema.estudiantes.dao.ClienteDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.ProductoDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Servidor HTTP mínimo (usa solo el JDK, sin librerías extra) para que la app Android
 * trabaje a través de este programa. Reutiliza los mismos DAO que las pantallas Swing,
 * así que MySQL sigue siendo accedido únicamente por este programa.
 *
 * Uso: llamar ServidorApi.iniciar() una vez al arrancar el programa.
 */
public class ServidorApi {

    public static final int PUERTO = 8080;
    /** Debe ser IGUAL a API_KEY en Config.java de la app Android. Cámbiala. */
    private static final String CLAVE_API = "727f9135ecac5cc0";

    private static HttpServer servidor;
    private static final Object LOCK_GUARDAR = new Object();

    private static final FacturaDAO facturaDAO = new FacturaDAOImpl();
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ProductoDAO productoDAO = new ProductoDAO();

    private interface Accion { String ejecutar(HttpExchange ex) throws Exception; }

    private static class ApiException extends Exception {
        final int codigo;
        ApiException(int codigo, String mensaje) { super(mensaje); this.codigo = codigo; }
    }

    private ServidorApi() { }

    public static synchronized void iniciar() {
        if (servidor != null) return;
        try {
            servidor = HttpServer.create(new InetSocketAddress(PUERTO), 0);
            servidor.createContext("/api/ping", ex -> manejar(ex, "GET", e -> "{\"ok\":true}"));
            servidor.createContext("/api/productos", ex -> manejar(ex, "GET", ServidorApi::productos));
            servidor.createContext("/api/siguiente-factura", ex -> manejar(ex, "GET", ServidorApi::siguienteFactura));
            servidor.createContext("/api/cliente", ex -> manejar(ex, "GET", ServidorApi::cliente));
            servidor.createContext("/api/facturas", ex -> manejar(ex, "POST", ServidorApi::guardarFactura));
            servidor.setExecutor(Executors.newFixedThreadPool(4));
            servidor.start();
            System.out.println("[API] Servidor para la app móvil escuchando en el puerto " + PUERTO);
        } catch (IOException e) {
            servidor = null;
            System.err.println("[API] No se pudo iniciar el servidor (¿puerto " + PUERTO + " ocupado?): " + e.getMessage());
        }
    }

    public static synchronized void detener() {
        if (servidor != null) {
            servidor.stop(0);
            servidor = null;
        }
    }

    // ------------------------------------------------------------------ endpoints

    private static String productos(HttpExchange ex) {
        List<Producto> lista = productoDAO.listar();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"id\":").append(p.getIdProducto())
              .append(",\"nombre\":").append(q(p.getNombre()))
              .append(",\"precio\":").append(p.getPrecio())
              .append(",\"stock\":").append(p.getExistencia()).append('}');
        }
        return sb.append(']').toString();
    }

    private static String siguienteFactura(HttpExchange ex) {
        return "{\"id\":" + facturaDAO.obtenerSiguienteIdFactura() + "}";
    }

    private static String cliente(HttpExchange ex) throws Exception {
        String nit = param(ex, "nit");
        if (nit == null || nit.trim().isEmpty()) throw new ApiException(400, "Falta el parámetro nit");
        Cliente c = clienteDAO.buscarPorNit(nit.trim());
        if (c == null) throw new ApiException(404, "Cliente no encontrado");
        return "{\"id\":" + c.getIdCliente()
                + ",\"nit\":" + q(c.getNit())
                + ",\"nombre\":" + q(c.getNombre())
                + ",\"direccion\":" + q(c.getDireccion()) + "}";
    }

    /**
     * Cuerpo (texto plano UTF-8), una línea por dato:
     *   nit=1234567
     *   nombre=Juan Perez
     *   direccion=Zona 1
     *   entrega=TIENDA | RECOGER | ENVIO
     *   item=idProducto,cantidad,precioUnitario      (una línea por producto)
     */
    private static String guardarFactura(HttpExchange ex) throws Exception {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String nit = "", nombre = "", direccion = "", entrega = "TIENDA";
        List<double[]> items = new ArrayList<>();

        for (String linea : body.split("\\r?\\n")) {
            linea = linea.trim();
            int i = linea.indexOf('=');
            if (linea.isEmpty() || i < 0) continue;
            String k = linea.substring(0, i).trim();
            String v = linea.substring(i + 1).trim();
            switch (k) {
                case "nit": nit = v; break;
                case "nombre": nombre = v; break;
                case "direccion": direccion = v; break;
                case "entrega": entrega = v; break;
                case "item":
                    String[] p = v.split(",");
                    if (p.length != 3) throw new ApiException(400, "Detalle inválido: " + v);
                    try {
                        items.add(new double[]{Integer.parseInt(p[0].trim()),
                                Integer.parseInt(p[1].trim()), Double.parseDouble(p[2].trim())});
                    } catch (NumberFormatException e) {
                        throw new ApiException(400, "Detalle con valores no numéricos: " + v);
                    }
                    break;
                default: break;
            }
        }

        if (items.isEmpty()) throw new ApiException(400, "Debe agregar al menos un producto al detalle.");
        if (nit.isEmpty()) nit = "C/F";
        if (nombre.isEmpty()) nombre = "Consumidor Final";

        String tipoEntrega, estadoDespacho;
        switch (entrega.toUpperCase()) {
            case "RECOGER": tipoEntrega = "Recoger en tienda"; estadoDespacho = "Pendiente de Recoger"; break;
            case "ENVIO": tipoEntrega = "Envío a domicilio"; estadoDespacho = "Pendiente de Envío"; break;
            default: tipoEntrega = "Venta en tienda"; estadoDespacho = "Entregado"; break;
        }

        synchronized (LOCK_GUARDAR) {
            // Validar productos y stock contra MySQL (mismas reglas que FrmFactura)
            List<Producto> enBd = productoDAO.listar();
            if (enBd.isEmpty()) throw new ApiException(503, "No se pudieron leer los productos de MySQL.");
            Map<Integer, Producto> porId = new HashMap<>();
            for (Producto p : enBd) porId.put(p.getIdProducto(), p);

            Map<Integer, Integer> acumulado = new HashMap<>();
            Factura factura = new Factura(0, LocalDate.now().toString(), null);
            for (double[] it : items) {
                int idProd = (int) it[0];
                int cant = (int) it[1];
                double precio = it[2];
                Producto p = porId.get(idProd);
                if (p == null) throw new ApiException(400, "Producto no encontrado: ID " + idProd);
                if (cant <= 0) throw new ApiException(400, "La cantidad debe ser mayor a 0 (producto " + idProd + ").");
                if (precio < 0) throw new ApiException(400, "Precio inválido para el producto " + idProd);
                int total = acumulado.merge(idProd, cant, Integer::sum);
                if (total > p.getExistencia()) {
                    throw new ApiException(409, "Stock insuficiente para \"" + p.getNombre()
                            + "\". Disponible: " + p.getExistencia());
                }
                factura.agregarDetalle(new DetalleFactura(p, cant, precio));
            }

            // Cliente: igual que btnGuardarActionPerformed
            Cliente cliente = null;
            if (!nit.equalsIgnoreCase("C/F")) {
                cliente = clienteDAO.buscarPorNit(nit);
                if (cliente == null) {
                    Cliente nuevo = new Cliente();
                    nuevo.setNit(nit);
                    nuevo.setNombre(nombre);
                    nuevo.setDireccion(direccion.isEmpty() ? "Ciudad" : direccion);
                    clienteDAO.insertar(nuevo);
                    cliente = clienteDAO.buscarPorNit(nit);
                }
            }

            factura.setCliente(cliente);
            factura.setNitCliente(nit);
            factura.setNombreCliente(nombre);
            factura.setTipoEntrega(tipoEntrega);
            factura.setEstadoDespacho(estadoDespacho);

            if (!facturaDAO.guardar(factura)) {
                throw new ApiException(500, "No se pudo guardar la factura en MySQL (revise la consola del programa).");
            }
            return "{\"ok\":true,\"idFactura\":" + factura.getIdFactura()
                    + ",\"total\":" + factura.calcularTotal()
                    + ",\"tipoEntrega\":" + q(tipoEntrega) + "}";
        }
    }

    // ------------------------------------------------------------------ utilidades

    private static void manejar(HttpExchange ex, String metodo, Accion accion) throws IOException {
        try {
            if (!CLAVE_API.equals(ex.getRequestHeaders().getFirst("X-API-KEY"))) {
                throw new ApiException(401, "Clave de API incorrecta");
            }
            if (!metodo.equalsIgnoreCase(ex.getRequestMethod())) {
                throw new ApiException(405, "Método no permitido");
            }
            responder(ex, 200, accion.ejecutar(ex));
        } catch (ApiException e) {
            responder(ex, e.codigo, "{\"error\":" + q(e.getMessage()) + "}");
        } catch (Exception e) {
            e.printStackTrace();
            responder(ex, 500, "{\"error\":" + q("Error interno: " + e.getMessage()) + "}");
        }
    }

    private static void responder(HttpExchange ex, int codigo, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String param(HttpExchange ex, String nombre) throws UnsupportedEncodingException {
        String qs = ex.getRequestURI().getRawQuery();
        if (qs == null) return null;
        for (String par : qs.split("&")) {
            int i = par.indexOf('=');
            String k = i < 0 ? par : par.substring(0, i);
            if (k.equals(nombre)) return i < 0 ? "" : URLDecoder.decode(par.substring(i + 1), "UTF-8");
        }
        return null;
    }

    /** Texto -> literal JSON con comillas y escapes. null -> "". */
    private static String q(String s) {
        if (s == null) s = "";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.append('"').toString();
    }
}
