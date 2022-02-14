package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Athanasios Theodoropoulos
 */

public class WagerData {
    
//Table constructor
    public static void createTable() {
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE Wagers ("
                    + "columns int(10) DEFAULT 0 NOT NULL, "
                    + "wagers int(10) DEFAULT 0 NOT NULL, "
                    +"FOREIGN KEY (gameID, drawID) REFERENCES Draws(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = connect();
            String dropTableSQL = "DROP TABLE Wagers;";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //mathod to select all Table congtents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(select * from Wagers)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //method to insert data to the table (one tuple at a time)
    public static void insertData (int column,int wagers,  int gameId, int drawId) {
        try {
            Connection connection = connect();
            String insertSQL = "INSERT INTO Wagers("
                    + "column, "
                    + "wagers, "
                    + "gameId, "
                    + "drawId) "
                    + "VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, column);
            preparedStatement.setInt(2, wagers);
            preparedStatement.setInt(3, gameId);
            preparedStatement.setInt(4, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to update a tuple
    public static void updateData (int column, int wagers ,  int gameId, int drawId) {
        try {
            Connection connection = connect();
            String updateSQL = "UPDATE Wagers SET "
                    + "column = ?, "
                    + "wagers = ?, "
                    + "WHERE gameID = ? AND drawID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, column);
            preparedStatement.setInt(2, wagers);
            preparedStatement.setInt(3, gameId);
            preparedStatement.setInt(4, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to delete entire tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM Wagers WHERE gameID = ? AND drawID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //method to delete entire all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM Wagers WHERE gameID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to connect to the database
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
