package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.ProductoDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmProducto extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;
    private final ProductoDAO productoDAO = new ProductoDAO();

    public FrmProducto() {
        initComponents();
        configurarTabla();
        cargarDatosDesdeBD(); // Carga automática al abrir la ventana
        jTextField1.setEditable(false); // Deshabilita el ID porque MySQL lo asigna automáticamente (AUTO_INCREMENT)
    }

    // Configura la estructura y columnas del modelo de la tabla
    private void configurarTabla() {
        String[] columnas = {"ID Producto", "Nombre", "Precio", "Existencia"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen la tabla directamente
            }
        };
        tblProductos.setModel(modeloTabla);
    }

    // Método que consulta MySQL y llena el DefaultTableModel
    private void cargarDatosDesdeBD() {
        modeloTabla.setRowCount(0); // Limpia filas previas
        List<Producto> lista = productoDAO.listar();
        
        for (Producto p : lista) {
            Object[] fila = {
                p.getIdProducto(),
                p.getNombre(),
                p.getPrecio(),
                p.getExistencia()
            };
            modeloTabla.addRow(fila); // Agrega cada registro al modelo
        }
    }

    private void limpiarCampos() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        tblProductos.clearSelection();
        jTextField2.requestFocus(); // Enfoca directamente en el campo Nombre
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        btnIngresar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnIngresar1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestión de Inventario y Productos");

        btnIngresar.setText("Ver Inventario");
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductos);

        jLabel1.setText("ID producto");

        jLabel2.setText("Nombre");

        jLabel3.setText("Precio");

        jLabel4.setText("Existencia");

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnEditar.setText("Editar");
        btnEditar.addActionListener(this::btnEditarActionPerformed);

        btnIngresar1.setText("Ingresar");
        btnIngresar1.addActionListener(this::btnIngresar1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLimpiar)
                            .addComponent(btnIngresar)
                            .addComponent(btnEliminar)
                            .addComponent(btnEditar)
                            .addComponent(btnIngresar1))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIngresar)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIngresar1)
                        .addGap(8, 8, 8)
                        .addComponent(btnLimpiar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditar)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    // BOTÓN VER INVENTARIO: Refresca el modelo de la tabla desde MySQL
    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        cargarDatosDesdeBD();
        JOptionPane.showMessageDialog(this, "Inventario cargado en el modelo de la tabla.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }                                           

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        limpiarCampos();
    }                                           

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int fila = tblProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(jTextField1.getText().trim());
            String nombre = jTextField2.getText().trim();
            double precio = Double.parseDouble(jTextField3.getText().trim());
            int existencia = Integer.parseInt(jTextField4.getText().trim());

            Producto producto = new Producto(id, nombre, precio, existencia);

            if (productoDAO.actualizar(producto)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
                cargarDatosDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese datos numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }                                        

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        int fila = tblProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        int resp = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el producto seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(idProducto)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
                cargarDatosDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                           

    // Pasa los datos del modelo a las cajas de texto al hacer clic
    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {                                         
        int fila = tblProductos.getSelectedRow();
        if (fila >= 0) {
            jTextField1.setText(modeloTabla.getValueAt(fila, 0).toString());
            jTextField2.setText(modeloTabla.getValueAt(fila, 1).toString());
            jTextField3.setText(modeloTabla.getValueAt(fila, 2).toString());
            jTextField4.setText(modeloTabla.getValueAt(fila, 3).toString());
        }
    }                                        

    // BOTÓN INGRESAR: Agrega a MySQL y refresca el modelo sin enviar ID manual
    private void btnIngresar1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try {
            // Solo valida Nombre, Precio y Existencia (El ID lo genera automáticamente MySQL)
            if (jTextField2.getText().trim().isEmpty() || jTextField3.getText().trim().isEmpty() || jTextField4.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre, Precio y Existencia son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = jTextField2.getText().trim();
            double precio = Double.parseDouble(jTextField3.getText().trim());
            int existencia = Integer.parseInt(jTextField4.getText().trim());

            // Se pasa 0 como ID temporal; en el DAO no se debe usar el ID al hacer INSERT
            Producto producto = new Producto(0, nombre, precio, existencia);

            if (productoDAO.insertar(producto)) {
                JOptionPane.showMessageDialog(this, "Producto guardado correctamente en la base de datos.");
                cargarDatosDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en MySQL. Verifique su consulta de inserción.", "Error DB", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese datos numéricos válidos para Precio y Existencia.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }                                            

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnIngresar1;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tblProductos;
    // End of variables declaration                   
}