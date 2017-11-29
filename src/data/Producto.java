package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producto {

    private int id_;
    private String nombre_;
    private String descripcion_;
    private BigDecimal precio_;
    private int unidadesStock_;
    private String codigo_;
    private String estatus_;

    private String textoABuscar_;

    // Para realizar consultas a la base de datos.
    public Producto() {

    }

    // Para actualizar.
    public Producto(int id, String codigo, String nombre, String descripcion,
                    BigDecimal precio, int unidadesStock, String estatus) {
        this(codigo, nombre, descripcion, precio, unidadesStock);
        setEstatus(estatus);
        setId(id);
    }

    // Para insertar
    public Producto(String codigo, String nombre, String descripcion,
                    BigDecimal precio, int unidadesStock) {
        setCodigo(codigo);
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setUnidadesStock(unidadesStock);
    }

    // Para buscar
    public Producto(String textoABuscar) {
        setTextoABuscar(textoABuscar);
    }

    // Para eliminar
    public Producto(int id){
        setId(id);
    }

    public String insertar(Producto producto) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call Producto_insertar(?, ?, ?, ?, ?)}")) {
                query.setString("codigo", producto.codigo_);
                query.setString("nombre", producto.nombre_);
                query.setString("descripcion", producto.descripcion_);
                query.setBigDecimal("precio", producto.precio_);
                query.setInt("unidadesStock", producto.unidadesStock_);

                boolean esEjecutado =  (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "El registro ha sido agregado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser agregado correctamente.");
                }
            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
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
            try (CallableStatement query = conn.prepareCall("{call Producto_Actualizar(?, ?, ?, ?, ?, ?, ?)}")) {
                query.setInt("id", producto.id_); // Modificar
                query.setString("codigo", producto.codigo_);
                query.setString("nombre", producto.nombre_);
                query.setString("descripcion", producto.descripcion_);
                query.setBigDecimal("precio", producto.precio_);
                query.setInt("unidadesStock", producto.unidadesStock_);
                query.setString("estatus", String.valueOf(producto.estatus_));

                boolean esEjecutado = (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "El registro ha sido eliminado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser eliminado correctamente.");
                }
            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);

            }
        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    public String eliminar(Producto producto) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call Producto_Eliminar(?)}")) {
                query.setInt("id", producto.id_);

                boolean esEjecutado  = (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "El registro ha sido eliminado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser eliminado correctamente.");
                }
            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    private ObservableList<Producto> leer(ResultSet resultSet) throws SQLException {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int no = resultSet.getInt("ProductoId");
            final String nombre = resultSet.getString("ProductoNombre");
            final String descripcion = resultSet.getString("ProductoDescripcion");
            final BigDecimal precio = resultSet.getBigDecimal("ProductoPrecio");
            final int unidadesStock = resultSet.getInt("ProductoUnidadesStock");
            final String estatus = resultSet.getString("ProductoEstatus");
            final String codigo = resultSet.getString("ProductoCodigo");
            Producto obj = new Producto(no, codigo, nombre, descripcion, precio, unidadesStock, estatus);
            data.add(obj);
        }
        return data;
    }

    public ObservableList<Producto> buscar(Producto producto) {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Producto_Buscar(?)}");
            query.setString("nombre", producto.textoABuscar_);
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public ObservableList<Producto> mostrar() {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Producto_Mostrar}");
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
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

    public BigDecimal getPrecio() {
        return precio_;
    }

    public int getUnidadesStock() {
        return unidadesStock_;
    }

    public String getCodigo() {
        return codigo_;
    }

    public String getEstatus() {
        return estatus_;
    }

    public void setId(int id) {
        id_ = id;
    }

    public void setNombre(String nombre) {
        nombre_ = nombre;
    }

    public void setDescripcion(String descripcion) {
        descripcion_ = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        precio_ = precio;
    }

    public void setUnidadesStock(int unidadesStock) {
        unidadesStock_ = unidadesStock;
    }

    public void setCodigo(String codigo) {
        codigo_ = codigo;
    }

    public void setEstatus(String estatus) {
        estatus_ = estatus;
    }

    public void setTextoABuscar(String textoABuscar) {
        textoABuscar_ = textoABuscar;
    }
}
