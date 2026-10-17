package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.modelo.Usuario;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Menú Principal del Sistema
 */
public class FrmMenuPrincipal extends JFrame {

    private static final Logger logger = Logger.getLogger(FrmMenuPrincipal.class.getName());

    // Variable para rastrear la ventana activa independiente de su tipo
    private Object ventanaActual = null;

    // Guardar la información del usuario en sesión
    private Usuario usuarioSesion;

    /**
     * Constructor por defecto
     */
    public FrmMenuPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null); // Centra la ventana
        this.setTitle("Sistema de Ventas - Menú Principal");
    }

    /**
     * Constructor principal que recibe el usuario con su rol
     */
    public FrmMenuPrincipal(Usuario usuario) {
        this();
        this.usuarioSesion = usuario;

        if (usuarioSesion != null) {
            this.setTitle("Sistema de Ventas - Usuario: " + usuarioSesion.getNombre() + " (" + usuarioSesion.getRol() + ")");
            aplicarPermisos();
        }
    }

    /**
     * Aplica la restricción de accesos evaluando las casillas/módulos autorizados en MySQL
     */
    private void aplicarPermisos() {
        if (usuarioSesion == null) return;

        // Si es ADMIN, tiene acceso libre a todos los módulos
        if ("ADMIN".equalsIgnoreCase(usuarioSesion.getRol())) {
            mniClientes.setEnabled(true);
            mniEmpleados.setEnabled(true);
            mniProductos.setEnabled(true);
            mniEstudiantes.setEnabled(true);
            mniUsuarios.setEnabled(true);
            mniFactura.setEnabled(true);
            mniDespacho.setEnabled(true);
            mniRastreo.setEnabled(true);
            return;
        }

        // Habilita o deshabilita según los permisos asignados en los CheckBoxes
        mniClientes.setEnabled(usuarioSesion.tienePermiso("Clientes"));
        mniEmpleados.setEnabled(usuarioSesion.tienePermiso("Empleados"));
        mniProductos.setEnabled(usuarioSesion.tienePermiso("Productos"));
        mniUsuarios.setEnabled(usuarioSesion.tienePermiso("Usuarios"));
        mniFactura.setEnabled(usuarioSesion.tienePermiso("Facturas"));

        // Módulos complementarios vinculados a sus dependencias correspondientes
        mniEstudiantes.setEnabled(usuarioSesion.tienePermiso("Empleados"));
        mniDespacho.setEnabled(usuarioSesion.tienePermiso("Facturas"));
        mniRastreo.setEnabled(usuarioSesion.tienePermiso("Facturas"));
    }

    /**
     * Método híbrido que abre tanto JInternalFrame como JFrame sin generar error de tipos
     */
    private void abrirVentanaExclusiva(Object nuevaVentana) {
        if (nuevaVentana == null) return;

        // Cierra la ventana anterior si estaba abierta
        if (ventanaActual != null) {
            if (ventanaActual instanceof JInternalFrame) {
                JInternalFrame iframe = (JInternalFrame) ventanaActual;
                if (!iframe.isClosed()) {
                    iframe.dispose();
                }
            } else if (ventanaActual instanceof JFrame) {
                JFrame frame = (JFrame) ventanaActual;
                if (frame.isDisplayable()) {
                    frame.dispose();
                }
            }
        }

        ventanaActual = nuevaVentana;

        // Si es un JInternalFrame (se agrega al desktopPane)
        if (nuevaVentana instanceof JInternalFrame) {
            JInternalFrame iframe = (JInternalFrame) nuevaVentana;
            desktopPane.add(iframe);
            iframe.setVisible(true);
            try {
                iframe.setMaximum(true);
                iframe.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } 
        // Si es un JFrame estándar (se muestra emergente y centrado)
        else if (nuevaVentana instanceof JFrame) {
            JFrame frame = (JFrame) nuevaVentana;
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        desktopPane = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mniClientes = new javax.swing.JMenuItem();
        mniEmpleados = new javax.swing.JMenuItem();
        mniProductos = new javax.swing.JMenuItem();
        mniEstudiantes = new javax.swing.JMenuItem();
        mniUsuarios = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mniFactura = new javax.swing.JMenuItem();
        mniDespacho = new javax.swing.JMenuItem();
        mniRastreo = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktopPane.setToolTipText("");

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        jMenu1.setText("Mantenimientos");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        mniClientes.setText("Clientes");
        mniClientes.addActionListener(this::mniClientesActionPerformed);
        jMenu1.add(mniClientes);

        mniEmpleados.setText("Empleados");
        mniEmpleados.addActionListener(this::mniEmpleadosActionPerformed);
        jMenu1.add(mniEmpleados);

        mniProductos.setText("Productos");
        mniProductos.addActionListener(this::mniProductosActionPerformed);
        jMenu1.add(mniProductos);

        mniEstudiantes.setText("Estudiantes");
        mniEstudiantes.addActionListener(this::mniEstudiantesActionPerformed);
        jMenu1.add(mniEstudiantes);

        mniUsuarios.setText("Usuarios");
        mniUsuarios.addActionListener(this::mniUsuariosActionPerformed);
        jMenu1.add(mniUsuarios);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ventas");

        mniFactura.setText("Factura");
        mniFactura.addActionListener(this::mniFacturaActionPerformed);
        jMenu2.add(mniFactura);

        mniDespacho.setText("Despacho de Producto");
        mniDespacho.addActionListener(this::mniDespachoActionPerformed);
        jMenu2.add(mniDespacho);

        mniRastreo.setText("Rastreo de Pedido");
        mniRastreo.addActionListener(this::mniRastreoActionPerformed);
        jMenu2.add(mniRastreo);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );

        pack();
    }// </editor-fold>                        

    private void mniClientesActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmCliente());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmEmpleado());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniProductosActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmProducto());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmEstudiante());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Estudiantes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniUsuariosActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmGestionUsuarios());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Gestión de Usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniFacturaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new FrmFactura());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Facturación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniDespachoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new Despachoproducto());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Despacho: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mniRastreoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirVentanaExclusiva(new RastreoPedido());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el formulario de Rastreo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Usuario usuarioPrueba = new Usuario("admin", "1234", "Carlos López", "ADMIN");
            new FrmMenuPrincipal(usuarioPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JMenuItem mniClientes;
    private javax.swing.JMenuItem mniDespacho;
    private javax.swing.JMenuItem mniEmpleados;
    private javax.swing.JMenuItem mniEstudiantes;
    private javax.swing.JMenuItem mniFactura;
    private javax.swing.JMenuItem mniProductos;
    private javax.swing.JMenuItem mniRastreo;
    private javax.swing.JMenuItem mniUsuarios;
    // End of variables declaration                    
}