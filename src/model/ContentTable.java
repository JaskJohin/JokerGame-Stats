package model;

import POJOs.Content;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class ContentTable {
    
  
    //Table constructor
    public static void createTable() {
        try {
            Connection connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "CONTENT")) {
                String createTableSQL = "CREATE TABLE content ("
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "drawTime BIGINT NOT NULL, "
                        + "status VARCHAR(30) NOT NULL, "
                        + "drawBreak INTEGER NOT NULL, "
                        + "visualDraw INTEGER NOT NULL, "
                        + "PRIMARY KEY (gameID, drawID))";
                Statement statement = connection.createStatement();
                statement.execute(createTableSQL);
                statement.close();
                connection.close();
            }
            } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }     
    }
    
    //Method to drop existing table
    public static void dropTable() {
        try {
            Connection connection = DbConnect.connect();
            String dropTableSQL = "DROP TABLE content";    
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to select all table contents (for testing purposes)
    public static ResultSet selectAll() {
        try {
            Connection connection = DbConnect.connect();   
            Statement statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM content)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    //Method to insert data to the table (one tuple at a time)
    public static void insertData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = DbConnect.connect();
            String insertSQL = "INSERT INTO content("
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
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to update a tuple
    public static void updateData (int gameId, int drawId, long drawTime, String status, int drawBreak, int visualDraw) {
        try {
            Connection connection = DbConnect.connect();
            String updateSQL = "UPDATE content SET "
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
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Method to delete all tuples for a specific draw, based on the primary key
    public static void deleteTupple (int gameId, int drawId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM content WHERE gameID = ? AND drawID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to delete all data for a specific game
    public static void deleteGameData (int gameId) {
        try {
            Connection connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM content WHERE gameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to check if a record already exists in the database
    public static boolean checkIfRecordExists (Content content) {
    Connection connection = DbConnect.connect();
    //SQL query to find record. If record exists, returns 1, else 0
    String findRecordsByPK = "SELECT COUNT(1) FROM CONTENT WHERE gameid=? AND drawid=?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(findRecordsByPK);
        preparedStatement.setInt(1, content.getContentPK().getGameid());
        preparedStatement.setInt(2, content.getContentPK().getDrawid());
        //definee a local variable to store SQL query returned value 
        ResultSet resultSet = preparedStatement.executeQuery();
        //definee a local variable to store SQL query returned value
        boolean exists = false;
        while(resultSet.next())
            exists = resultSet.getInt(1) == 1; 
        preparedStatement.close();
        connection.close();
        //inform the user for check result (for debugging)
        System.out.println(exists);
        return exists;
    } catch (SQLException ex) {
        Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
    }
    //method to delete data from the database providing a date range
    public static void deleteDataByDateRange (String fromDateStr, String toDateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(fromDateStr);
        Date toDate = dateFormat.parse(toDateStr);
        long fromEpoch = fromDate.getTime();
        long toEpoch = toDate.getTime();
        
        Connection connection = DbConnect.connect();
        String deleteRecordsByDR = "DELETE FROM CONTENT WHERE DRAWTIME >=? AND DRAWTIME<=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordsByDR);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ContentTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}