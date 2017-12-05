package business;

import javafx.collections.ObservableList;

public class Suplidor {

    private Suplidor(){
        
    }
    
    public static String insertar(String nombre, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais) {
        data.Suplidor suplidor = new data.Suplidor(nombre, direccion, ciudad, email,
            telefono, codigoPostal, pais);

        return new data.Suplidor().insertar(suplidor);
    }

    public static String actualizar(int id, String nombre, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, String estatus) {
        data.Suplidor suplidor = new data.Suplidor(id, nombre, direccion, ciudad, email,
            telefono, codigoPostal, pais, estatus);
        return new data.Suplidor().actualizar(suplidor);
    }

    public static String eliminar(int id) {
        data.Suplidor suplidor = new data.Suplidor(id);
        return new data.Suplidor().eliminar(suplidor);
    }

    public static ObservableList<data.Suplidor> buscar(String textoABuscar) {
        data.Suplidor suplidor = new data.Suplidor(textoABuscar);
        return new data.Suplidor().buscar(suplidor);
    }

    public static ObservableList<data.Suplidor> buscarActivos(String textoABuscar) {
        data.Suplidor suplidor = new data.Suplidor(textoABuscar);
        return new data.Suplidor().buscarActivos(suplidor);
    }

    public static ObservableList<data.Suplidor> mostrar() {
        return new data.Suplidor().mostrar();
    }

    public static ObservableList<data.Suplidor> mostrarActivos() {
        return new data.Suplidor().mostrarActivos();
    }

}
