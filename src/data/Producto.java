package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producto {

    private int id_;
    private String nombre_;
    private String descripcion_;
    private BigDecimal precio_;
    private int unidadesStock_;
    private String codigo_;
    private Estatus estatus_;

    private String textoABuscar_;

    // Para realizar consultas a la base de datos.
    public Producto() {

    }

    // Para actualizar.
    public Producto(int id, String codigo, String nombre, String descripcion,
                    BigDecimal precio, int unidadesStock, Estatus estatus) {
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
            try (var query = conn.prepareStatement("INSERT INTO Producto(ProductoCodigo, ProductoNombre, "
                    + "ProductoDescripcion, ProductoPrecio, ProductoUnidadesStock) VALUES (?, ?, ?, ?, ?);")) {
                query.setString(1, producto.codigo_);
                query.setString(2, producto.nombre_);
                query.setString(3, producto.descripcion_);
                query.setBigDecimal(4, producto.precio_);
                query.setInt(5, producto.unidadesStock_);

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
            try (var query = conn.prepareStatement("UPDATE Producto SET  ProductoCodigo = ?, " +
                    "ProductoNombre = ?, ProductoDescripcion = ?, ProductoPrecio = ?, ProductoUnidadesStock = ?,"
                    + "ProductoEstatus = ? WHERE ProductoId = ?;")) {
                query.setString(1, producto.codigo_);
                query.setString(2, producto.nombre_);
                query.setString(3, producto.descripcion_);
                query.setBigDecimal(4, producto.precio_);
                query.setInt(5, producto.unidadesStock_);
                query.setString(6, producto.getEstatus().getChar());
                query.setInt(7, producto.id_); // Modificar

                boolean esEjecutado = (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "El registro ha sido actualizado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser actualizado correctamente.");
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
            try (var query = conn.prepareStatement("UPDATE Producto SET ProductoEstatus = 'I'"
                    + "WHERE ProductoId = ? ;")) {
                query.setInt(1, producto.id_);

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

    public ObservableList<Producto> buscar(Producto producto) {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            var query = conn.prepareStatement("SELECT * from producto_buscar(?)");
            query.setString(1, producto.textoABuscar_);
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public ObservableList<Producto> mostrar() {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            var query = conn.prepareStatement("SELECT * from producto_mostrar()");
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public ObservableList<Producto> mostrarActivos() {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            var query = conn.prepareStatement("SELECT * from producto_mostraractivos()");
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private ObservableList<Producto> leer(ResultSet resultSet) throws SQLException {
        ObservableList<Producto> data = FXCollections.observableArrayList();
        while (resultSet.next()) {
            data.add(crear(resultSet));
        }
        return data;
    }

    private Producto crear(ResultSet resultSet) throws SQLException {
        int no = resultSet.getInt("Id");
        final String nombre = resultSet.getString("Nombre");
        final String descripcion = resultSet.getString("Descripcion");
        final BigDecimal precio = resultSet.getBigDecimal("Precio");
        final int unidadesStock = resultSet.getInt("Unidades_Stock");
        final String codigo = resultSet.getString("Codigo");
        Producto producto = new Producto(codigo, nombre, descripcion, precio, unidadesStock);
        producto.setId(no);

        // Set Estatus.
        HashMap<String, Estatus> opciones = new HashMap<>();
        opciones.put("A", Estatus.ACTIVO);
        opciones.put("I", Estatus.INACTIVO);
        final Estatus estatus = opciones.get(resultSet.getString("Estatus"));
        producto.setEstatus(estatus);

        return producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo_, producto.codigo_);
    }

    @Override
    public int hashCode() {

        return Objects.hash(codigo_);
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

    public Estatus getEstatus() {
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

    private void setUnidadesStock(int unidadesStock) {
        unidadesStock_ = unidadesStock;
    }

    public void setCodigo(String codigo) {
        codigo_ = codigo;
    }

    public void setEstatus(Estatus estatus) {
        estatus_ = estatus;
    }

    private void setTextoABuscar(String textoABuscar) {
        textoABuscar_ = textoABuscar;
    }
}
