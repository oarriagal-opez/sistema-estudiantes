package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.RastreoPedidoDAO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RastreoPedido extends JInternalFrame {

    private DefaultTableModel modeloTabla;
    private JTable tblPedidos;
    private JComboBox<String> cbEstado;
    private JButton btnActualizarEstado;
    private JButton btnRefrescar;
    private final RastreoPedidoDAO dao = new RastreoPedidoDAO();

    public RastreoPedido() {
        super("Módulo de Rastreo y Seguimiento de Pedidos", true, true, true, true);
        setSize(750, 420);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("Seguimiento de Estado de Envío");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelSuperior.add(lblTitulo);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"No. Factura", "Cliente", "Dirección", "Tipo Entrega", "Estado Actual", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPedidos = new JTable(modeloTabla);
        add(new JScrollPane(tblPedidos), BorderLayout.CENTER);

        // Panel Inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(new JLabel("Cambiar Estado a:"));

        cbEstado = new JComboBox<>(new String[]{"Empaquetando", "En camino", "Entregado"});
        panelInferior.add(cbEstado);

        btnActualizarEstado = new JButton("Actualizar Estado");
        btnActualizarEstado.setBackground(new Color(30, 144, 255));
        btnActualizarEstado.setForeground(Color.WHITE);
        panelInferior.add(btnActualizarEstado);

        btnRefrescar = new JButton("Refrescar");
        panelInferior.add(btnRefrescar);

        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnActualizarEstado.addActionListener(e -> cambiarEstado());
        btnRefrescar.addActionListener(e -> cargarDatos());

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<gt.edu.umg.sistema.estudiantes.modelo.RastreoPedido> lista = dao.listarTodosLosPedidos();
        for (gt.edu.umg.sistema.estudiantes.modelo.RastreoPedido r : lista) {
            modeloTabla.addRow(new Object[]{
                r.getIdFactura(),
                r.getCliente(),
                r.getDireccion(),
                r.getTipoEntrega(),
                r.getEstadoDespacho(),
                r.getFecha()
            });
        }
    }

    private void cambiarEstado() {
        int fila = tblPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idFactura = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nuevoEstado = cbEstado.getSelectedItem().toString();

        if (dao.actualizarEstado(idFactura, nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "Estado actualizado a '" + nuevoEstado + "' correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}