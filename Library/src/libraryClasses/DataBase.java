
package mysmartlibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/mySmartLibrary?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; 

    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            
            System.err.println("MySQL JDBC Driver not found. Add the connector jar to classpath.");
            e.printStackTrace();
        }
   
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }
}
