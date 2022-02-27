package model;

import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */
public class Utilities {
    
    public static JsonObject GET_API(String urlToCall) throws Exception {
 
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToCall);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while((line = bReader.readLine()) != null) {
                result.append(line);
            }
        }
        JsonParser jParser = new JsonParser();
        JsonObject jsonObj = (JsonObject) jParser.parse(result.toString());
        
        return jsonObj;
    }
    
    public static JsonArray GET_API_ARRAY(String urlToCall) throws Exception {
 
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToCall);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while((line = bReader.readLine()) != null) {
                result.append(line);
            }
        }
        JsonParser jParser = new JsonParser();
        JsonArray jsonArr = (JsonArray) jParser.parse(result.toString());
        
        return jsonArr;
    }
    
    public static void main(String[] args) throws Exception {
        
        //method to store to the DataBase Draw data derived for a date range
        //JsonObject response =  Utilities.GET_API("https://api.opap.gr/draws/v3.0/5104/draw-date/2020-01-01/2020-02-10");
        //AddDataController.storeDrawsDataByDateRange(response);
        
        //method to store signle Draw data to the DataBase
        //JsonObject response2 =  Utilities.GET_API("https://api.opap.gr/draws/v3.0/5104/2402");
        //AddDataController.storeDrawsDataByDrawID(response2);
        
        //delete data from DB for a specific game
        //QueriesSQL.deleteDataByGameId(5104);
        
        //delete data from DB for specific date range
        //QueriesSQL.deleteDataByDateRange("2020-01-01", "2020-02-01");
        
        //QueriesSQL.countJackpots("2021-01-01", "2021-01-31");
        
        //System.out.println(QueriesSQL.singleNumberOccurrences("2000-01-01", "2022-02-24", 14));
        //System.out.println(QueriesSQL.singleNumberDelays("2000-01-01", "2022-02-24", 1));
        
        //System.out.println(QueriesSQL.singleBonusDelays("2000-01-01", "2022-02-24", 5));
        //System.out.println(QueriesSQL.singleBonusOccurrences("2000-01-01", "2022-02-24", 5));
        
        /*
        List<WinningNumberOccurence> top5Nums = QueriesSQL.topFiveWinningNumbersOccurred("2000-01-01", "2022-02-24");
        for(WinningNumberOccurence wnOccurrence: top5Nums) {
            System.out.print("Number: " + wnOccurrence.getWinningNumber());
            System.out.println("\tOccurrences: " + wnOccurrence.getOccurrences());
        }
         */   
        
        List<BonusOccurrence> top5Bonusses = QueriesSQL.topFiveBonusesOccurred("2000-01-01", "2022-02-24");
        for(BonusOccurrence bonusOccurrence: top5Bonusses) {
            System.out.print("Number: " + bonusOccurrence.getBonus());
            System.out.println("\tOccurrences: " + bonusOccurrence.getOccurrences());
        }
    }
}