/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.vista.FrmMenuPrincipal; // O el nombre de tu ventana principal/MDI


/**
 *
 * @author maorozco
 */
public class SistemaEstudiantes {

    public static void main(String[] args) {
//{
        // Ejecutamos la ventana principal del sistema (JFrame)
        FrmMenuPrincipal menuPrincipal = new FrmMenuPrincipal();
        menuPrincipal.setLocationRelativeTo(null); // Centra el JFrame principal en la pantalla
        menuPrincipal.setVisible(true);
    }
}