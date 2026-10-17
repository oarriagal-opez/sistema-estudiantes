package gt.edu.umg.sistema.estudiantes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = 
            "jdbc:mysql://localhost:3306/sistema_estudiantes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USUARIO = "root";
    private static final String PASSWORD = "Izquierdo";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conexion = conectar()) {
            if (conexion != null) {
                System.out.println("¡Conexión exitosa a la base de datos MySQL!");
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}