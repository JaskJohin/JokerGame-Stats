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
    //attributes declaration
    private static Connection connection;
    private static String connectionString;
    private static DatabaseMetaData dbMeta;
    private static ResultSet resultSet;
    
    //Method to connect to the database
    public static Connection connect() {
        connectionString = "jdbc:derby://localhost/jokerStatData;create=true";
        connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
    //method to check if a table already exists in the database
    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        dbMeta = connection.getMetaData();
        resultSet = dbMeta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }
            
}
