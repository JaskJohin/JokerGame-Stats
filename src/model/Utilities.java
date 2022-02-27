package model;

import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
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
        
        QueriesSQL.countJackpots("2021-01-01", "2021-01-31");
        System.out.println("----------------------");
        
        System.out.println("Number \t Occurences \t Delays");
        int number = 5;
        System.out.print("   " + number);
        System.out.print("\t\t" + QueriesSQL.singleNumberOccurrences("2000-01-01", "2022-02-24", number));
        System.out.println("\t    " +QueriesSQL.singleNumberDelays("2000-01-01", "2022-02-24", number));
        System.out.println("----------------------");
        
        int bonus = 2;
        System.out.println("Bonus, \t Occurences, \t Delays");
        System.out.print("   " + bonus);
        System.out.print("\t\t" + QueriesSQL.singleBonusDelays("2000-01-01", "2022-02-24", bonus));
        System.out.println("\t    " + QueriesSQL.singleBonusOccurrences("2000-01-01", "2022-02-24", bonus));
        System.out.println("----------------------");

        List<WinningNumberOccurence> top5Nums = QueriesSQL.topFiveWinningNumbersOccurred("2000-01-01", "2022-02-24");
        System.out.println("Top 5 winning numbers by occurrence");
        for(WinningNumberOccurence wnOccurrence: top5Nums) {
            System.out.print("Number: " + wnOccurrence.getWinningNumber());
            System.out.println("\tOccurrences: " + wnOccurrence.getOccurrences());
        }

        System.out.println("---------------------");
        List<BonusOccurrence> top5Bonusses = QueriesSQL.topFiveBonusesOccurred("2000-01-01", "2022-02-24");
        System.out.println("Top 5 bonus numbers by occurrence");
        for(BonusOccurrence bonusOccurrence: top5Bonusses) {
            System.out.print("Number: " + bonusOccurrence.getBonus());
            System.out.println("\tOccurrences: " + bonusOccurrence.getOccurrences());
        }
        
        System.out.println("---------------------");
        List<AverageDistributedPrizeCat> averagePerCat = QueriesSQL.averageDistributedPerCategory("2000-01-01", "2022-02-24");
        System.out.println("Average distrubuted earning per Category");
        for(AverageDistributedPrizeCat average: averagePerCat) {
            System.out.print("Category ID: " + average.getCategoryId());
            System.out.println("\tDistributed: " + average.getAverageDistributed());
        }
        
    }
}