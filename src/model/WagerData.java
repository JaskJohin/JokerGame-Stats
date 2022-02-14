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

public class WagerData {
 
    //Table constructor
    public static void createTable() {
        try {
            Connection connection = DbConnect.connect();
            String createTableSQL = "CREATE TABLE Wagers ("
                    + "columns INTEGER DEFAULT 0 NOT NULL, "
                    + "wagers INTEGER DEFAULT 0 NOT NULL, "
                    + "gameID INTEGER NOT NULL, "
                    + "drawID INTEGER NOT NULL, "
                    + "FOREIGN KEY (gameID, drawID) REFERENCES Draws(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = DbConnect.connect();
            String dropTableSQL = "DROP TABLE Wagers";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to select all table contents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = DbConnect.connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM Wagers)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //method to insert data to the table (one tuple at a time)
    public static void insertData (int column,int wagers,  int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String insertSQL = "INSERT INTO Wagers "
                    + "(column, "
                    + "wagers, "
                    + "gameId, "
                    + "drawId) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, column);
            preparedStatement.setInt(2, wagers);
            preparedStatement.setInt(3, gameId);
            preparedStatement.setInt(4, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to update a tuple
    public static void updateData (int column, int wagers ,  int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String updateSQL = "UPDATE Wagers SET "
                    + "column = ?, "
                    + "wagers = ?, "
                    + "WHERE gameID = ? AND drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, column);
            preparedStatement.setInt(2, wagers);
            preparedStatement.setInt(3, gameId);
            preparedStatement.setInt(4, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to delete all tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM Wagers WHERE gameID = ? AND drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //method to delete all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM Wagers WHERE gameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WagerData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }    
}
