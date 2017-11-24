package business;

import java.sql.ResultSet;

public class Producto {

    private Producto() {

    }

    public static String insertar(String nombre, String descripcion, double precio,
            int unidadesStock, String tipoCodigo, char estatus) {
        data.Producto producto = new data.Producto(nombre, descripcion, precio, unidadesStock, tipoCodigo, estatus);

        return new data.Producto().insertar(producto);
    }

    public static String actualizar(int id, String nombre, String descripcion, double precio,
            int unidadesStock, String tipoCodigo, char estatus) {
        data.Producto producto = new data.Producto(id, nombre, descripcion, precio, unidadesStock, tipoCodigo, estatus);
        return new data.Producto().actualizar(producto);
    }

    public static String eliminar(int id) {
        data.Producto producto = new data.Producto(id);
        return new data.Producto().eliminar(producto);
    }

    public static ResultSet buscar(String textoABuscar) {
        data.Producto producto = new data.Producto(textoABuscar);
        return new data.Producto().buscar(producto);
    }

    public static ResultSet mostrar() {
        return new data.Producto().mostrar();
    }

}
