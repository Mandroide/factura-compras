package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Conexion {

    public static Connection conectar() throws SQLException {
        final String dbUrl = "jdbc:mysql://localhost:3306/compra?useSSL=false";
        final String user = "student";
        final String password = "student123";
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
