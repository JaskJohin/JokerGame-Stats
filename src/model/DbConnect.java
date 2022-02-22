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
        String connectionString = "jdbc:derby://localhost/jokerStatData;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData dbMeta = connection.getMetaData();
        ResultSet resultSet = dbMeta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }
            
}
