package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Suplidor {

    private int id_;
    private String empresa_;
    private String direccion_;
    private String ciudad_;
    private String email_;
    private String telefono_;
    private String codigoPostal_;
    private String pais_;
    private char estatus_;

    private String textoABuscar_;

    public Suplidor() {

    }

    // Para buscar
    public Suplidor(String textoABuscar) {
        textoABuscar_ = textoABuscar;
    }

    // Para eliminar
    public Suplidor(int id) {
        id_ = id;
    }

    // Para actualizar
    public Suplidor(int id, String empresa, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, char estatus) {
        this(empresa, direccion, ciudad, email, telefono, codigoPostal, pais, estatus);
        id_ = id;
    }

    // Para insertar
    public Suplidor(String empresa, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, char estatus) {
        empresa_ = empresa;
        direccion_ = direccion;
        ciudad_ = ciudad;
        email_ = email;
        telefono_ = telefono;
        codigoPostal_ = codigoPostal;
        pais_ = pais;
        estatus_ = estatus;
    }

    public String insertar(Suplidor suplidor) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call SP_InsertarSuplidor(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                query.setString("empresa", suplidor.empresa_);
                query.setString("direccion", suplidor.direccion_);
                query.setString("ciudad", suplidor.ciudad_);
                query.setString("email", suplidor.email_);
                query.setString("telefono", suplidor.telefono_);
                query.setString("codigoPostal", suplidor.codigoPostal_);
                query.setString("pais", suplidor.pais_);
                query.setString("estatus", String.valueOf(suplidor.estatus_));

                int tuplas = query.executeUpdate();
                if (tuplas > 0) {
                    mensaje = "El registro ha sido agregado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser agregado correctamente.\n");
                }

            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);

            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;
    }

    public String actualizar(Suplidor suplidor) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call SP_ActualizarSuplidor(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                query.setInt("id", suplidor.id_); // Modificar
                query.setString("empresa", suplidor.empresa_);
                query.setString("direccion", suplidor.direccion_);
                query.setString("ciudad", suplidor.ciudad_);
                query.setString("email", suplidor.email_);
                query.setString("telefono", suplidor.telefono_);
                query.setString("codigoPostal", suplidor.codigoPostal_);
                query.setString("pais", suplidor.pais_);
                query.setString("estatus", String.valueOf(suplidor.estatus_));

                boolean esEjecutado = (query.executeUpdate() > 0);
                if (esEjecutado) {
                    mensaje = "El registro ha sido actualizado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser actualizado correctamente.");
                }

            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    public String eliminar(Suplidor suplidor) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call SP_EliminarSuplidor(?)}")) {
                query.setInt("id", suplidor.id_);

                int tupla = query.executeUpdate();
                if (tupla > 0) {
                    mensaje = "El registro ha sido eliminado exitosamente.";
                } else {
                    throw new SQLException("El registro no pudo ser eliminado correctamente.");
                }

            } catch (SQLException ex) {
                mensaje = ex.getMessage();
                Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            mensaje = "La conexion a la base de datos no pudo ser realizada exitosamente.";
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;

    }

    public ResultSet buscar(Suplidor suplidor) {
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call SP_BuscarSuplidor(?)}");
            query.setString("id", suplidor.textoABuscar_);
            rs = query.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        }

        return rs;
    }

    public ResultSet mostrar() {
        ResultSet rs;
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call sp_MostrarSuplidor()}");
            rs = query.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        }

        return rs;
    }

    public int getId() {
        return id_;
    }

    public String getEmpresa() {
        return empresa_;
    }

    public String getDireccion() {
        return direccion_;
    }

    public String getCiudad() {
        return ciudad_;
    }

    public String getEmail() {
        return email_;
    }

    public String getTelefono() {
        return telefono_;
    }

    public String getCodigoPostal() {
        return codigoPostal_;
    }

    public String getPais() {
        return pais_;
    }

    public char getEstatus() {
        return estatus_;
    }

}
