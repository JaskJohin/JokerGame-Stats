package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */
public class DbConnect {
        //Method to connect to the database
    public static Connection connect() {
        String connectionString = "jdbc:derby:jokerStatData";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    } 
}
