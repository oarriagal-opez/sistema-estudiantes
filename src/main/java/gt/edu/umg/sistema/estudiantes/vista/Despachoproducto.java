package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.DespachoproductoDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Despachoproducto extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;
    private final DespachoproductoDAO despachoDAO = new DespachoproductoDAO();

    public Despachoproducto() {
        initComponents();
        configurarTabla();
        cargarFacturasPendientes();
        agregarEventos();
    }

    private void configurarTabla() {
        String[] columnas = {"No. Factura", "NIT", "Cliente", "Fecha", "Tipo Entrega", "Estado Despacho"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDespachos.setModel(modeloTabla);
    }

    private void cargarFacturasPendientes() {
        modeloTabla.setRowCount(0);
        try {
            List<Factura> lista = despachoDAO.listarFacturasPendientes();
            for (Factura f : lista) {
                modeloTabla.addRow(new Object[]{
                    f.getIdFactura(),
                    f.getNitCliente() != null ? f.getNitCliente() : "C/F",
                    f.getNombreCliente() != null ? f.getNombreCliente() : "Consumidor Final",
                    f.getFecha(),
                    f.getTipoEntrega(),
                    f.getEstadoDespacho()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar despachos desde MySQL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarEventos() {
        txtBuscarFactura.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarFactura(txtBuscarFactura.getText().trim());
            }
        });
    }

    private void filtrarFactura(String criterio) {
        if (criterio.isEmpty()) {
            cargarFacturasPendientes();
            return;
        }
        for (int i = modeloTabla.getRowCount() - 1; i >= 0; i--) {
            String noFactura = modeloTabla.getValueAt(i, 0).toString();
            String cliente = modeloTabla.getValueAt(i, 2).toString().toLowerCase();
            if (!noFactura.contains(criterio) && !cliente.contains(criterio.toLowerCase())) {
                modeloTabla.removeRow(i);
            }
        }
    }

    private void despacharPedido() {
        int filaSeleccionada = tblDespachos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idFactura = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "¿Desea despachar el pedido de la Factura No. " + idFactura + "?\nAl despacharlo iniciará su seguimiento en el módulo de Rastreo.",
                "Confirmar Despacho",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean actualizado = despachoDAO.actualizarEstadoDespacho(idFactura, "Empaquetando");
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Producto despachado exitosamente. Ya se encuentra disponible en Rastreo de Pedido.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarFacturasPendientes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado en la base de datos.", "Error DB", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para despachar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarFactura = new java.awt.TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDespachos = new javax.swing.JTable();
        btnDespachar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Modulo  de  Despacho y  Entrega");

        jLabel2.setText("Buscar por No.  Facatura o Cliente ");

        txtBuscarFactura.addActionListener(this::txtBuscarFacturaActionPerformed);

        tblDespachos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No. Factura", "NIT", "Cliente", "Fecha", "Tipo Entrega", "Estado de despacho"
            }
        ));
        jScrollPane1.setViewportView(tblDespachos);

        btnDespachar.setText("Marcar como Despachado");
        btnDespachar.addActionListener(this::btnDespacharActionPerformed);

        btnActualizar.setText("Refrescar Lista");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(61, 61, 61)
                                .addComponent(txtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(btnDespachar, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDespachar)
                    .addComponent(btnActualizar))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarFacturaActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        filtrarFactura(txtBuscarFactura.getText().trim());
    }                                                

    private void btnDespacharActionPerformed(java.awt.event.ActionEvent evt) {                                             
        despacharPedido();
    }                                            

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        cargarFacturasPendientes();
    }                                            

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnDespachar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDespachos;
    private java.awt.TextField txtBuscarFactura;
    // End of variables declaration//GEN-END:variables
}