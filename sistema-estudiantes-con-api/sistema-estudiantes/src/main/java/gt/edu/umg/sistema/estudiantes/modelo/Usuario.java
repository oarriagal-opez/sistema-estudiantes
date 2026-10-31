package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    private String username;
    private String password;
    private String nombre;
    private String rol; // "ADMIN", "VENDEDOR", "OPERADOR"
    
    // Lista para almacenar los códigos de módulos autorizados (ej: 'MOD_ESTUDIANTES', 'MOD_PRODUCTOS')
    private List<String> permisos = new ArrayList<>();

    public Usuario(String username, String password, String nombre, String rol) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Usuario() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Métodos para la gestión de permisos por módulo
    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }

    /**
     * Verifica si el usuario tiene permiso para acceder a un módulo específico.
     * Si el rol es 'ADMIN', otorga acceso total automáticamente.
     */
    public boolean tienePermiso(String codigoModulo) {
        if ("ADMIN".equalsIgnoreCase(this.rol)) {
            return true;
        }
        return permisos != null && permisos.contains(codigoModulo);
    }
}