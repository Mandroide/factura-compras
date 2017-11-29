package business;

import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class Producto {

    private Producto() {

    }

    public static String insertar(String nombre, String descripcion, BigDecimal precio,
                                  int unidadesStock, String codigo) {
        data.Producto producto = new data.Producto(nombre, descripcion, precio, unidadesStock, codigo);
        return producto.insertar(producto);
    }

    public static String actualizar(int id, String nombre, String descripcion, BigDecimal precio,
                                    int unidadesStock, String codigo, String estatus) {
        data.Producto producto = new data.Producto(id, nombre, descripcion, precio, unidadesStock, codigo, estatus);
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

}
