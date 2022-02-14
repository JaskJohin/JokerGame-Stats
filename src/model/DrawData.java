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

public class DrawData {
    
  
    //Table constructor
    public static void createTable() {
        try {
            Connection connection = DbConnect.connect();
            String createTableSQL = "CREATE TABLE Draws ("
                    + "gameID INTEGER NOT NULL, "
                    + "drawID INTEGER NOT NULL, "
                    + "drawTime BIGINT NOT NULL, "
                    + "status VARCHAR(30) NOT NULL, "
                    + "drawBreak INTEGER NOT NULL, "
                    + "visualDraw INTEGER NOT NULL, "
                    + "CONSTRAINT drawID "
                    + "PRIMARY KEY (gameID, drawID))";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = DbConnect.connect();
            String dropTableSQL = "DROP TABLE Draws";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to select all table contents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = DbConnect.connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM Draws)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //Method to insert data to the table (one tuple at a time)
    public static void insertData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = DbConnect.connect();
            String insertSQL = "INSERT INTO Draws("
                    + "gameID, "
                    + "drawID, "
                    + "drawTime, "
                    + "status, "
                    + "drawBreak, "
                    + "visualDraw) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.setLong(3, drawTime);
            preparedStatement.setString(4, status);
            preparedStatement.setInt(5, drawBreak);
            preparedStatement.setInt(4, visualDraw);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to update a tuple
    public static void updateData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = DbConnect.connect();
            String updateSQL = "UPDATE Draws SET "
                    + "drawTime = ?, "
                    + "status = ?, "
                    + "drawBreak = ?, "
                    + "visualDraw = ? "
                    + "WHERE "
                    + "gameID = ? AND "
                    + "drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setLong(1, drawTime);
            preparedStatement.setString(2, status);
            preparedStatement.setInt(3, drawBreak);
            preparedStatement.setInt(4, visualDraw);
            preparedStatement.setInt(5, gameId);
            preparedStatement.setInt(6, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to delete all tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM Draws WHERE gameID = ? AND drawID = ?";
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
    
    //Method to delete all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM Draws WHERE gameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }   
}