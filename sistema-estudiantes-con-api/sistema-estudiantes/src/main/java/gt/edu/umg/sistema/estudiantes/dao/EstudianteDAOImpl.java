package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    @Override
    public void guardar(Estudiante estudiante) {

        String sql = "INSERT INTO estudiantes "
                + "(id, nombres, apellidos, carnet, email) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, estudiante.getId());
            ps.setString(2, estudiante.getNombres());
            ps.setString(3, estudiante.getApellidos());
            ps.setString(4, estudiante.getCarnet());
            ps.setString(5, estudiante.getEmail());

            ps.executeUpdate();

            System.out.println("Estudiante guardado en la base de datos.");

        } catch (Exception e) {
            System.out.println("Error al guardar estudiante: "
                    + e.getMessage());
        }
    }

    @Override
    public List listar() {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT id, nombres, apellidos, carnet, email "
                + "FROM estudiantes";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Estudiante estudiante = new Estudiante();

                estudiante.setId(rs.getInt("id"));
                estudiante.setNombres(rs.getString("nombres"));
                estudiante.setApellidos(rs.getString("apellidos"));
                estudiante.setCarnet(rs.getString("carnet"));
                estudiante.setEmail(rs.getString("email"));

                estudiantes.add(estudiante);
            }

        } catch (Exception e) {
            System.out.println("Error al listar estudiantes: "
                    + e.getMessage());
        }

        return estudiantes;
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        throw new UnsupportedOperationException("No implementado todavía.");
    }

    @Override
    public void eliminar(int id) {
        throw new UnsupportedOperationException("No implementado todavía.");
    }
}

