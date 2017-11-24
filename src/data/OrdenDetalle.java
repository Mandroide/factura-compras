package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdenDetalle {

    private short linea_;
    private int idOrden_;   // Orden
    private int idProducto_;    // Producto
    private double precio_;
    private int cantidad_;
    private double descuento_;
    private double impuesto_;
    private double neto_;

    // Para realizar consultas a la base de datos.
    public OrdenDetalle() {

    }

    // Para eliminar
    public OrdenDetalle(short linea) {
        linea_ = linea;
    }

    // Para insertar.
    public OrdenDetalle(int idProducto, double precio, int cantidad,
            double descuento, double impuesto, double neto) {
        idProducto_ = idProducto;
        precio_ = precio;
        cantidad_ = cantidad;
        descuento_ = descuento;
        impuesto_ = impuesto;
        neto_ = neto;
    }

    // Para actualizar
    public OrdenDetalle(short linea, int idOrden, int idProducto, double precio, int cantidad,
            double descuento, double impuesto, double neto) {
        this(idProducto, precio, cantidad, descuento, impuesto, neto); // Constructor de insercion.
        linea_ = linea;
    }

    // Listo.
    public Connection insertar(OrdenDetalle detalle, Connection conn) {
        Connection conexion = null;
        CallableStatement query;
        try {
            query = conn.prepareCall("{call SP_InsertarOrdenDetalle(?, ?, ?, ?, ?, ?, ?, ?)}");
            query.setInt("id", detalle.idOrden_);
            query.registerOutParameter("linea", JDBCType.SMALLINT);
            query.setInt("productoId", detalle.idProducto_);
            query.setDouble("precio", detalle.precio_);
            query.setInt("cantidad", detalle.cantidad_);
            query.setDouble("neto", detalle.neto_);
            query.setDouble("impuesto", detalle.impuesto_);
            query.setDouble("descuento", detalle.descuento_);

            int tuplas = query.executeUpdate();
            if (tuplas > 0) {
                conexion = conn;
                detalle.linea_ = query.getShort("linea");
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrdenDetalle.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conexion;
    }

    public int getLinea() {
        return linea_;
    }

    public int getIdOrden() {
        return idOrden_;
    }

    public void setIdOrden(int idOrden) {
        idOrden_ = idOrden;
    }

    public int getIdProducto() {
        return idProducto_;
    }

    public double getPrecio() {
        return precio_;
    }

    public int getCantidad() {
        return cantidad_;
    }

    public double getDescuento() {
        return descuento_;
    }

    public double getImpuesto() {
        return impuesto_;
    }

    public double getNeto() {
        return neto_;
    }

}
