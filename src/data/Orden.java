package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orden {

    private int id_;
    private int numero_;
    private int suplidorId_;
    private LocalDateTime fecha_;
    private double totalBruto_;
    private double totalDescuento_;
    private double totalImpuesto_;
    private double totalCargo_;
    private double totalNeto_;
    private String estatus_;

    // Para realizar consultas a la base de datos.
    public Orden() {

    }

    // Para insertar
    public Orden(int numero, int suplidorId, /*LocalDateTime fecha, double totalBruto,
            double totalDescuento, double totalImpuesto, double totalCargo, double totalNeto,*/ String estatus) {
        numero_ = numero;
        suplidorId_ = suplidorId;
        estatus_ = estatus;
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

            CallableStatement query = conn.prepareCall("{call Orden_Insertar(?, ?, ?, ?)}");
            query.registerOutParameter("id", JDBCType.INTEGER);
            query.setInt("suplidorId", orden.suplidorId_);
            query.setInt("numero", orden.numero_);
            query.setString("estatus", String.valueOf(orden.estatus_));

            boolean esEjecutado = (query.executeUpdate() > 0);
            if (esEjecutado) {
                orden.id_ = query.getInt("id");

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
                    CallableStatement calcula = conn.prepareCall("{call Orden_CalcularTotal(?)}");
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

    public String eliminar(Orden orden) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Orden_Eliminar(?)}");
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
            CallableStatement query = conn.prepareCall("{call Orden_Mostrar}");
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

    public LocalDateTime getFecha() {
        return fecha_;
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
