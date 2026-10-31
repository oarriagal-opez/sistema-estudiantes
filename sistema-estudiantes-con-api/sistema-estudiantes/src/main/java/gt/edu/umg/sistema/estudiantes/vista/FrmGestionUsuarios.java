package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.UsuarioDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Formulario de Gestión de Usuarios y Permisos conectado a MySQL
 */
public class FrmGestionUsuarios extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public FrmGestionUsuarios() {
        initComponents();
        this.setTitle("Gestión de Usuarios, Permisos y Módulos");
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);

        configurarTabla();
        listarUsuariosBD();
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Usuario", "Contraseña", "Rol", "Módulos Permitidos"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsuarios.setModel(modeloTabla);
    }

    private void listarUsuariosBD() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listar();

        for (Usuario u : lista) {
            // Consulta los permisos asignados en MySQL para este usuario
            List<String> permisosBD = usuarioDAO.obtenerModulosPorUsername(u.getUsername());
            String modulos = String.join(", ", permisosBD);

            if (modulos.isEmpty()) {
                modulos = "Ninguno";
            }

            modeloTabla.addRow(new Object[]{
                u.getUsername(),
                u.getPassword(),
                u.getRol(),
                modulos
            });
        }
    }

    private List<String> obtenerListaCheckboxesSeleccionados() {
        List<String> seleccionados = new ArrayList<>();
        if (chkClientes.isSelected()) seleccionados.add("Clientes");
        if (chkEmpleados.isSelected()) seleccionados.add("Empleados");
        if (chkProductos.isSelected()) seleccionados.add("Productos");
        if (chkUsuarios.isSelected()) seleccionados.add("Usuarios");
        if (chkFacturas.isSelected()) seleccionados.add("Facturas");
        return seleccionados;
    }

    private void cargarModulosEnCheckboxes(String modulosCadena) {
        chkClientes.setSelected(modulosCadena.contains("Clientes"));
        chkEmpleados.setSelected(modulosCadena.contains("Empleados"));
        chkProductos.setSelected(modulosCadena.contains("Productos"));
        chkUsuarios.setSelected(modulosCadena.contains("Usuarios"));
        chkFacturas.setSelected(modulosCadena.contains("Facturas"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        txtUsuario = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        cmbRol = new javax.swing.JComboBox<>();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        chkClientes = new javax.swing.JCheckBox();
        chkEmpleados = new javax.swing.JCheckBox();
        chkProductos = new javax.swing.JCheckBox();
        chkUsuarios = new javax.swing.JCheckBox();
        chkFacturas = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Usuario:");
        jLabel2.setText("Nombre Completo:");
        jLabel3.setText("Contraseña:");
        jLabel4.setText("Rol / Permisos:");

        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuarios);

        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "VENDEDOR", "OPERADOR" }));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Permisos de Pestañas"));

        chkClientes.setText("Clientes");
        chkEmpleados.setText("Empleados");
        chkProductos.setText("Productos");
        chkUsuarios.setText("Usuarios");
        chkFacturas.setText("Facturas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(chkClientes)
                .addGap(18, 18, 18)
                .addComponent(chkEmpleados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkProductos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkFacturas)
                .addGap(0, 233, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkClientes)
                    .addComponent(chkEmpleados)
                    .addComponent(chkProductos)
                    .addComponent(chkUsuarios)
                    .addComponent(chkFacturas))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                    .addComponent(cmbRol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(btnAgregar)
                        .addGap(42, 42, 42)
                        .addComponent(btnModificar)
                        .addGap(45, 45, 45)
                        .addComponent(btnEliminar)
                        .addGap(42, 42, 42)
                        .addComponent(btnLimpiar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        pack();
    }// </editor-fold>                        

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el usuario y la contraseña.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuario);
        nuevoUsuario.setPassword(pass);
        nuevoUsuario.setRol(rol);

        if (usuarioDAO.insertar(nuevoUsuario)) {
            // Guardar los CheckBoxes seleccionados en la BD
            List<String> modulosSeleccionados = obtenerListaCheckboxesSeleccionados();
            usuarioDAO.guardarPermisosPorUsername(usuario, modulosSeleccionados);

            JOptionPane.showMessageDialog(this, "Usuario y permisos registrados exitosamente en MySQL.");
            listarUsuariosBD();
            btnLimpiarActionPerformed(evt);
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar usuario (el nombre de usuario ya podría existir).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                          

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario e ingrese los datos a modificar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioActualizar = new Usuario();
        usuarioActualizar.setUsername(usuario);
        usuarioActualizar.setPassword(pass);
        usuarioActualizar.setRol(rol);

        if (usuarioDAO.actualizar(usuarioActualizar)) {
            // Actualizar los permisos marcados en los CheckBoxes
            List<String> modulosSeleccionados = obtenerListaCheckboxesSeleccionados();
            usuarioDAO.guardarPermisosPorUsername(usuario, modulosSeleccionados);

            JOptionPane.showMessageDialog(this, "Usuario y permisos actualizados correctamente en MySQL.");
            listarUsuariosBD();
            btnLimpiarActionPerformed(evt);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                            

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        String usuario = txtUsuario.getText().trim();
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el usuario '" + usuario + "' de MySQL?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                listarUsuariosBD();
                btnLimpiarActionPerformed(evt);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                           

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        txtUsuario.setText("");
        txtPassword.setText("");
        txtNombre.setText("");
        cmbRol.setSelectedIndex(0);
        chkClientes.setSelected(false);
        chkEmpleados.setSelected(false);
        chkProductos.setSelected(false);
        chkUsuarios.setSelected(false);
        chkFacturas.setSelected(false);
        tblUsuarios.clearSelection();
        txtUsuario.requestFocus();
    }                                          

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {                                         
        int fila = tblUsuarios.getSelectedRow();
        if (fila != -1) {
            txtUsuario.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtPassword.setText(modeloTabla.getValueAt(fila, 1).toString());
            cmbRol.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());

            String modulosGuardados = modeloTabla.getValueAt(fila, 3).toString();
            cargarModulosEnCheckboxes(modulosGuardados);
        }
    }                                        

    // Declaration of variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JCheckBox chkClientes;
    private javax.swing.JCheckBox chkEmpleados;
    private javax.swing.JCheckBox chkFacturas;
    private javax.swing.JCheckBox chkProductos;
    private javax.swing.JCheckBox chkUsuarios;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration                  
}