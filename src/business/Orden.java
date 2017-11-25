package business;

import data.OrdenDetalle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orden {

    private Orden() {

    }

    // Sin verificar
    public static String insertar(int numero, int suplidorId, LocalDate fecha, LocalDate fechaEnviada, /*double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto,*/ char estatus, ResultSet rs) {
        data.Orden orden = new data.Orden(numero, suplidorId, fecha, fechaEnviada,
                /*totalBruto, totalDescuento, totalImpuesto, totalCargo, totalNeto,*/ estatus);

        ArrayList<OrdenDetalle> detalles = new ArrayList<>();

        try {
            if (rs != null) {
                while (rs.next()) {
                    detalles.add(new OrdenDetalle(rs.getInt("ProductoId"), rs.getDouble("OrdenDetallePrecio"),
                            rs.getInt("OrdenDetalleCantidad"), rs.getDouble("OrdenDetalleDescuento"),
                            rs.getDouble("OrdenDetalleImpuesto"), rs.getDouble("OrdenDetalleNeto"))
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

/*    public static ResultSet buscarNumero(int numero, int posicion) {
        data.Suplidor suplidor = new data.Suplidor(numero);
        return new data.Orden().buscar(suplidor);
    }*/

    @Deprecated
    public static String actualizar(int id, String empresa, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, char estatus) {
        data.Suplidor suplidor = new data.Suplidor(id, empresa, direccion, ciudad, email,
                telefono, codigoPostal, pais, estatus);
        return new data.Suplidor().actualizar(suplidor);
    }

}
