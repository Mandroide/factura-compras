package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orden {

    private int id_;
    private int numero_;
    private int suplidorId_;
    private LocalDate fecha_;
    private LocalDate fechaEnviada_;
    private double totalBruto_;
    private double totalDescuento_;
    private double totalImpuesto_;
    private double totalCargo_;
    private double totalNeto_;
    private char estatus_;

    // Para realizar consultas a la base de datos.
    public Orden() {

    }

    // Para insertar
    public Orden(int numero, int suplidorId, LocalDate fecha, LocalDate fechaEnviada, double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto, char estatus) {
        numero_ = numero;
        suplidorId_ = suplidorId;
        fecha_ = fecha;
        fechaEnviada_ = fechaEnviada;
        totalBruto_ = totalBruto;
        totalDescuento_ = totalDescuento;
        totalImpuesto_ = totalImpuesto;
        totalCargo_ = totalCargo;
        totalNeto_ = totalNeto;
    }

    // Para actualizar
    public Orden(int id, int numero, int suplidorId, LocalDate fecha, LocalDate fechaEnviada, double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto, char estatus) {
        this(numero, suplidorId, fecha, fechaEnviada, totalBruto, totalDescuento, totalImpuesto, totalCargo, totalNeto, estatus);
        id_ = id;
    }

    // Para eliminar.
    public Orden(int id) {
        id_ = id;
    }

    // Listo. 
    public String insertar(Orden orden, ArrayList<OrdenDetalle> detalles) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            // Deshabilita auto transaccion.
            conn.setAutoCommit(false);

            CallableStatement query = conn.prepareCall("{call SP_InsertarOrden(?, ?, ?, ?, ?, ?, ?)}");
            query.registerOutParameter(1, JDBCType.INTEGER);
            query.setInt("suplidorId", orden.suplidorId_);
            query.setInt("numero", orden.numero_);
            query.setString("estatus", String.valueOf(orden.estatus_));
            query.setObject("fecha", orden.fecha_);
            query.setObject("fechaEnviada", orden.fechaEnviada_);

            // Calculos en proceso.
            query.setDouble("totalBruto", orden.totalBruto_);
            query.setDouble("totalDescto", orden.totalDescuento_);
            query.setDouble("totalImpuesto", orden.totalImpuesto_);
            query.setDouble("totalCargo", orden.totalCargo_);
            query.setDouble("totalNeto", totalNeto_);

            int tuplas = query.executeUpdate();
            if (tuplas > 0) {
                mensaje = "El registro ha sido agregado exitosamente.";
                orden.id_ = query.getInt(1);

                boolean esNula = false;
                for (OrdenDetalle detalle : detalles) {
                    detalle.setIdOrden(orden.id_);
                    Connection conectado = detalle.insertar(null, conn);
                    esNula = (conectado == null);
                    if (esNula) {
                        conn.rollback();
                        break;
                    }
                }
                if (!esNula) {
                    conn.commit();
                }

            } else {
                conn.rollback();
                mensaje = "El registro no pudo ser agregado correctamente.";
            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;
    }

    // Listo
    public String eliminar(Orden orden) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call SP_EliminarOrden(?)}");
            query.setInt("id", orden.id_);

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

    // Listo
    public ResultSet mostrar() {
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_MostrarOrden()}");
            rs = query.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        }

        return rs;
    }

    // Listo
    public ResultSet mostrarDetalles(int numero) {
                                // Texto
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call SP_BuscarSuplidor(?)}");
            query.setInt("numero", numero);
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

    public int getNumero() {
        return numero_;
    }

    public int getSuplidorId() {
        return suplidorId_;
    }

    public LocalDate getFecha() {
        return fecha_;
    }

    public LocalDate getFechaEnviada() {
        return fechaEnviada_;
    }

    public double getTotalBruto() {
        return totalBruto_;
    }

    public double getTotalDescuento() {
        return totalDescuento_;
    }

    public double getTotalImpuesto() {
        return totalImpuesto_;
    }

    public double getTotalCargo() {
        return totalCargo_;
    }

    public double getTotalNeto() {
        return totalNeto_;
    }

}
