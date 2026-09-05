package gt.edu.umg.sistema.estudiantes.modelo;

public class DetalleFactura {
    private String producto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleFactura(String producto, int cantidad, double precio) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = cantidad * precio;
    }

    // Getters y Setters
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        this.subtotal = this.cantidad * this.precio;
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { 
        this.precio = precio; 
        this.subtotal = this.cantidad * this.precio;
    }

    public double getSubtotal() { return subtotal; }
}