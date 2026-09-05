package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements FacturaDAO {
    private static List<Factura> listaFacturas = new ArrayList<>();

    @Override
    public boolean guardar(Factura factura) {
        return listaFacturas.add(factura);
    }

    @Override
    public List<Factura> listar() {
        return listaFacturas;
    }
}