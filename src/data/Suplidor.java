package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Suplidor {

    private int id_;
    private String nombre_;
    private String direccion_;
    private String ciudad_;
    private String email_;
    private String telefono_;
    private String codigoPostal_;
    private String pais_;
    private String estatus_;

    private String textoABuscar_;

    public Suplidor() {
    }

    // Para buscar
    public Suplidor(String textoABuscar) {
        setTextoABuscar(textoABuscar);
    }

    // Para eliminar
    public Suplidor(int id) {
        setId(id);
    }

    // Para actualizar
    public Suplidor(int id, String nombre, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais, String estatus) {
        this(nombre, direccion, ciudad, email, telefono, codigoPostal, pais); // Constructor de insertar
        setEstatus(estatus);
        setId(id);
    }

    // Para insertar
    public Suplidor(String nombre, String direccion, String ciudad, String email,
            String telefono, String codigoPostal, String pais) {
        setNombre(nombre);
        setDireccion(direccion);
        setCiudad(ciudad);
        setEmail(email);
        setTelefono(telefono);
        setCodigoPostal(codigoPostal);
        setPais(pais);
    }

    public String insertar(Suplidor suplidor) {
        String mensaje;
        try (Connection conn = Conexion.conectar()) {
            try (CallableStatement query = conn.prepareCall("{call Suplidor_Insertar(?, ?, ?, ?, ?, ?, ?)}")) {
                query.setString("nombre", suplidor.nombre_);
                query.setString("direccion", suplidor.direccion_);
                query.setString("ciudad", suplidor.ciudad_);
                query.setString("email", suplidor.email_);
                query.setString("telefono", suplidor.telefono_);
                query.setString("codigoPostal", suplidor.codigoPostal_);
                query.setString("pais", suplidor.pais_);

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
            try (CallableStatement query = conn.prepareCall("{call Suplidor_Actualizar(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                query.setInt("id", suplidor.id_); // Modificar
                query.setString("nombre", suplidor.nombre_);
                query.setString("direccion", suplidor.direccion_);
                query.setString("ciudad", suplidor.ciudad_);
                query.setString("email", suplidor.email_);
                query.setString("telefono", suplidor.telefono_);
                query.setString("codigoPostal", suplidor.codigoPostal_);
                query.setString("pais", suplidor.pais_);
                query.setString("estatus", suplidor.estatus_);

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
            try (CallableStatement query = conn.prepareCall("{call Suplidor_Eliminar(?)}")) {
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

    public ObservableList<Suplidor> buscar(Suplidor suplidor) {
        ObservableList<Suplidor> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Suplidor_Buscar(?)}");
            query.setString("nombre", suplidor.textoABuscar_);
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public ObservableList<Suplidor> buscarActivos(Suplidor suplidor) {
        ObservableList<Suplidor> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Suplidor_BuscarActivos(?)}");
            query.setString("nombre", suplidor.textoABuscar_);
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public ObservableList<Suplidor> mostrar() {
        ObservableList<Suplidor> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Suplidor_Mostrar}");
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public ObservableList<Suplidor> mostrarActivos() {
        ObservableList<Suplidor> data = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            CallableStatement query = conn.prepareCall("{call Suplidor_MostrarActivos}");
            data = leer(query.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(Suplidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private ObservableList<Suplidor> leer(ResultSet resultSet) throws SQLException {
        ObservableList<Suplidor> data = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Suplidor suplidor = crear(resultSet);
            try {
                final String estatus = resultSet.getString("Estatus");
                suplidor.setEstatus(estatus);
            } catch (SQLException e) {
            }
            data.add(suplidor);
        }
        return data;
    }

    private Suplidor crear(ResultSet resultSet) throws SQLException {
        int no = resultSet.getInt("Id");
        final String nombre = resultSet.getString("Nombre");
        final String email = resultSet.getString("Email");
        final String telefono = resultSet.getString("Telefono");
        final String direccion = resultSet.getString("Direccion");
        final String pais = resultSet.getString("Pais");
        final String ciudad = resultSet.getString("Ciudad");
        final String codigoPostal = resultSet.getString("Codigo_Postal");
        Suplidor suplidor = new Suplidor(nombre, direccion, ciudad, email, telefono, codigoPostal, pais);
        suplidor.setId(no);
        return suplidor;
    }

    public int getId() {
        return id_;
    }

    public String getNombre() {
        return nombre_;
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

    public String getEstatus() {
        return estatus_;
    }

    public void setId(int id) {
        id_ = id;
    }

    public void setNombre(String nombre) {
        nombre_ = nombre;
    }

    public void setDireccion(String direccion) {
        direccion_ = direccion;
    }

    public void setCiudad(String ciudad) {
        ciudad_ = ciudad;
    }

    public void setEmail(String email) {
        email_ = email;
    }

    public void setTelefono(String telefono) {
        telefono_ = telefono;
    }

    public void setCodigoPostal(String codigoPostal) {
        codigoPostal_ = codigoPostal;
    }

    public void setPais(String pais) {
        pais_ = pais;
    }

    public void setEstatus(String estatus) {
        estatus_ = estatus;
    }

    public void setTextoABuscar(String textoABuscar) {
        this.textoABuscar_ = textoABuscar;
    }

}
