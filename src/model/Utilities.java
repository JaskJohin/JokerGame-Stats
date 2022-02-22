package model;

import model.AddDataController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import model.*;

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
    
    public static void main(String[] args) throws Exception {
        
        //method to store to the DataBase Draw data derived for a date range
        JsonObject response =  Utilities.GET_API("https://api.opap.gr/draws/v3.0/5104/draw-date/2020-01-01/2020-02-01");
        AddDataController.storeDrawsDataByDateRange(response);
        
        //method to store signle Draw data to the DataBase
        JsonObject response2 =  Utilities.GET_API("https://api.opap.gr/draws/v3.0/5104/2404");
        AddDataController.storeDrawsDataByDrawID(response2);
        
        //delete data from DB for a specific game
        QueriesSQL.deleteDataByGameId(5104);
        
        //delete data from DB for specific date range
        QueriesSQL.deleteDataByDateRange("2020-01-01", "2020-02-01");

    }
}