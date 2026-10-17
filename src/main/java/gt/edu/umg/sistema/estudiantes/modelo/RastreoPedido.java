package gt.edu.umg.sistema.estudiantes.modelo;

public class RastreoPedido {
    private int idFactura;
    private String cliente;
    private String direccion;
    private String tipoEntrega;
    private String estadoDespacho;
    private String fecha;

    public RastreoPedido() {
    }

    public RastreoPedido(int idFactura, String cliente, String direccion, String tipoEntrega, String estadoDespacho, String fecha) {
        this.idFactura = idFactura;
        this.cliente = cliente;
        this.direccion = direccion;
        this.tipoEntrega = tipoEntrega;
        this.estadoDespacho = estadoDespacho;
        this.fecha = fecha;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}