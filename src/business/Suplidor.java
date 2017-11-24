package business;

import java.sql.ResultSet;

public class Suplidor {

    private Suplidor(){
        
    }
    
    public static String insertar(String empresa, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, char estatus) {
        data.Suplidor suplidor = new data.Suplidor(empresa, direccion, ciudad, email,
            telefono, codigoPostal, pais, estatus);

        return new data.Suplidor().insertar(suplidor);
    }

    public static String actualizar(int id, String empresa, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, char estatus) {
        data.Suplidor suplidor = new data.Suplidor(id, empresa, direccion, ciudad, email,
            telefono, codigoPostal, pais, estatus);
        return new data.Suplidor().actualizar(suplidor);
    }

    public static String eliminar(int id) {
        data.Suplidor suplidor = new data.Suplidor(id);
        return new data.Suplidor().eliminar(suplidor);
    }

    public static ResultSet buscar(String textoABuscar) {
        data.Suplidor suplidor = new data.Suplidor(textoABuscar);
        return new data.Suplidor().buscar(suplidor);
    }

    public static ResultSet mostrar() {
        return new data.Suplidor().mostrar();
    }

}
