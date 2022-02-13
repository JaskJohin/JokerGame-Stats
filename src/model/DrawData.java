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

public class DrawData {
    
    //Table constructor
    private static void createTable() {
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE Draw ("
                    + "gameID int(10) NOT NULL, "
                    + "drawID int(10) NOT NULL, "
                    + "drawTime bigint(19) NOT NULL, "
                    + "status varchar(30) NOT NULL, "
                    + "drawBreak int(10) NOT NULL, "
                    + "visualDraw int(10) NOT NULL, "
                    + "CONSTRAINT drawID "
                    + "PRIMARY KEY (gameID, drawID));";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DrawData.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to drop an existing table
    private static void dropTable() {
        try {
            Connection connection = connect();
            String dropTableSQL = "DROP TABLE Draw;";    
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
    //method to insert data to the table (one tuple at a time)
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
    //method to update a tuple
    private static void updateData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
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
    //method to delete entire tuples for a specific draw, based on the primary key
    private static void deleteDrawData (int gameId, int drawId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM Draw WHERE gameID = ? AND drawID = ?;";
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
    private static void deleteGameData (int gameId) {
        try {
            Connection connection = connect();
            String deleteSQL = "DELETE FROM Draw WHERE gameID = ?;";
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
