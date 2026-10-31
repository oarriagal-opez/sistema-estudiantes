package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.ClienteDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.ProductoDAO;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmFactura extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;
    private final FacturaDAO facturaDAO = new FacturaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    private List<Producto> listaProductosBD = new ArrayList<>();
    private Cliente clienteSeleccionado = null;
    private Producto productoSeleccionado = null;
    private ButtonGroup grupoEntrega;

    private JLabel NumeroFactura1, lblDireccion, Cliente, Cliente1, jLabel2, jLabel6, jLabel3, jLabel4, jLabel5, Total, lblTotal;
    private JTextField txtNumeroFactura, txtDireccion, txtCliente, txtCliente1, txtFecha, txtProducto, txtCantidad, txtPrecio;
    private JComboBox<String> cbxProductos;
    private JButton btnAgregar, btnEliminar, btnGuardar;
    private JRadioButton rbVentaTienda, rbRecoger, rbEnvio;
    private JTable tblDetalle;
    private JScrollPane jScrollPane1;

    public FrmFactura() {
        initComponents();
        agruparRadioButtons();
        configurarTabla();

        if (txtFecha != null) {
            txtFecha.setText(LocalDate.now().toString());
            txtFecha.setEditable(false);
        }
        if (txtNumeroFactura != null) {
            txtNumeroFactura.setEditable(false);
        }

        cargarProductosEnCombo();
        generarSiguienteNumeroFactura();
        agregarEventosBusqueda();
    }

    private void initComponents() {
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Formulario de Facturación");

        NumeroFactura1 = new JLabel("No. Factura:");
        lblDireccion = new JLabel("Dirección:");
        Cliente = new JLabel("NIT Cliente:");
        Cliente1 = new JLabel("Nombre Cliente:");
        jLabel2 = new JLabel("Fecha:");
        jLabel6 = new JLabel("ID Producto:");
        jLabel3 = new JLabel("Seleccionar:");
        jLabel4 = new JLabel("Cantidad:");
        jLabel5 = new JLabel("Precio:");
        Total = new JLabel("Total: Q");
        Total.setFont(new java.awt.Font("Tahoma", 1, 12));

        lblTotal = new JLabel("0.00");
        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12));

        txtNumeroFactura = new JTextField();
        txtDireccion = new JTextField();
        txtCliente = new JTextField();
        txtCliente1 = new JTextField();
        txtFecha = new JTextField();
        txtProducto = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();

        cbxProductos = new JComboBox<>();
        cbxProductos.setModel(new DefaultComboBoxModel<>(new String[] { "--Seleccionar producto--" }));
        cbxProductos.addActionListener(e -> cbxProductosActionPerformed(e));

        btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> btnAgregarActionPerformed(e));

        btnEliminar = new JButton("Eliminar Selección");
        btnEliminar.addActionListener(e -> btnEliminarActionPerformed(e));

        btnGuardar = new JButton("Guardar Factura");
        btnGuardar.addActionListener(e -> btnGuardarActionPerformed(e));

        rbVentaTienda = new JRadioButton("Venta en tienda");
        rbRecoger = new JRadioButton("Recoger en tienda");
        rbEnvio = new JRadioButton("Envío a domicilio");

        tblDetalle = new JTable();
        jScrollPane1 = new JScrollPane(tblDetalle);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(NumeroFactura1)
                            .addComponent(lblDireccion)
                            .addComponent(Cliente)
                            .addComponent(Cliente1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(Total))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTotal, 100, 100, 100)
                            .addComponent(txtPrecio)
                            .addComponent(txtCantidad)
                            .addComponent(cbxProductos, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtProducto)
                            .addComponent(txtFecha)
                            .addComponent(txtCliente1)
                            .addComponent(txtCliente)
                            .addComponent(txtDireccion)
                            .addComponent(txtNumeroFactura)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAgregar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEliminar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuardar, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(rbVentaTienda)
                            .addComponent(rbRecoger)
                            .addComponent(rbEnvio))))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(NumeroFactura1)
                            .addComponent(txtNumeroFactura, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDireccion)
                            .addComponent(txtDireccion, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(Cliente)
                            .addComponent(txtCliente, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(Cliente1)
                            .addComponent(txtCliente1, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtFecha, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtProducto, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbxProductos, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCantidad, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPrecio, 25, 25, 25))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(Total)
                            .addComponent(lblTotal))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregar)
                            .addComponent(rbVentaTienda))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminar)
                            .addComponent(rbRecoger))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(rbEnvio))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void agruparRadioButtons() {
        grupoEntrega = new ButtonGroup();
        grupoEntrega.add(rbRecoger);
        grupoEntrega.add(rbEnvio);
        grupoEntrega.add(rbVentaTienda);
        rbVentaTienda.setSelected(true);
    }

    private void configurarTabla() {
        String[] columnas = {"ID Prod", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalle.setModel(modeloTabla);
    }

    private void generarSiguienteNumeroFactura() {
        try {
            int siguienteId = facturaDAO.obtenerSiguienteIdFactura();
            txtNumeroFactura.setText(String.valueOf(siguienteId));
        } catch (Exception e) {
            txtNumeroFactura.setText("1");
        }
    }

    private void cargarProductosEnCombo() {
        try {
            listaProductosBD = productoDAO.listar();
            if (listaProductosBD == null) {
                listaProductosBD = new ArrayList<>();
            }
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            comboModel.addElement("-- Seleccione Producto --");
            for (Producto p : listaProductosBD) {
                comboModel.addElement(p.getIdProducto() + " - " + p.getNombre() + " (Stock: " + p.getExistencia() + ")");
            }
            cbxProductos.setModel(comboModel);
        } catch (Exception e) {
            System.err.println("Error al cargar productos desde MySQL: " + e.getMessage());
        }
    }

    private void agregarEventosBusqueda() {
        txtCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarClientePorNit();
                }
            }
        });

        txtCliente.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!txtCliente.getText().trim().isEmpty()) {
                    buscarClientePorNit();
                }
            }
        });

        txtProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarProductoPorId();
                }
            }
        });

        txtProducto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!txtProducto.getText().trim().isEmpty()) {
                    buscarProductoPorId();
                }
            }
        });
    }

    private void buscarClientePorNit() {
        String nit = txtCliente.getText().trim();
        if (nit.isEmpty()) return;

        try {
            Cliente c = clienteDAO.buscarPorNit(nit);
            if (c != null) {
                clienteSeleccionado = c;
                txtCliente1.setText(c.getNombre());
                txtDireccion.setText(c.getDireccion() != null ? c.getDireccion() : "");
            } else {
                clienteSeleccionado = null;
                txtCliente1.setText("");
                txtDireccion.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error al buscar cliente en MySQL: " + e.getMessage());
        }
    }

    private void buscarProductoPorId() {
        try {
            int id = Integer.parseInt(txtProducto.getText().trim());
            for (int i = 0; i < listaProductosBD.size(); i++) {
                Producto p = listaProductosBD.get(i);
                if (p.getIdProducto() == id) {
                    productoSeleccionado = p;
                    cbxProductos.setSelectedIndex(i + 1);
                    txtPrecio.setText(String.valueOf(p.getPrecio()));
                    txtCantidad.requestFocus();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            productoSeleccionado = null;
            txtPrecio.setText("");
        } catch (NumberFormatException e) {
            // Entrada inválida
        }
    }

    private void calcularTotal() {
        double total = 0.0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total += Double.parseDouble(modeloTabla.getValueAt(i, 4).toString());
        }
        lblTotal.setText(String.format("%.2f", total));
    }

    private void limpiarCamposProducto() {
        txtProducto.setText("");
        if (cbxProductos.getItemCount() > 0) {
            cbxProductos.setSelectedIndex(0);
        }
        txtCantidad.setText("");
        txtPrecio.setText("");
        productoSeleccionado = null;
        txtProducto.requestFocus();
    }

    private void limpiarFormularioCompleto() {
        cargarProductosEnCombo();
        generarSiguienteNumeroFactura();
        txtCliente.setText("");
        txtCliente1.setText("");
        txtDireccion.setText("");
        txtFecha.setText(LocalDate.now().toString());
        clienteSeleccionado = null;
        limpiarCamposProducto();
        modeloTabla.setRowCount(0);
        lblTotal.setText("0.00");
    }

    private void cbxProductosActionPerformed(java.awt.event.ActionEvent evt) {
        int idx = cbxProductos.getSelectedIndex();
        if (idx > 0 && (idx - 1) < listaProductosBD.size()) {
            productoSeleccionado = listaProductosBD.get(idx - 1);
            txtProducto.setText(String.valueOf(productoSeleccionado.getIdProducto()));
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
            txtCantidad.requestFocus();
        } else {
            productoSeleccionado = null;
            txtProducto.setText("");
            txtPrecio.setText("");
        }
    }

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione o busque un producto válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidadExistenteEnTabla = 0;
            int filaExistente = -1;

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int idInTable = Integer.parseInt(modeloTabla.getValueAt(i, 0).toString());
                if (idInTable == productoSeleccionado.getIdProducto()) {
                    cantidadExistenteEnTabla = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
                    filaExistente = i;
                    break;
                }
            }

            if ((cantidad + cantidadExistenteEnTabla) > productoSeleccionado.getExistencia()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente.\nDisponible: " + productoSeleccionado.getExistencia() 
                        + "\nYa en carrito: " + cantidadExistenteEnTabla, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (filaExistente >= 0) {
                int nuevaCantidad = cantidadExistenteEnTabla + cantidad;
                double nuevoSubtotal = nuevaCantidad * precio;
                modeloTabla.setValueAt(nuevaCantidad, filaExistente, 2);
                modeloTabla.setValueAt(precio, filaExistente, 3);
                modeloTabla.setValueAt(nuevoSubtotal, filaExistente, 4);
            } else {
                DetalleFactura detalle = new DetalleFactura(productoSeleccionado, cantidad, precio);
                modeloTabla.addRow(new Object[]{
                    productoSeleccionado.getIdProducto(),
                    productoSeleccionado.getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.calcularSubtotal()
                });
            }

            calcularTotal();
            limpiarCamposProducto();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos en Cantidad y Precio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int filaSeleccionada = tblDetalle.getSelectedRow();
        if (filaSeleccionada >= 0) {
            modeloTabla.removeRow(filaSeleccionada);
            calcularTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nit = txtCliente.getText().trim();
            String nombreCliente = txtCliente1.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (nit.isEmpty()) {
                nit = "C/F";
            }
            if (nombreCliente.isEmpty()) {
                nombreCliente = "Consumidor Final";
            }

            if (!nit.equalsIgnoreCase("C/F")) {
                if (clienteSeleccionado == null) {
                    clienteSeleccionado = clienteDAO.buscarPorNit(nit);
                }

                if (clienteSeleccionado == null) {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setNit(nit);
                    nuevoCliente.setNombre(nombreCliente);
                    nuevoCliente.setDireccion(direccion.isEmpty() ? "Ciudad" : direccion);

                    try {
                        clienteDAO.insertar(nuevoCliente);
                        clienteSeleccionado = clienteDAO.buscarPorNit(nit);
                    } catch (Exception ex) {
                        System.err.println("Aviso al insertar cliente: " + ex.getMessage());
                    }
                }
            }

            String estadoDespacho = "Pendiente";
            String tipoEntrega = "Venta en tienda";

            if (rbRecoger.isSelected()) {
                tipoEntrega = "Recoger en tienda";
                estadoDespacho = "Pendiente de Recoger";
            } else if (rbEnvio.isSelected()) {
                tipoEntrega = "Envío a domicilio";
                estadoDespacho = "Pendiente de Envío";
            } else if (rbVentaTienda.isSelected()) {
                tipoEntrega = "Venta en tienda";
                estadoDespacho = "Entregado";
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto al detalle.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idFactura = Integer.parseInt(txtNumeroFactura.getText().trim());
            String fechaActual = LocalDate.now().toString();

            Factura factura = new Factura(idFactura, fechaActual, clienteSeleccionado);
            factura.setNitCliente(nit);
            factura.setNombreCliente(nombreCliente);
            factura.setTipoEntrega(tipoEntrega);
            factura.setEstadoDespacho(estadoDespacho);

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int idProd = Integer.parseInt(modeloTabla.getValueAt(i, 0).toString());
                String prodNombre = modeloTabla.getValueAt(i, 1).toString();
                int cant = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
                double prec = Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());

                Producto p = new Producto(idProd, prodNombre, prec, cant);
                DetalleFactura det = new DetalleFactura(p, cant, prec);
                factura.agregarDetalle(det);
            }

            if (facturaDAO.guardar(factura)) {
                JOptionPane.showMessageDialog(this, "Factura guardada exitosamente en MySQL.\nCliente: " + nombreCliente 
                        + "\nNIT: " + nit + "\nTipo entrega: " + tipoEntrega 
                        + "\nEstado despacho: " + estadoDespacho 
                        + "\nTotal: Q" + String.format("%.2f", factura.calcularTotal()));
                limpiarFormularioCompleto();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la factura en la base de datos.", "Error DB", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al procesar la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}