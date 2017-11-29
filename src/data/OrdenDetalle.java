package data;

import java.math.BigDecimal;
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
    private BigDecimal precio_;
    private int cantidad_;
    private BigDecimal descuento_;
    private BigDecimal impuesto_;
    private BigDecimal neto_;

    // Para realizar consultas a la base de datos.
    public OrdenDetalle() {

    }

    // Para eliminar
    public OrdenDetalle(short linea) {
        linea_ = linea;
    }

    // Para insertar.
    public OrdenDetalle(int idProducto, BigDecimal precio, int cantidad,
            BigDecimal descuento, BigDecimal impuesto, BigDecimal neto) {
        idProducto_ = idProducto;
        precio_ = precio;
        cantidad_ = cantidad;
        descuento_ = descuento;
        impuesto_ = impuesto;
        neto_ = neto;
    }

    // Para actualizar
    public OrdenDetalle(short linea, int idOrden, int idProducto, BigDecimal precio, int cantidad,
            BigDecimal descuento, BigDecimal impuesto, BigDecimal neto) {
        this(idProducto, precio, cantidad, descuento, impuesto, neto); // Constructor de insercion.
        linea_ = linea;
    }

    // Listo.
    public Connection insertar(OrdenDetalle detalle, Connection conn) {
        Connection conexion = null;
        CallableStatement query;
        try {
            query = conn.prepareCall("{call OrdenDetalle_Insertar( ?, ?, ?, ?, ?, ?, ?)}");
            query.setInt("id", detalle.idOrden_);
            query.setInt("productoId", detalle.idProducto_);
            query.setBigDecimal("precio", detalle.precio_);
            query.setInt("cantidad", detalle.cantidad_);
            query.setBigDecimal("neto", detalle.neto_);
            query.setBigDecimal("impuesto", detalle.impuesto_);
            query.setBigDecimal("descuento", detalle.descuento_);

            int tuplas = query.executeUpdate();
            if (tuplas > 0) {
                conexion = conn;
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

    public BigDecimal getPrecio() {
        return precio_;
    }

    public int getCantidad() {
        return cantidad_;
    }

    public BigDecimal getDescuento() {
        return descuento_;
    }

    public BigDecimal getImpuesto() {
        return impuesto_;
    }

    public BigDecimal getNeto() {
        return neto_;
    }

}
