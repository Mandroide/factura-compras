package data;

import java.math.BigDecimal;

public class OrdenDetalle {

    private short linea_;
    private int idOrden_;   // Orden
    private Producto producto_ = new Producto();
    private int cantidad_;
    private BigDecimal descuento_;
    private BigDecimal impuesto_;
    private BigDecimal neto_;

    // Para realizar consultas a la base de datos.
    OrdenDetalle() {

    }

    // Para insertar.

    OrdenDetalle(int idOrden, Producto producto, int cantidad) {
        idOrden_ = idOrden;
        producto_ = producto;
        setPrecio(producto.getPrecio());
        setCantidad(cantidad);
    }

    // Para mostrar.
     OrdenDetalle(int idOrden, Producto producto, int cantidad,
                        BigDecimal descuento, BigDecimal impuesto, BigDecimal neto) {
        idOrden_ = idOrden;
        producto_ = producto;
        setCantidad(cantidad);
        setDescuento(descuento);
        setImpuesto(impuesto);
        setNeto(neto);
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

    public int getProductoId(){
        return producto_.getId();
    }

    public String getProductoCodigo() {
        return producto_.getCodigo();
    }

    public String getProductoNombre() {
        return producto_.getNombre();
    }

    public String getProductoDescripcion() {
        return producto_.getDescripcion();
    }

    public BigDecimal getPrecio() {
        return producto_.getPrecio();
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

    public void setLinea(short linea) {
        linea_ = linea;
    }

    public void setProductoCodigo(String codigo) {
        producto_.setCodigo(codigo);
    }

    public void setProductoNombre(String nombre) {
        producto_.setNombre(nombre);
    }

    public void setProductoDescripcion(String descripcion) {
        producto_.setDescripcion(descripcion);
    }

    public void setPrecio(BigDecimal precio) {
        producto_.setPrecio(precio);
    }

    public void setCantidad(int cantidad) {
        cantidad_ = cantidad;
    }

    public void setDescuento(BigDecimal descuento) {
        descuento_ = descuento;
    }

    public void setImpuesto(BigDecimal impuesto) {
        impuesto_ = impuesto;
    }

    public void setNeto(BigDecimal neto) {
        neto_ = neto;
    }
}
