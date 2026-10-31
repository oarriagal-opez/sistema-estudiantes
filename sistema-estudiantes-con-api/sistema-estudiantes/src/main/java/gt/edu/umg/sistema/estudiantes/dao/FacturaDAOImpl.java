package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements FacturaDAO {

    @Override
    public int obtenerSiguienteIdFactura() {
        String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                     "WHERE TABLE_SCHEMA = 'sistema_estudiantes' AND TABLE_NAME = 'factura'";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int siguienteId = rs.getInt(1);
                return siguienteId > 0 ? siguienteId : 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el siguiente ID de factura: " + e.getMessage());
        }
        return 1;
    }

    @Override
    public boolean insertar(Factura factura) {
        String sqlFactura = "INSERT INTO factura (id_cliente, fecha, total, tipo_entrega, estado_despacho) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";

        Connection con = null;
        PreparedStatement psFactura = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        ResultSet rs = null;

        try {
            con = Conexion.conectar();
            if (con == null) {
                System.err.println("Error: No se pudo establecer conexion con la base de datos.");
                return false;
            }

            con.setAutoCommit(false); // Iniciar transacción

            // Determinar tipo de entrega y estado de despacho
            String tipoEntrega = (factura.getTipoEntrega() != null && !factura.getTipoEntrega().isEmpty())
                                 ? factura.getTipoEntrega()
                                 : "venta en tienda";

            String estadoDespacho = tipoEntrega.equalsIgnoreCase("venta en tienda") ? "Completado" : "Pendiente";

            // 1. Insertar Factura
            psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);

            // Manejo de id_cliente
            if (factura.getCliente() != null && factura.getCliente().getIdCliente() > 0) {
                psFactura.setInt(1, factura.getCliente().getIdCliente());
            } else {
                psFactura.setNull(1, java.sql.Types.INTEGER);
            }

            // Manejo de Fecha (String a java.sql.Date)
            Date fechaSQL;
            try {
                fechaSQL = (factura.getFecha() != null && !factura.getFecha().trim().isEmpty())
                        ? Date.valueOf(factura.getFecha())
                        : new Date(System.currentTimeMillis());
            } catch (IllegalArgumentException e) {
                fechaSQL = new Date(System.currentTimeMillis());
            }
            psFactura.setDate(2, fechaSQL);

            psFactura.setDouble(3, factura.calcularTotal());
            psFactura.setString(4, tipoEntrega);
            psFactura.setString(5, estadoDespacho);

            int filasAfectadas = psFactura.executeUpdate();
            if (filasAfectadas == 0) {
                con.rollback();
                return false;
            }

            // Obtener el ID generado
            rs = psFactura.getGeneratedKeys();
            int idFacturaGenerada = 0;
            if (rs.next()) {
                idFacturaGenerada = rs.getInt(1);
                factura.setIdFactura(idFacturaGenerada);
            } else {
                con.rollback();
                return false;
            }

            // 2. Insertar DetalleFactura y actualizar stock
            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlStock);

            if (factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
                for (DetalleFactura detalle : factura.getDetalles()) {
                    
                    psDetalle.setInt(1, idFacturaGenerada);
                    psDetalle.setInt(2, detalle.getProducto().getIdProducto());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecioUnitario());
                    psDetalle.setDouble(5, detalle.calcularSubtotal());
                    psDetalle.executeUpdate();

                    // Descontar del inventario
                    psStock.setInt(1, detalle.getCantidad());
                    psStock.setInt(2, detalle.getProducto().getIdProducto());
                    psStock.setInt(3, detalle.getCantidad());

                    int stockActualizado = psStock.executeUpdate();
                    if (stockActualizado == 0) {
                        System.err.println("Error: Stock insuficiente para el producto ID " 
                                           + detalle.getProducto().getIdProducto());
                        con.rollback();
                        return false;
                    }
                }
            }

            con.commit(); // Confirmar la transacción
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar la factura en MySQL: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error durante rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psFactura != null) psFactura.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexion JDBC: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean guardar(Factura factura) {
        return insertar(factura);
    }

    @Override
    public List<Factura> listar() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT id_factura, id_cliente, fecha, total, tipo_entrega, estado_despacho FROM factura";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idFactura = rs.getInt("id_factura");
                int idCliente = rs.getInt("id_cliente");
                
                Cliente cliente = new Cliente();
                cliente.setIdCliente(idCliente);

                Date fechaSql = rs.getDate("fecha");
                String fecha = (fechaSql != null) ? fechaSql.toString() : java.time.LocalDate.now().toString();

                Factura f = new Factura(idFactura, fecha, cliente);
                f.setTotal(rs.getDouble("total"));
                f.setTipoEntrega(rs.getString("tipo_entrega"));
                f.setEstadoDespacho(rs.getString("estado_despacho"));
                
                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar las facturas: " + e.getMessage());
        }
        return lista;
    }
}