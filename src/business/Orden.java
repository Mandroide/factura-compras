package business;

import data.OrdenDetalle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orden {

    private Orden() {

    }

    // Sin verificar
    public static String insertar(int numero, int suplidorId, String estatus, ResultSet rs) {
        data.Orden orden = new data.Orden(numero, suplidorId,  estatus);

        ArrayList<OrdenDetalle> detalles = new ArrayList<>();

        try {
            if (rs != null) {
                while (rs.next()) {
                    detalles.add(new OrdenDetalle(rs.getInt("ProductoId"), rs.getBigDecimal("OrdenDetallePrecio"),
                            rs.getInt("OrdenDetalleCantidad"), rs.getBigDecimal("OrdenDetalleDescuento"),
                            rs.getBigDecimal("OrdenDetalleImpuesto"), rs.getBigDecimal("OrdenDetalleNeto"))
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new data.Orden().insertar(orden, detalles);
    }

    // Listo.
    public static String eliminar(int id) {
        data.Orden orden = new data.Orden(id);
        return new data.Orden().eliminar(orden);
    }

    // Listo.
    public static ResultSet mostrar() {
        return new data.Orden().mostrar();
    }

    // Listo.
    public static ResultSet mostrarDetalles(int numero) {
        data.Orden orden = new data.Orden();
        return orden.mostrarDetalles(numero);
    }

}


