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

public class QueriesSQL {
    
    //Attributes  declaration
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static SimpleDateFormat dateFormat;

    //Method to select all table contents (for testing purposes)
    public static ResultSet selectContentAll() {
        try {
            connection = DbConnect.connect();   
            statement = connection.createStatement();
            String selectSQL = "(SELECT * FROM content)";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            statement.close();
            connection.close();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);          
        }
        return null;
    }
    
    //Method to delete all tuples for a specific draw, based on the primary key
    public static void deleteContentTupple (int gameId, int drawId) {
        try {
            connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM content WHERE gameID = ? AND drawID = ?";
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, drawId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to delete all data for a specific game
    public static void deleteDataByGameId (int gameId) {
        try {
            connection = DbConnect.connect();
            String deleteSQL = "DELETE FROM content WHERE gameID = ?";
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //method to check if a record already exists in the database
    public static boolean checkIfRecordExists (Content content) {
    connection = DbConnect.connect();
    //SQL query to find record. If record exists, returns 1, else 0
    String findRecordsByPK = "SELECT COUNT(1) FROM CONTENT WHERE gameid=? AND drawid=?";
    try {
        preparedStatement = connection.prepareStatement(findRecordsByPK);
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
        Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
    }
    //method to delete data from the database providing a date range
    public static void deleteDataByDateRange (String fromDateStr, String toDateStr) throws ParseException {
        //format the input String
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //parse the formatted String to Date class
        Date fromDate = dateFormat.parse(fromDateStr);
        Date toDate = dateFormat.parse(toDateStr);
        //get the long representation of Epoch which is stored in the database
        long fromEpoch = fromDate.getTime();
        long toEpoch = toDate.getTime();
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String deleteRecordsByDR = "DELETE FROM CONTENT WHERE DRAWTIME >=? AND DRAWTIME<=?";
        try {
            preparedStatement = connection.prepareStatement(deleteRecordsByDR);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}