package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Conexion {

    private Conexion(){
        
    }
    
    static Connection conectar() throws SQLException {
        final String dbUrl = "jdbc:mysql://127.0.0.1:3306/compra?useSSL=false";
        final String user = "student";
        final String password = "student123";
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
