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

public class WinningNumbersData {
   
    //Table constructor
    public static void createTable() {
        try {
            Connection connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "WinningNumbers")) {
                String createTableSQL = "CREATE TABLE WinningNumbers ("
                        + "INDEX INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY, "
                        + "number1 INTEGER NOT NULL,"
                        + "number2 INTEGER NOT NULL,"
                        + "number3 INTEGER NOT NULL,"
                        + "number4 INTEGER NOT NULL,"
                        + "number5 INTEGER NOT NULL,"
                        + "bonus INTEGER NOT NULL,"
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "FOREIGN KEY (gameID, drawID) REFERENCES Draws(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                Statement statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = DbConnect.connect();
            String dropTableSQL = "DROP TABLE WinningNumbers";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to select all table contents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = DbConnect.connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM WinningNumbers)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //Method to insert data to the table (one tuple at a time)
    public static void insertData (int number1,int number2 ,int number3, int number4, int number5, int bonus,  int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String insertSQL = "INSERT INTO WinningNumbers("
                    + "number1, "
                    + "number2, "
                    + "number3, "
                    + "number4, "
                    + "number5, "
                    + "bonus, "
                    + "gameId, "
                    + "drawId) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, number1);
            preparedStatement.setInt(2, number2);
            preparedStatement.setInt(3, number3);
            preparedStatement.setInt(4, number4);
            preparedStatement.setInt(5, number5);
            preparedStatement.setInt(6, bonus);
            preparedStatement.setInt(7, gameId);
            preparedStatement.setInt(8, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to update a tuple
    public static void updateData (int number1, int number2 ,int number3, int number4, int number5, int bonus,  int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String updateSQL = "UPDATE WinningNumbers SET "
                    + "number1 = ?, "
                    + "number2 = ?, "
                    + "number3 = ?, "
                    + "number4 = ?, "
                    + "number5 = ?, "
                    + "bonus = ?, "
                    + "WHERE gameID = ? AND drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, number1);
            preparedStatement.setInt(2, number2);
            preparedStatement.setInt(3, number3);
            preparedStatement.setInt(4, number4);
            preparedStatement.setInt(5, number5);
            preparedStatement.setInt(6, bonus);
            preparedStatement.setInt(7, gameId);
            preparedStatement.setInt(8, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to delete all tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM WinningNumbers WHERE gameID = ? AND drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to delete all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM WinningNumbers WHERE gameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(WinningNumbersData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }    
}
