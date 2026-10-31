package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.ClienteDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FrmCliente extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmCliente.class.getName());
    private final ClienteDAO clienteDAO = new ClienteDAO();

    // Tabla secundaria dinámica en memoria
    private JTable tblTelefonos;
    private DefaultTableModel modeloTelefonos;

    public FrmCliente() {
        initComponents();
        this.setTitle("Gestión de Clientes - MySQL");
        this.setLocationRelativeTo(null);

        inicializarTablaTelefonos();
        configurarTabla();
        agregarEventosBusqueda();
        agregarEventosTabla();
        conectarBotones();
        listarClientes();
    }

    private void inicializarTablaTelefonos() {
        modeloTelefonos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Número", "Tipo"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTelefonos = new JTable(modeloTelefonos);
        JScrollPane scrollTelefonos = new JScrollPane(tblTelefonos);

        // Posiciona la sub-tabla de teléfonos arriba del botón Guardar
        scrollTelefonos.setBounds(310, 30, 185, 90);
        this.getContentPane().add(scrollTelefonos);

        this.repaint();
    }

    private void btnAgregarTelActionPerformed(java.awt.event.ActionEvent evt) {
        agregarTelefonoALista();
    }

    private void btnQuitarTelActionPerformed(java.awt.event.ActionEvent evt) {
        quitarTelefonoDeLista();
    }

    private void cbTipoTelefonoActionPerformed(java.awt.event.ActionEvent evt) {
        // Evento opcional del combo box
    }

    private void agregarTelefonoALista() {
        String num = jTextField3.getText().trim();
        String tipo = cbTipoTelefono.getSelectedItem() != null ? cbTipoTelefono.getSelectedItem().toString() : "Personal";

        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de teléfono.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTelefonos.addRow(new Object[]{num, tipo});
        jTextField3.setText("");
        jTextField3.requestFocus();
    }

    private void quitarTelefonoDeLista() {
        int fila = tblTelefonos.getSelectedRow();
        if (fila >= 0) {
            modeloTelefonos.removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un teléfono de la lista para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void conectarBotones() {
        for (java.awt.event.ActionListener al : jButton1.getActionListeners()) {
            jButton1.removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : jButton2.getActionListeners()) {
            jButton2.removeActionListener(al);
        }

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
    }

    private void limpiarCampos() {
        jTextField1.setText(""); // Nombre
        jTextField2.setText(""); // NIT
        jTextField3.setText(""); // Telefono
        jTextField4.setText(""); // Direccion
        modeloTelefonos.setRowCount(0);
        jTextField1.requestFocus();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Nombre", "NIT", "Teléfono", "Dirección"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(modelo);
    }

    private void listarClientes() {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        try {
            List<Cliente> lista = clienteDAO.listar();
            for (Cliente c : lista) {
                StringBuilder tels = new StringBuilder();
                for (String[] t : c.getTelefonos()) {
                    if (tels.length() > 0) tels.append(", ");
                    tels.append(t[0]).append(" (").append(t[1]).append(")");
                }

                modelo.addRow(new Object[]{
                    c.getNombre(),
                    c.getNit(),
                    tels.toString(),
                    c.getDireccion()
                });
            }
        } catch (Exception e) {
            logger.warning("Error al listar clientes desde MySQL: " + e.getMessage());
        }
    }

    private void agregarEventosTabla() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = jTable1.getSelectedRow();
                if (fila >= 0) {
                    jTextField1.setText(jTable1.getValueAt(fila, 0) != null ? jTable1.getValueAt(fila, 0).toString() : "");
                    jTextField2.setText(jTable1.getValueAt(fila, 1) != null ? jTable1.getValueAt(fila, 1).toString() : "");
                    jTextField4.setText(jTable1.getValueAt(fila, 3) != null ? jTable1.getValueAt(fila, 3).toString() : "");
                    buscarClientePorNit();
                }
            }
        });
    }

    private void agregarEventosBusqueda() {
        jTextField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarClientePorNit();
                }
            }
        });

        jTextField2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!jTextField2.getText().trim().isEmpty()) {
                    buscarClientePorNit();
                }
            }
        });
    }

    private void buscarClientePorNit() {
        String nit = jTextField2.getText().trim();
        if (nit.isEmpty()) return;

        try {
            Cliente cliente = clienteDAO.buscarPorNit(nit);
            if (cliente != null) {
                jTextField1.setText(cliente.getNombre());
                jTextField4.setText(cliente.getDireccion());

                modeloTelefonos.setRowCount(0);
                for (String[] tel : cliente.getTelefonos()) {
                    modeloTelefonos.addRow(new Object[]{tel[0], tel[1]});
                }
            }
        } catch (Exception e) {
            logger.warning("Error al buscar cliente por NIT: " + e.getMessage());
        }
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nombre = jTextField1.getText().trim();
            String nit = jTextField2.getText().trim();
            String direccion = jTextField4.getText().trim();

            if (nombre.isEmpty() || nit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y NIT son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String telPendiente = jTextField3.getText().trim();
            if (!telPendiente.isEmpty()) {
                String tipo = cbTipoTelefono.getSelectedItem() != null ? cbTipoTelefono.getSelectedItem().toString() : "Personal";
                modeloTelefonos.addRow(new Object[]{telPendiente, tipo});
                jTextField3.setText("");
            }

            List<String[]> listaTelefonos = new ArrayList<>();
            for (int i = 0; i < modeloTelefonos.getRowCount(); i++) {
                String num = modeloTelefonos.getValueAt(i, 0).toString();
                String tipo = modeloTelefonos.getValueAt(i, 1).toString();
                listaTelefonos.add(new String[]{num, tipo});
            }

            Cliente clienteExistente = clienteDAO.buscarPorNit(nit);

            if (clienteExistente != null) {
                clienteExistente.setNombre(nombre);
                clienteExistente.setDireccion(direccion);
                clienteExistente.setTelefonos(listaTelefonos);

                if (clienteDAO.actualizar(clienteExistente)) {
                    JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.");
                    limpiarCampos();
                    listarClientes();
                }
            } else {
                Cliente nuevoCliente = new Cliente(0, nombre, nit, direccion);
                nuevoCliente.setTelefonos(listaTelefonos);
                if (clienteDAO.insertar(nuevoCliente) > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
                    limpiarCampos();
                    listarClientes();
                }
            }

        } catch (Exception e) {
            logger.warning("Error de MySQL: " + e.getMessage());
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String nit = jTextField2.getText().trim();
        if (nit.isEmpty()) {
            return;
        }

        try {
            Cliente cliente = clienteDAO.buscarPorNit(nit);
            if (cliente != null && clienteDAO.eliminar(cliente.getIdCliente())) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                limpiarCampos();
                listarClientes();
            }
        } catch (Exception e) {
            logger.warning("Error al eliminar en MySQL: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cbTipoTelefono = new javax.swing.JComboBox<>();
        btnAgregarTel = new javax.swing.JButton();
        btnQuitarTel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombre");

        jLabel2.setText("Telefono");

        jLabel3.setText("NIT");

        jLabel4.setText("Direccion");

        jButton1.setText("Guardar");

        jButton2.setText("Eliminar");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "NIT", "Telefono", "Direccion"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        cbTipoTelefono.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TipoTelefono", "Personal", "Trabajo", "Casa", "Otro" }));
        cbTipoTelefono.addActionListener(this::cbTipoTelefonoActionPerformed);

        btnAgregarTel.setText("AgregarTel");
        btnAgregarTel.addActionListener(this::btnAgregarTelActionPerformed);

        btnQuitarTel.setText("QuitarTel");
        btnQuitarTel.addActionListener(this::btnQuitarTelActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbTipoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(btnAgregarTel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuitarTel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnAgregarTel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnQuitarTel)
                    .addComponent(cbTipoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FrmCliente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarTel;
    private javax.swing.JButton btnQuitarTel;
    private javax.swing.JComboBox<String> cbTipoTelefono;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}