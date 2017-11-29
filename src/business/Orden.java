package business;

import javafx.collections.ObservableList;

import java.util.HashMap;

public class Orden {

    private Orden() {

    }

    // Terminado.
    public static String insertar(int numero, data.Suplidor suplidor, ObservableList<data.Producto> productos,
            HashMap<data.Producto, Integer> cantidad) {
        data.Orden orden = new data.Orden(numero, suplidor);
        return new data.Orden().insertar(orden, productos, cantidad);
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