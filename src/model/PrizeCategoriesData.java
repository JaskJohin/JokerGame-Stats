package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class PrizeCategoriesData {
    
    //Table constructor
    private static void createTable() {
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE PrizeCategories ("
                    + "categoryID int(2) NOT NULL, "
                    + "divident double DEFAULT 0 NOT NULL, "
                    + "winners int(32) DEFAULT 0 NOT NULL, "
                    + "distributed double NOT NULL, "
                    + "jackpot double NOT NULL, fixed double NOT NULL, "
                    + "categoryType int(1) NOT NULL, "
                    + "gameType varchar(30) NOT NULL, "
                    + "DrawdrawID int(10) NOT NULL, "
                    + "DrawgameID int(10) NOT NULL, "
                    + "PRIMARY KEY (categoryID) "
                    + "FOREIGN KEY (gameID, drawID) REFERENCES DrawData(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to drop existing table
    private static void dropTable() {
        try {
            Connection connection = connect();
            String dropTableSQL = "DROP TABLE PrizeCategories;";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //mathod to select all Table congtents (for testing purposes)
    private static ResultSet selectAll() {
        try {
            Connection connection = connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(select * from PrizeCategories)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            //while(resultSet.next()) {
            //    System.out.println(resultSet.getString("gameID") + ", " + resultSet.getString("drawID") + resultSet.getString("drawTime") + resultSet.getString("status") + resultSet.getString("drawBreak") + resultSet.getString("visualDraw"));
            //}
            statement.close();
            connection.close();
            //System.out.println("Done!");
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //method to insert data to the table (one tuple at a time)
    private static void insertData (int categoryId, double divident, int winners, double distributed, double jackpot, double fixed, int categoryType, String gameType, int gameId, int drawId) {
        try {
            Connection connection = connect();
            String insertSQL = "INSERT INTO PrizeCategories("
                    + "categoryID, "
                    + "divident, "
                    + "winners, "
                    + "distributed, "
                    + "jackpot, "
                    + "fixed, "
                    + "categoryType, "
                    + "gameType, "
                    + "gameId, "
                    + "drawId) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to update a tuple
    private static void updateData (int categoryId, double divident, int winners, double distributed, double jackpot, double fixed, int categoryType, String gameType, int gameId, int drawId) {
        try {
            Connection connection = connect();
            String updateSQL = "UPDATE PrizeCategories SET "
                    + "divident = ?, "
                    + "winners = ?, "
                    + "distributed = ?, "
                    + "jackpot = ?, "
                    + "fixed = ?, "
                    + "categoryType = ?, "
                    + "gameType = ?, "
                    + "WHERE gameID = ? AND drawID = ? AND categoryId = ?;";
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
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to delete entire tuples for a specific draw, based on the primary key
    private static void deleteTupple (int gameId, int drawId, int categoryId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM PrizeCategories WHERE gameID = ? AND drawID = ? AND categoryId = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //method to delete entire all data for a specific game
    private static void deleteGameData (int gameId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM PrizeCategories WHERE gameID = ?;";
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
    private static Connection connect() {
        String connectionString = "jdbc:derby:draw";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
}
