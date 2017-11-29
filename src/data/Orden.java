package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.LocalTime;

public class Orden {

    private int id_;
    private int numero_;
    private Suplidor suplidor_ = new Suplidor();
    private LocalDate fecha_;
    private LocalTime hora_;
    private BigDecimal totalBruto_;
    private BigDecimal totalDescuento_;
    private BigDecimal totalImpuesto_;
    private BigDecimal totalCargo_;
    private BigDecimal totalNeto_;
    private String estatus_;
    private ObservableList<OrdenDetalle> detalles_ = FXCollections.observableArrayList();


    // Para realizar consultas a la base de datos.
    public Orden() {

    }

    // Para insertar
    public Orden(int numero, Suplidor suplidor) {
        setNumero(numero);
        suplidor_ = suplidor;
    }

    // Para Mostrar
    private Orden(Suplidor suplidor, int id, int numero, LocalDate fecha, LocalTime hora,
                  BigDecimal totalBruto, BigDecimal totalDescuento, BigDecimal totalImpuesto, BigDecimal totalCargo,
                  BigDecimal totalNeto){
        suplidor_ = suplidor;
        setId(id);
        setNumero(numero);
        setFecha(fecha);
        setHora(hora);
        setTotalBruto(totalBruto);
        setTotalDescuento(totalDescuento);
        setTotalImpuesto(totalImpuesto);
        setTotalCargo(totalCargo);
        setTotalNeto(totalNeto);

    }


    // Para eliminar.
    public Orden(int id) {
        setId(id);
    }

    private void insertarDetalles(Connection conn, Orden orden, ObservableList<Producto> productos, HashMap<Producto, Integer> cantidad) throws SQLException {

        for (Producto producto : productos) {
            detalles_.add(new OrdenDetalle(orden.getId(), producto, cantidad.get(producto)));
        }

        for(OrdenDetalle detalle : detalles_){
            detalle.setIdOrden(orden.getId());
            AtomicReference<CallableStatement> procedure = new AtomicReference<>(conn.prepareCall("{call OrdenDetalle_Insertar( ?, ?, ?, ?, ?, ?, ?)}"));
            procedure.get().setInt("id", detalle.getIdOrden());
            procedure.get().setInt("productoId", detalle.getProductoId());
            procedure.get().setBigDecimal("precio", detalle.getPrecio());
            procedure.get().setInt("cantidad", detalle.getCantidad());
            procedure.get().setBigDecimal("neto", detalle.getNeto());
            procedure.get().setBigDecimal("impuesto", detalle.getImpuesto());
            procedure.get().setBigDecimal("descuento", detalle.getDescuento());

            boolean esEjecutado = (procedure.get().executeUpdate() > 0);
            if (!esEjecutado) {
                throw new SQLException();
            }
        }
    }

