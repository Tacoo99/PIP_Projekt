package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection conDB()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/magazyn", "root", "");
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Baza SQL : "+ex.getMessage());
           return null;
        }
    }

}
