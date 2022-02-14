package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thanos Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class PrizeCategoriesData {

    //Table constructor
    public static void createTable() {
        try {
            Connection connection = DbConnect.connect();
            String createTableSQL = "CREATE TABLE PrizeCategories ("
                    + "categoryID INTEGER(2) PRIMARY KEY, "
                    + "divident DOUBLE DEFAULT 0 NOT NULL, "
                    + "winners INTEGER DEFAULT 0 NOT NULL, "
                    + "distributed DOUBLE NOT NULL, "
                    + "jackpot DOUBLE NOT NULL, "
                    + "fixed double NOT NULL, "
                    + "categoryType INTEGER(1) NOT NULL, "
                    + "gameType VARCHAR(30) NOT NULL, "
                    + "drawID INTEGER(10) NOT NULL, "
                    + "gameID INTEGER(10) NOT NULL, "
                    + "FOREIGN KEY (gameID, drawID) REFERENCES Draws (gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = DbConnect.connect();
            String dropTableSQL = "DROP TABLE PrizeCategories";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to select all table contents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = DbConnect.connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM PrizeCategories)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();

            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //method to insert data to the table (one tuple at a time)
    public static void insertData (int categoryId, double divident, int winners, double distributed, double jackpot, double fixed, int categoryType, String gameType, int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String insertSQL = "INSERT INTO PrizeCategories "
                    + "(categoryID, "
                    + "divident, "
                    + "winners, "
                    + "distributed, "
                    + "jackpot, "
                    + "fixed, "
                    + "categoryType, "
                    + "gameType, "
                    + "gameId, "
                    + "drawId) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setDouble(2, divident);
            preparedStatement.setInt(3, winners);
            preparedStatement.setDouble(4, distributed);
            preparedStatement.setDouble(5, jackpot);
            preparedStatement.setDouble(6, fixed);
            preparedStatement.setInt(7, categoryType);
            preparedStatement.setString(8, gameType);
            preparedStatement.setInt(9, gameId);
            preparedStatement.setInt(10, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to update a tuple
    public static void updateData (int categoryId, double divident, int winners, double distributed, double jackpot, double fixed, int categoryType, String gameType, int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String updateSQL = "UPDATE PrizeCategories SET "
                    + "divident = ?, "
                    + "winners = ?, "
                    + "distributed = ?, "
                    + "jackpot = ?, "
                    + "fixed = ?, "
                    + "categoryType = ?, "
                    + "gameType = ?, "
                    + "WHERE gameID = ? AND drawID = ? AND categoryId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setDouble(1, divident);
            preparedStatement.setInt(2, winners);
            preparedStatement.setDouble(3, distributed);
            preparedStatement.setDouble(4, jackpot);
            preparedStatement.setDouble(5, fixed);
            preparedStatement.setInt(6, categoryType);
            preparedStatement.setString(7, gameType);
            preparedStatement.setInt(8, gameId);
            preparedStatement.setInt(9, drawId);
            preparedStatement.setInt(10, categoryId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to delete all tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId, int categoryId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM PrizeCategories WHERE gameID = ? AND drawID = ? AND categoryId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //method to delete all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM PrizeCategories WHERE gameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrizeCategoriesData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }    
}
