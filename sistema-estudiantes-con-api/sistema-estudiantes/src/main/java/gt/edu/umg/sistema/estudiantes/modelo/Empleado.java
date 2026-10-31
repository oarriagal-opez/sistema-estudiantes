package gt.edu.umg.sistema.estudiantes.modelo;

public class Empleado extends Persona {
    private String codigoEmpleado;
    private String puesto;

    public Empleado(int id, String nombre, String nit, String telefono, String codigoEmpleado, String puesto) {
        super(id, nombre, nit, telefono);
        this.codigoEmpleado = codigoEmpleado;
        this.puesto = puesto;
    }

    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() + ", Código: " + codigoEmpleado + ", Puesto: " + puesto;
    }
}