    // Listo. 
    public String insertar(Orden orden, ObservableList<Producto> productos, HashMap<Producto, Integer> cantidad) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try {
                // Deshabilita auto transaccion.
                conn.setAutoCommit(false);
                AtomicReference<CallableStatement> query = new AtomicReference<>(conn.prepareCall("{call Orden_Insertar(?, ?, ?, ?)}"));
                query.get().registerOutParameter("id", JDBCType.INTEGER);
                query.get().setInt("suplidorId", orden.getSuplidorId());
                query.get().setInt("numero", orden.numero_);
                query.get().setString("estatus", String.valueOf(orden.estatus_));

                boolean esEjecutado = (query.get().executeUpdate() > 0);
                if (esEjecutado) {
                    orden.setId(query.get().getInt("id"));
                    insertarDetalles(conn, orden, productos, cantidad);
                    conn.commit(); // Confirma la transaccion.
                    mensaje = "El registro ha sido agregado exitosamente.";
/*                    CallableStatement calcula = conn.prepareCall("{call Orden_CalcularTotal(?)}");
                    calcula.setInt("id", orden.id_);
                    esEjecutado = (calcula.executeUpdate() > 0);
                    if (esEjecutado) {
                        conn.commit(); // Confirma la transaccion.
                        mensaje = "El registro ha sido agregado exitosamente.";
                    }*/

                } else {
                    throw new SQLException();
                }

            } catch (SQLException ex) {
                conn.rollback(); // Cancela la transaccion
                mensaje = "El registro no pudo ser agregado correctamente.";
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
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
            try (CallableStatement query = conn.prepareCall("{call Orden_Eliminar(?)}")) {

                query.setInt("id", orden.id_);
                boolean esEjecutado = (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "La orden ha sido cancelada exitosamente.";
                } else {
                    throw new SQLException( "La orden no pudo ser cancelada.");
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

    // Listo
    public ObservableList<Orden> mostrar() {
        ObservableList<Orden> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar();
             CallableStatement query = conn.prepareCall("{call Orden_Mostrar}")) {
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {

                Suplidor suplidor = new Suplidor();
                int suplidorId = resultSet.getInt("No");
                String suplidorNombre = resultSet.getString("Nombre");
                suplidor.setNombre(suplidorNombre);
                suplidor.setId(suplidorId);

                final int id = resultSet.getInt("Id");
                final int numero = resultSet.getInt("Numero");
                final LocalDate fecha = resultSet.getDate("Fecha").toLocalDate();
                final LocalTime hora = resultSet.getTime("Hora").toLocalTime();
                BigDecimal totalBruto = resultSet.getBigDecimal("Total_Bruto");
                BigDecimal totalDescuento = resultSet.getBigDecimal("Total_Descuento");
                BigDecimal totalImpuesto = resultSet.getBigDecimal("Total_Impuesto");
                BigDecimal totalCargo = resultSet.getBigDecimal("Total_Cargo");
                BigDecimal totalNeto = resultSet.getBigDecimal("Total_Neto");
                Orden obj = new Orden(suplidor, id, numero, fecha, hora,
                        totalBruto, totalDescuento, totalImpuesto, totalCargo, totalNeto);

                data.add(obj);
            }


        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    // Listo
    public ObservableList<OrdenDetalle> mostrarDetalles(int numero) {
        // Texto
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call OrdenDetalle_Mostrar(?)}");
            query.setInt("numero", numero);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {

                final short linea = resultSet.getShort("Linea");

                Producto producto = new Producto();
                final String productoCodigo = resultSet.getString("Codigo_Producto");
                producto.setCodigo(productoCodigo);
                final String productoNombre = resultSet.getString("producto");
                producto.setNombre(productoNombre);
                final String productoDescripcion = resultSet.getString("descripcion");
                producto.setDescripcion(productoDescripcion);
                BigDecimal precio = resultSet.getBigDecimal("Precio");
                producto.setPrecio(precio);

                final int cantidad = resultSet.getInt("cantidad");
                BigDecimal descuento = resultSet.getBigDecimal("Descuento");
                BigDecimal impuesto = resultSet.getBigDecimal("Impuesto");
                BigDecimal neto = resultSet.getBigDecimal("Neto");
                OrdenDetalle detalle = new OrdenDetalle();
                OrdenDetalle obj = new OrdenDetalle(id_, producto,  cantidad,
                        descuento, impuesto, neto);
                obj.setLinea(linea);
                detalles_.add(obj);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
        }

        return detalles_;

    }

    public int getId() {
        return id_;
    }

    public int getNumero() {
        return numero_;
    }

    public int getSuplidorId() {
        return suplidor_.getId();
    }

    public String getSuplidorNombre(){
        return suplidor_.getNombre();
    }

    public LocalDate getFecha() {
        return fecha_;
    }

    public LocalTime getHora(){
        return hora_;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto_;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento_;
    }

    public BigDecimal getTotalImpuesto() {
        return totalImpuesto_;
    }

    public BigDecimal getTotalCargo() {
        return totalCargo_;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto_;
    }

    public void setSuplidorId(int id){
        suplidor_.setId(id);
    }

    public void setSuplidorNombre(String nombre){
        suplidor_.setNombre(nombre);
    }

    public void setId(int id) {
        id_ = id;
    }

    public void setNumero(int numero) {
        numero_ = numero;
    }

    public void setFecha(LocalDate fecha) {
        fecha_ = fecha;
    }

    public void setHora(LocalTime hora) {
        hora_ = hora;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        totalBruto_ = totalBruto;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        totalDescuento_ = totalDescuento;
    }

    public void setTotalImpuesto(BigDecimal totalImpuesto) {
        totalImpuesto_ = totalImpuesto;
    }

    public void setTotalCargo(BigDecimal totalCargo) {
        totalCargo_ = totalCargo;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        totalNeto_ = totalNeto;
    }

    public void setEstatus(String estatus) {
        estatus_ = estatus;
    }
}
