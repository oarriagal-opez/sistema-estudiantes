package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, username, password, rol FROM usuario";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuario (username, password, rol) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuario SET password = ?, rol = ? WHERE username = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getPassword());
            ps.setString(2, u.getRol());
            ps.setString(3, u.getUsername());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String username) {
        String sql = "DELETE FROM usuario WHERE username = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS NUEVOS PARA MANEJO DE PERMISOS
    // =========================================================================

    /**
     * Obtiene el id_usuario a partir del username
     */
    public int obtenerIdPorUsername(String username) {
        String sql = "SELECT id_usuario FROM usuario WHERE username = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Guarda o actualiza los permisos asociados a un username en usuario_permiso
     */
    public boolean guardarPermisosPorUsername(String username, List<String> modulosSeleccionados) {
        int idUsuario = obtenerIdPorUsername(username);
        if (idUsuario == -1) return false;

        String sqlLimpiar = "DELETE FROM usuario_permiso WHERE id_usuario = ?";
        String sqlInsertar = "INSERT INTO usuario_permiso (id_usuario, id_modulo) " +
                             "SELECT ?, id_modulo FROM modulo WHERE nombre_modulo = ?";

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            // 1. Limpiar permisos viejos
            try (PreparedStatement psDel = con.prepareStatement(sqlLimpiar)) {
                psDel.setInt(1, idUsuario);
                psDel.executeUpdate();
            }

            // 2. Insertar permisos nuevos seleccionados
            try (PreparedStatement psIns = con.prepareStatement(sqlInsertar)) {
                for (String modulo : modulosSeleccionados) {
                    psIns.setInt(1, idUsuario);
                    psIns.setString(2, modulo);
                    psIns.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar permisos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los nombres de los módulos asignados a un usuario por su username
     */
    public List<String> obtenerModulosPorUsername(String username) {
        List<String> modulos = new ArrayList<>();
        String sql = "SELECT m.nombre_modulo FROM usuario_permiso up " +
                     "INNER JOIN modulo m ON up.id_modulo = m.id_modulo " +
                     "INNER JOIN usuario u ON up.id_usuario = u.id_usuario " +
                     "WHERE u.username = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modulos.add(rs.getString("nombre_modulo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener módulos del usuario: " + e.getMessage());
        }
        return modulos;
    }
}