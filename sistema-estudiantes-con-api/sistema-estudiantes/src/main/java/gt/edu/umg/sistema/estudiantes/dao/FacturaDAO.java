package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import java.util.List;

public interface FacturaDAO {

    int obtenerSiguienteIdFactura();

    boolean insertar(Factura factura);

    boolean guardar(Factura factura);

    List<Factura> listar();
}