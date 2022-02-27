package model;

import POJOs.Content;
import java.sql.*;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        resultSet.close();
        preparedStatement.close();
        connection.close();
        //inform the user for check result (for debugging)
        if(exists)
            System.out.println("Record exists.");
        else
            System.out.println("Record doesn't exist. Adding!");
        return exists;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //method to delete data from the database providing a date range
    public static void deleteDataByDateRange (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
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
    
    //method to return the number of games carried out within a given month
    public static int countMonthlyGames (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyDrawsCountStr = "SELECT COUNT(drawid) AS GAMES_NUM FROM CONTENT WHERE DRAWTIME >=? AND DRAWTIME<=?";
        try {
            preparedStatement = connection.prepareStatement(montlhyDrawsCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            ResultSet drawsCountSet = preparedStatement.executeQuery();
            int drawsCount = 0;
            while(drawsCountSet.next())
                drawsCount = drawsCountSet.getInt(1);
            drawsCountSet.close();
            preparedStatement.close();
            connection.close();
            return drawsCount;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to return the total divident for all games within a given month
    public static double sumMonthlyDivident (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String monthlyDividentSumStr = "SELECT SUM(DIVIDENT) AS TOTAL_DIVIDENT FROM "
                + "(SELECT c.DRAWID, pc.DIVIDENT, c.DRAWTIME FROM "
                + "CONTENT c INNER JOIN PRIZECATEGORIES pc "
                + "ON c.DRAWID = pc.DRAWID) AS JOINED_T "
                + "WHERE DRAWTIME >=? AND DRAWTIME <=?";
        try {
            preparedStatement = connection.prepareStatement(monthlyDividentSumStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.executeQuery();
            ResultSet dividentSumSet = preparedStatement.executeQuery();
            double dividentSum = 0.0;
            while(dividentSumSet.next())
                dividentSum = dividentSumSet.getDouble(1);
            dividentSumSet.close();
            preparedStatement.close();
            connection.close();
            return dividentSum;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }
    
    //method to return the multitude of Jackpots within a given date range
    public static int countJackpots (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT COUNT(WINNERS) AS JACKPOTS FROM "
                + "(SELECT c.DRAWID, pc.CATEGORYID, pc.WINNERS, c.DRAWTIME "
                + "FROM CONTENT c INNER JOIN PRIZECATEGORIES pc "
                + "ON c.DRAWID = pc.DRAWID) AS JOINED_T "
                + "WHERE DRAWTIME >=? AND DRAWTIME <=? AND CATEGORYID = 1 AND WINNERS = 0";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            ResultSet countJackpotSet = preparedStatement.executeQuery();
            int jackpots = 0;
            while(countJackpotSet.next())
               jackpots = countJackpotSet.getInt(1);
            countJackpotSet.close();
            preparedStatement.close();
            connection.close();
            return jackpots;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to return the the mulitutde of a single winning number for a given date range
    public static int singleNumberOccurrences (String fromDateStr, String toDateStr, int number) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT COUNT(NUMBERS) AS occurrences FROM "
                + "(SELECT c.DRAWID, wnl.NUMBER AS NUMBERS, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSLIST wnl ON c.DRAWID = wnl.DRAWID) "
                + "AS JOINED_T WHERE DRAWTIME >=? AND DRAWTIME <=? AND NUMBERS =? ";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.setInt(3, number);
            ResultSet countOccurrencesSet = preparedStatement.executeQuery();
            int occurrences = 0;
            while(countOccurrencesSet.next())
               occurrences = countOccurrencesSet.getInt(1);
            countOccurrencesSet.close();
            preparedStatement.close();
            connection.close();
            return occurrences;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to return the occurrence delay of a single winning number for a given date range
    public static int singleNumberDelays (String fromDateStr, String toDateStr, int number) throws ParseException {
        
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT (SELECT MAX(DRAWID) AS max_draw_ID FROM "
                + "(SELECT c.DRAWID, wnl.NUMBER AS NUMBERS, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSLIST wnl ON c.DRAWID = wnl.DRAWID WHERE DRAWTIME >=? AND DRAWTIME <=? ) maxDID ) - "
                + "(SELECT MAX(DRAWID) AS max_draw_appeared FROM "
                + "(SELECT c.DRAWID, wnl.NUMBER AS NUMBERS, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSLIST wnl ON c.DRAWID = wnl.DRAWID WHERE DRAWTIME >=? AND DRAWTIME <=? ) maxDIDappeared "
                + "WHERE NUMBERS =? ) AS delay from SYSIBM.SYSDUMMY1";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.setLong(3, fromEpoch);
            preparedStatement.setLong(4, toEpoch);
            preparedStatement.setInt(5, number);
            ResultSet countODelaysSet = preparedStatement.executeQuery();
            int delays = 0;
            while(countODelaysSet.next())
               delays = countODelaysSet.getInt(1);
            countODelaysSet.close();
            preparedStatement.close();
            connection.close();
            return delays;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to return the number of occurrences of a given bonus number within a given date range
    public static int singleBonusOccurrences (String fromDateStr, String toDateStr, int bonus) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT COUNT(BONUSES) AS occurrences FROM "
                + "(SELECT c.DRAWID, wnb.BONUS AS BONUSES, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSBONUS wnb ON c.DRAWID = wnb.DRAWID) "
                + "AS JOINED_T WHERE DRAWTIME >=? AND DRAWTIME <=? AND BONUSES =? ";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.setInt(3, bonus);
            ResultSet countOccurrencesSet = preparedStatement.executeQuery();
            int occurrences = 0;
            while(countOccurrencesSet.next())
               occurrences = countOccurrencesSet.getInt(1);
            countOccurrencesSet.close();
            preparedStatement.close();
            connection.close();
            return occurrences;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to return the occurrence delay of a single bonus number
    public static int singleBonusDelays (String fromDateStr, String toDateStr, int bonus) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT (SELECT MAX(DRAWID) AS max_draw_ID FROM "
                + "(SELECT c.DRAWID, wnb.BONUS AS BONUSES, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSBONUS wnb ON c.DRAWID = wnb.DRAWID WHERE DRAWTIME >=? AND DRAWTIME <=? ) maxDID ) - "
                + "(SELECT MAX(DRAWID) AS max_draw_appeared FROM "
                + "(SELECT c.DRAWID, wnb.BONUS AS BONUSES, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSBONUS wnb ON c.DRAWID = wnb.DRAWID WHERE DRAWTIME >=? AND DRAWTIME <=? ) maxDIDappeared "
                + "WHERE BONUSES =? ) AS delay from SYSIBM.SYSDUMMY1";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            preparedStatement.setLong(3, fromEpoch);
            preparedStatement.setLong(4, toEpoch);
            preparedStatement.setInt(5, bonus);
            ResultSet countODelaysSet = preparedStatement.executeQuery();
            int delays = 0;
            while(countODelaysSet.next())
               delays = countODelaysSet.getInt(1);
            countODelaysSet.close();
            preparedStatement.close();
            connection.close();
            return delays;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //method to get the top five winning numbers in terms of occurrences for a given date range
    public static List<WinningNumberOccurence> topFiveWinningNumbersOccurred (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        List<WinningNumberOccurence> topFiveList = new ArrayList<>();
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT NUMBERS, COUNT(NUMBERS) AS occurrences FROM "
                + "(SELECT c.DRAWID, wnl.NUMBER AS NUMBERS, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSLIST wnl ON c.DRAWID = wnl.DRAWID) AS ALL_NUMS "
                + "WHERE DRAWTIME >=? AND DRAWTIME <=? "
                + "GROUP BY NUMBERS ORDER BY occurrences DESC FETCH FIRST 5 ROWS ONLY";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            ResultSet topFiveOccuredSet = preparedStatement.executeQuery();
            while(topFiveOccuredSet.next()) {
                WinningNumberOccurence memberOfTopFive = new WinningNumberOccurence();
                memberOfTopFive.setWinningNumber(topFiveOccuredSet.getInt(1));
                memberOfTopFive.setOccurrences(topFiveOccuredSet.getInt(2));
                topFiveList.add(memberOfTopFive);
            }
            topFiveOccuredSet.close();
            preparedStatement.close();
            connection.close();
            return topFiveList;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //method to get the top five bonus numbers in terms of occurrences for a given date range
    public static List<BonusOccurrence> topFiveBonusesOccurred (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        List<BonusOccurrence> topFiveList = new ArrayList<>();
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT BONUSES, COUNT(BONUSES) AS occurrences FROM "
                + "(SELECT c.DRAWID, wnb.BONUS AS BONUSES, c.DRAWTIME FROM CONTENT c "
                + "INNER JOIN WINNINGNUMBERSBONUS wnb ON c.DRAWID = wnb.DRAWID) AS ALL_BONUSES "
                + "WHERE DRAWTIME >=? AND DRAWTIME <=? "
                + "GROUP BY BONUSES ORDER BY occurrences DESC FETCH FIRST 5 ROWS ONLY";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            ResultSet topFiveOccuredSet = preparedStatement.executeQuery();
            while(topFiveOccuredSet.next()) {
                BonusOccurrence memberOfTopFive = new BonusOccurrence();
                memberOfTopFive.setBonus(topFiveOccuredSet.getInt(1));
                memberOfTopFive.setOccurrences(topFiveOccuredSet.getInt(2));
                topFiveList.add(memberOfTopFive);
            }
            topFiveOccuredSet.close();
            preparedStatement.close();
            connection.close();
            return topFiveList;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //method to get the average winnings (distributed) per category
    public static List<AverageDistributedPrizeCat> averageDistributedPerCategory (String fromDateStr, String toDateStr) throws ParseException {
        //Call respective fucntions toget the long representation of 
        //first and last Dates to Epoch which is stored in the database
        long fromEpoch = fromDateStrToEpoch(fromDateStr);
        long toEpoch = toDateStrToEpoch(toDateStr);
        List<AverageDistributedPrizeCat> averageDistr = new ArrayList<>();
        //connect to the database
        connection = DbConnect.connect();
        //compile the SQL query for the deletion of data for the requested date range
        String montlhyJackpotCountStr = "SELECT prize_category, AVG(distributed) AS average_distributed FROM "
                + "(SELECT c.DRAWID, prCat.CATEGORYID AS prize_category, c.DRAWTIME, prCat.DISTRIBUTED as distributed "
                + "FROM CONTENT c INNER JOIN PRIZECATEGORIES prCat ON c.DRAWID = prCat.DRAWID) AS all_prCats "
                + "WHERE DRAWTIME >=? AND DRAWTIME <=? GROUP BY prize_category ORDER BY prize_category ASC";
        try {
            preparedStatement = connection.prepareStatement(montlhyJackpotCountStr);
            preparedStatement.setLong(1, fromEpoch);
            preparedStatement.setLong(2, toEpoch);
            ResultSet averageDistrPerCatSet = preparedStatement.executeQuery();
            while(averageDistrPerCatSet.next()) {
                AverageDistributedPrizeCat distributedForCat = new AverageDistributedPrizeCat();
                distributedForCat.setCategoryId(averageDistrPerCatSet.getInt(1));
                distributedForCat.setAverageDistributed(averageDistrPerCatSet.getInt(2));
                averageDistr.add(distributedForCat);
            }
            averageDistrPerCatSet.close();
            preparedStatement.close();
            connection.close();
            return averageDistr;
        } catch (SQLException ex) {
            Logger.getLogger(QueriesSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //formatter for the starting date of a given date range (starting at start of date)
    public static long fromDateStrToEpoch (String fromDateStr) {
        //parse the formatted String to Date class
        LocalDateTime fromDate = LocalDate.parse(fromDateStr).atStartOfDay();
        Instant instantFrom = fromDate.atZone(ZoneId.systemDefault()).toInstant();
        long fromEpoch = instantFrom.toEpochMilli();
        return fromEpoch;
    }
    
    //formatter of the ending date of a given date range (ending at 23:59:59)
    public static long toDateStrToEpoch (String toDateStr) {
        //format the input String
        LocalDateTime toDate = LocalDate.parse(toDateStr).atTime(LocalTime.MAX);
        Instant instantTo = toDate.atZone(ZoneId.systemDefault()).toInstant();
        long toEpoch = instantTo.toEpochMilli();
        return toEpoch;
    }
}