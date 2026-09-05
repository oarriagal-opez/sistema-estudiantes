package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String numeroFactura;
    private String cliente;
    private String fecha;
    private double total;
    private List<DetalleFactura> detalles;

    public Factura() {
        this.detalles = new ArrayList<>();
    }

    public Factura(String numeroFactura, String cliente, String fecha, double total) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }
}