package data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producto {

    private int id_;
    private String nombre_;
    private String descripcion_;
    private double precio_;
    private int unidadesStock_;
    private String tipoCodigo_;
    private char estatus_;

    private String textoABuscar_;

    // Para realizar consultas a la base de datos.
    public Producto() {

    }

    // Para actualizar.
    public Producto(int id, String nombre, String descripcion,
            double precio, int unidadesStock, String tipoCodigo, char estatus) {
        this(nombre, descripcion, precio, unidadesStock, tipoCodigo, estatus);
        id_ = id;
    }

    // Para insertar
    public Producto(String nombre, String descripcion,
            double precio, int unidadesStock, String tipoCodigo, char estatus) {
        nombre_ = nombre;
        descripcion_ = descripcion;
        precio_ = precio;
        unidadesStock_ = unidadesStock;
        tipoCodigo_ = tipoCodigo;
        estatus_ = estatus;
        id_ = 0;
    }

    // Para buscar
    public Producto(String textoABuscar) {
        textoABuscar_ = textoABuscar;
    }
    // Para eliminar
    public Producto(int id){
        id_ = id;
    }
    
    public String insertar(Producto producto) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_insertarProducto(?, ?, ?, ?, ?, ?, ?)}");
            query.registerOutParameter(1, JDBCType.INTEGER);
            query.setString(2, producto.nombre_);
            query.setString(3, producto.descripcion_);
            query.setDouble(4, producto.precio_);
            query.setInt(5, producto.unidadesStock_);
            query.setString(6, producto.tipoCodigo_);
            query.setString(7, String.valueOf(producto.estatus_));

            int tuplas = query.executeUpdate();
            if (tuplas > 0) {
                mensaje = "El registro ha sido agregado exitosamente.";
                producto.id_ = query.getInt(1);
            } else {
                mensaje = "El registro no pudo ser agregado correctamente.";
            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;
    }

    public String actualizar(Producto producto) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_ActualizarProducto(?, ?, ?, ?, ?, ?, ?)}");
            query.setInt(1, producto.id_); // Modificar
            query.setString(2, producto.nombre_);
            query.setString(3, producto.descripcion_);
            query.setDouble(4, producto.precio_);
            query.setInt(5, producto.unidadesStock_);
            query.setString(6, producto.tipoCodigo_);
            query.setString(7, String.valueOf(producto.estatus_));

            int tupla = query.executeUpdate();
            if (tupla > 0) {
                mensaje = "El registro ha sido eliminado exitosamente.";
            } else {
                mensaje = "El registro no pudo ser eliminado correctamente.";
            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    public String eliminar(Producto producto) {
        String mensaje = "";
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call SP_EliminarProducto(?)}");
            query.setInt(1, producto.id_);

            int tupla = query.executeUpdate();
            if (tupla > 0) {
                mensaje = "El registro ha sido eliminado exitosamente.";
            } else {
                mensaje = "El registro no pudo ser eliminado correctamente.";
            }
        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    public ResultSet buscar(Producto producto) {
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_BuscarProducto(?)}");
            query.setString(1, producto.textoABuscar_);
            rs = query.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        }

        return rs;
    }

    public ResultSet mostrar() {
        String mensaje = "";
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_MostrarProducto()}");
            rs = query.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        }

        return rs;
    }

    public int getId() {
        return id_;
    }

    public String getNombre() {
        return nombre_;
    }

    public String getDescripcion() {
        return descripcion_;
    }

    public double getPrecio() {
        return precio_;
    }

    public int getUnidadesStock() {
        return unidadesStock_;
    }

    public String getTipoCodigo() {
        return tipoCodigo_;
    }

    public char getStatus() {
        return estatus_;
    }

}
