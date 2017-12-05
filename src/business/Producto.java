package business;

import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class Producto {

    private Producto() {

    }

    public static String insertar(String codigo, String nombre, String descripcion, BigDecimal precio,
                                  int unidadesStock) {
        data.Producto producto = new data.Producto(codigo, nombre, descripcion, precio, unidadesStock);
        return producto.insertar(producto);
    }

    public static String actualizar(int id, String codigo, String nombre, String descripcion, BigDecimal precio,
                                    int unidadesStock, String estatus) {
        data.Producto producto = new data.Producto(id, codigo, nombre, descripcion, precio, unidadesStock, estatus);
        return producto.actualizar(producto);
    }

    public static String eliminar(int id) {
        data.Producto producto = new data.Producto(id);
        return producto.eliminar(producto);
    }

    public static ObservableList<data.Producto> buscar(String textoABuscar) {
        data.Producto producto = new data.Producto(textoABuscar);
        return producto.buscar(producto);
    }

    public static ObservableList<data.Producto> mostrar() {
        return new data.Producto().mostrar();
    }

    public static ObservableList<data.Producto> mostrarActivos() {
        return new data.Producto().mostrarActivos();
    }

}
