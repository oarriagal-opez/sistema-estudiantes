package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String nit;
    private String direccion;
    // Lista de teléfonos: [0] = Número, [1] = Tipo (Personal, Trabajo, Casa, Otro)
    private List<String[]> telefonos = new ArrayList<>();

    // Constructor vacío
    public Cliente() {
    }

    // Constructor de 4 parámetros
    public Cliente(int idCliente, String nombre, String nit, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
    }

    // CONSTRUCTOR DE COMPATIBILIDAD (5 parámetros)
    public Cliente(int idCliente, String nombre, String nit, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        if (telefono != null && !telefono.trim().isEmpty()) {
            this.telefonos.add(new String[]{telefono.trim(), "Personal"});
        }
    }

    // Getters y Setters principales
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String[]> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String[]> telefonos) {
        this.telefonos = telefonos;
    }

    // MÉTODOS DE COMPATIBILIDAD CON FRMCLIENTE / FRMFACTURA
    public String getTelefono() {
        if (telefonos != null && !telefonos.isEmpty()) {
            return telefonos.get(0)[0];
        }
        return "";
    }

    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (this.telefonos == null) {
                this.telefonos = new ArrayList<>();
            } else {
                this.telefonos.clear();
            }
            this.telefonos.add(new String[]{telefono.trim(), "Personal"});
        }
    }

    public void agregarTelefono(String numero, String tipo) {
        this.telefonos.add(new String[]{numero, tipo});
    }
}