package gt.edu.umg.sistema.estudiantes.modelo;

public abstract class Persona {
    private int id;
    private String nombre;
    private String nit;
    private String telefono;

    public Persona(int id, String nombre, String nit, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String mostrarInformacion() {
        return "ID: " + id + ", Nombre: " + nombre + ", NIT: " + nit + ", Teléfono: " + telefono;
    }
}