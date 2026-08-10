package gt.edu.umg.sistema.estudiantes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/Sistema_estudiantes";

    private static final String USUARIO = "root";

    private static final String PASSWORD = "1234";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void main(String[] args) {

        try {
            Connection conexion = conectar();
            System.out.println("Conexión exitosa a la base de datos.");
            conexion.close();

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}