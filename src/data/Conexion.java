package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class Conexion {

    private Conexion(){
        
    }
    
    static Connection conectar() throws SQLException {
        final String dbUrl = "jdbc:postgresql://localhost/compra";
        final String user = "user";
        final String password = "1234";
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
