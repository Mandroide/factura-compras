package business;

import data.OrdenDetalle;
import javafx.collections.ObservableList;

public class Orden {

    private Orden() {

    }

    // Terminado.
    public static String insertar(data.Suplidor suplidor, ObservableList<OrdenDetalle> detalles) {
        data.Orden orden = new data.Orden(suplidor);
        return new data.Orden().insertar(orden, detalles);
    }

    // Listo.
    public static String eliminar(int id) {
        data.Orden orden = new data.Orden(id);
        return new data.Orden().eliminar(orden);
    }

    // Listo.
    public static ObservableList<data.Orden> mostrar() {
        return new data.Orden().mostrar();
    }

    // Listo.
    public static ObservableList<data.OrdenDetalle> mostrarDetalles(int numero) {
        data.Orden orden = new data.Orden();
        return orden.mostrarDetalles(numero);
    }

}