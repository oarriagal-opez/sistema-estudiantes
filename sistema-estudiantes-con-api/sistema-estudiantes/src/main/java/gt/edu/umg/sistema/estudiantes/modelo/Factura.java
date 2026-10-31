package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Factura {

    private int idFactura;
    private String fecha;
    private Cliente cliente;
    private List<DetalleFactura> detalles = new ArrayList<>();

    // Atributos de factura
    private double total;
    private String tipoEntrega;
    private String estadoDespacho;
    private String nitCliente;
    private String nombreCliente;

    // Constructores
    public Factura() {
    }

    public Factura(int idFactura, String fecha, Cliente cliente) {
        this.idFactura = idFactura;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    // Getters y Setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getEstadoDespacho() {
        return estadoDespacho;
    }

    public void setEstadoDespacho(String estadoDespacho) {
        this.estadoDespacho = estadoDespacho;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    // Métodos de negocio
    public void agregarDetalle(DetalleFactura detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }

    public double calcularTotal() {
        double totalCalculado = 0.0;
        if (detalles != null) {
            for (DetalleFactura d : detalles) {
                totalCalculado += d.calcularSubtotal();
            }
        }
        return totalCalculado;
    }
}