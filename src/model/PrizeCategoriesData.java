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
    
    private static void createTable() {
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE PrizeCategories ("
                    + "categoryID int(32) NOT NULL, "
                    + "divident double DEFAULT 0 NOT NULL, "
                    + "winners int(32) DEFAULT 0 NOT NULL, "
                    + "`distributed` double NOT NULL, "
                    + "jackpot double NOT NULL, "
                    + "fixed double NOT NULL, "
                    + "categoryType int(32) NOT NULL, "
                    + "gameType varchar(100) NOT NULL, "
                    + "DrawdrawID int(32) NOT NULL, "
                    + "DrawgameID int(32) NOT NULL, "
                    + "PRIMARY KEY (categoryID, DrawdrawID, DrawgameID));";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    private static void dropTable() {
        try {
            Connection connection = connect();
            String dropTableSQL = "DROP TABLE IF EXISTS Draw;";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    private static ResultSet selectAll() {
        try {
            Connection connection = connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(select * from Draw)";
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
    
    private static void insertData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = connect();
            String insertSQL = "INSERT INTO Draw("
                    + "gameID, "
                    + "drawID, "
                    + "drawTime, "
                    + "status, "
                    + "drawBreak, "
                    + "visualDraw) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
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
    
    private static void updateDraw (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = connect();
            String updateSQL = "UPDATE Draw SET "
                    + "drawTime = ?, "
                    + "status = ?, "
                    + "drawBreak = ?, "
                    + "visualDraw = ? "
                    + "WHERE "
                    + "gameID = ? AND "
                    + "drawID = ?;";
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
    
    private static void deleteData (int gameId, int drawId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM Draw WHERE gameID = ? AND drawID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            int count = preparedStatement.executeUpdate();
            if(count > 0)
                System.out.println(count + " Records updated");
            preparedStatement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    private static Connection connect() {
        String connectionString = "jdbc:derby:prizeCategories";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
}