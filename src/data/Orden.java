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
    public Orden(int numero, int suplidorId, LocalDate fecha, LocalDate fechaEnviada,/* double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto,*/ char estatus) {
        numero_ = numero;
        suplidorId_ = suplidorId;
        fecha_ = fecha;
        fechaEnviada_ = fechaEnviada;
        estatus_ = estatus;
        totalBruto_ = 0.00;
        totalDescuento_ = 0.00;
        totalImpuesto_ = 0.00;
        totalCargo_ = 0.00;
        totalNeto_ = 0.00;
    }

    // Para actualizar
    @Deprecated
    public Orden(int id, int numero, int suplidorId, LocalDate fecha, LocalDate fechaEnviada, double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto, char estatus) {
        this(numero, suplidorId, fecha, fechaEnviada, estatus);
        id_ = id;
    }

    // Para eliminar.
    public Orden(int id) {
        id_ = id;
    }

    // Listo. 
    public String insertar(Orden orden, ArrayList<OrdenDetalle> detalles) {
        String mensaje = "";
        try (Connection conn = Conexion.conectar()) {
            // Deshabilita auto transaccion.
            conn.setAutoCommit(false);

            CallableStatement query = conn.prepareCall("{call SP_InsertarOrden(?, ?, ?, ?, ?)}");
            query.registerOutParameter("id", JDBCType.INTEGER);
            query.setInt("suplidorId", orden.suplidorId_);
            query.setInt("numero", orden.numero_);
            query.setString("estatus", String.valueOf(orden.estatus_));
            query.setObject("fecha", orden.fecha_);
            query.setObject("fechaEnviada", orden.fechaEnviada_);

            boolean esEjecutado = (query.executeUpdate() > 0);
            if (esEjecutado) {
                orden.id_ = query.getInt(1);

                boolean esConexionNula = false;
                for (OrdenDetalle detalle : detalles) {
                    detalle.setIdOrden(orden.id_);
                    Connection conectado = detalle.insertar(null, conn);
                    esConexionNula = (conectado == null);
                    if (esConexionNula) {
                        conn.rollback();
                        break;
                    }
                }
                if (!esConexionNula) {
                    CallableStatement calcula = conn.prepareCall("{call SP_CalcularTotalOrden(?)}");
                    calcula.setInt("id", orden.id_);
                    esEjecutado = (calcula.executeUpdate() > 0);
                    if (esEjecutado) {
                        mensaje = "El registro ha sido agregado exitosamente.";
                        conn.commit();
                    }
                }
            }
            // Si todas las consultas no son ejecutadas.
            if (!esEjecutado){
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

            boolean esEjecutado = (query.executeUpdate() > 0);
            if (esEjecutado) {
                mensaje = "La orden ha sido cancelada exitosamente.";
            } else {
                mensaje = "La orden no pudo ser cancelada.";
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
            CallableStatement query = conn.prepareCall("{call SP_MostrarOrdenDetalle(?)}");
            query.setInt("numero", numero);
            rs = query.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
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
