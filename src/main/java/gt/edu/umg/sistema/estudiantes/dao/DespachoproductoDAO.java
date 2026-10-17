package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DespachoproductoDAO {

    public List<Factura> listarFacturasPendientes() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, p.nit, p.nombre AS cliente, f.fecha, "
                + "COALESCE(d.tipo_entrega, f.tipo_entrega) AS tipo_entrega, "
                + "COALESCE(d.estado, f.estado_despacho) AS estado "
                + "FROM factura f "
                + "LEFT JOIN persona p ON f.id_cliente = p.id_persona "
                + "LEFT JOIN despacho d ON f.id_factura = d.id_factura "
                + "WHERE d.estado IS NULL OR d.estado = 'Pendiente' "
                + "ORDER BY f.id_factura DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setNitCliente(rs.getString("nit"));
                f.setNombreCliente(rs.getString("cliente"));
                f.setFecha(rs.getString("fecha"));
                f.setTipoEntrega(rs.getString("tipo_entrega"));
                f.setEstadoDespacho(rs.getString("estado"));
                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar facturas pendientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoDespacho(int idFactura, String nuevoEstado) {
        String sqlCheck = "SELECT COUNT(*) FROM despacho WHERE id_factura = ?";
        String sqlInsert = "INSERT INTO despacho (id_factura, tipo_entrega, estado) VALUES (?, 'A domicilio', ?)";
        String sqlUpdate = "UPDATE despacho SET estado = ? WHERE id_factura = ?";

        try (Connection con = Conexion.conectar()) {
            
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idFactura);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                        psUpdate.setString(1, nuevoEstado);
                        psUpdate.setInt(2, idFactura);
                        return psUpdate.executeUpdate() > 0;
                    }
                } else {
                    try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, idFactura);
                        psInsert.setString(2, nuevoEstado);
                        return psInsert.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar/insertar despacho: " + e.getMessage());
            return false;
        }
    }
}