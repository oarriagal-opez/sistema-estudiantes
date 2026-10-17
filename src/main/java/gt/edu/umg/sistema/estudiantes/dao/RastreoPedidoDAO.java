package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.Conexion;
import gt.edu.umg.sistema.estudiantes.modelo.RastreoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RastreoPedidoDAO {

    private static final Logger logger = Logger.getLogger(RastreoPedidoDAO.class.getName());

    public List<RastreoPedido> listarTodosLosPedidos() {
        List<RastreoPedido> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, COALESCE(p.nombre, 'Consumidor Final') AS cliente, "
                + "COALESCE(p.direccion, 'Ciudad') AS direccion, "
                + "d.tipo_entrega, d.estado, f.fecha "
                + "FROM despacho d "
                + "JOIN factura f ON d.id_factura = f.id_factura "
                + "LEFT JOIN persona p ON f.id_cliente = p.id_persona "
                + "ORDER BY f.id_factura DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RastreoPedido p = new RastreoPedido();
                p.setIdFactura(rs.getInt("id_factura"));
                p.setCliente(rs.getString("cliente"));
                p.setDireccion(rs.getString("direccion"));
                p.setTipoEntrega(rs.getString("tipo_entrega"));
                p.setEstadoDespacho(rs.getString("estado"));
                p.setFecha(rs.getString("fecha"));
                lista.add(p);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al consultar pedidos de rastreo", e);
        }
        return lista;
    }

    public boolean actualizarEstado(int idFactura, String nuevoEstado) {
        String sql = "UPDATE despacho SET estado = ? WHERE id_factura = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idFactura);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar estado del pedido", e);
            return false;
        }
    }
}