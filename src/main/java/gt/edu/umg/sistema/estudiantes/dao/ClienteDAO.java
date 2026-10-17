package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Inserta la persona y guarda sus teléfonos
    public int insertar(Cliente cliente) {
        String sql = "INSERT INTO persona (nit, nombre, direccion) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNit());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        cliente.setIdCliente(idGenerado);
                        guardarTelefonos(idGenerado, cliente.getTelefonos());
                        return idGenerado;
                    }
                }
                return 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente en persona: " + e.getMessage());
        }
        return 0; // Error al insertar
    }

    // Búsqueda por ID utilizando id_persona
    public Cliente obtenerPorId(int id) {
        String sql = "SELECT id_persona, nit, nombre, direccion FROM persona WHERE id_persona = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_persona"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefonos(obtenerTelefonos(cliente.getIdCliente()));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente por ID de persona: " + e.getMessage());
        }
        return null;
    }

    // Búsqueda por NIT
    public Cliente buscarPorNit(String nit) {
        String sql = "SELECT id_persona, nit, nombre, direccion FROM persona WHERE nit = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_persona"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefonos(obtenerTelefonos(cliente.getIdCliente()));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por NIT en persona: " + e.getMessage());
        }
        return null;
    }

    // Listar todos los clientes con sus respectivos teléfonos
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_persona, nit, nombre, direccion FROM persona";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_persona"));
                cliente.setNit(rs.getString("nit"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefonos(obtenerTelefonos(cliente.getIdCliente()));
                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes desde persona: " + e.getMessage());
        }
        return lista;
    }

    // Actualiza cliente y sus teléfonos
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE persona SET nit = ?, nombre = ?, direccion = ? WHERE id_persona = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNit());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setInt(4, cliente.getIdCliente());

            boolean actualizado = ps.executeUpdate() > 0;
            if (actualizado) {
                // Sincronizar teléfonos: elimina los anteriores y guarda la lista nueva
                eliminarTelefonos(cliente.getIdCliente());
                guardarTelefonos(cliente.getIdCliente(), cliente.getTelefonos());
            }
            return actualizado;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente en persona: " + e.getMessage());
            return false;
        }
    }

    // Elimina cliente de persona (por CASCADE en MySQL, elimina sus teléfonos automáticamente)
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM persona WHERE id_persona = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente de persona: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // MÉTODOS AUXILIARES PRIVADOS PARA LA TABLA 'telefono'
    // =========================================================================

    private void guardarTelefonos(int idPersona, List<String[]> telefonos) {
        if (telefonos == null || telefonos.isEmpty()) return;

        String sql = "INSERT INTO telefono (id_persona, numero, tipo) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String[] tel : telefonos) {
                ps.setInt(1, idPersona);
                ps.setString(2, tel[0]); // Número
                ps.setString(3, tel[1]); // Tipo (Personal, Trabajo, Casa, Otro)
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar teléfonos: " + e.getMessage());
        }
    }

    private List<String[]> obtenerTelefonos(int idPersona) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT numero, tipo FROM telefono WHERE id_persona = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{rs.getString("numero"), rs.getString("tipo")});
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener teléfonos: " + e.getMessage());
        }
        return lista;
    }

    private void eliminarTelefonos(int idPersona) {
        String sql = "DELETE FROM telefono WHERE id_persona = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPersona);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar teléfonos: " + e.getMessage());
        }
    }
}