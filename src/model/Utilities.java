package model;

import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
    
    public void createPdf(String fromDate, String toDate, String dest) throws IOException, ParseException {
        //Variable declaration
        Integer number, bonus;

        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
        
        //set Fonts for the documents (support Greek)
        FontSet fontSet = new FontSet();
        fontSet.addFont("fonts/NotoSans-Bold.ttf");
        fontSet.addFont("fonts/NotoSans-BoldItalic.ttf");
        fontSet.addFont("fonts/NotoSans-Italic.ttf");
        fontSet.addFont("fonts/NotoSans-Regular.ttf");
        document.setFontProvider(new FontProvider(fontSet));
        document.setProperty(Property.FONT, new String[]{"NotoGreekFonts"});
        
        //initialize outer table to define to columns for the document
        Table outerTable = new Table(2).useAllAvailableWidth();
        //initialize the left column of the outer table
        Cell left = new Cell();
        //outer Table cells will have no border
        left.setBorder(Border.NO_BORDER);
        //Add some text defining what will be displayed in the left column
        left.add(new Paragraph("Εμφανίσεις και καθυστερήσεις αριθμών").setFontSize(14).setBold());
        left.add(new Paragraph("").setFontSize(14));
        left.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12)).setItalic();
        left.add(new Paragraph("").setFontSize(14));
        
        //create a nested Table to store the winning number statistics
        Table nestedTable1 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        
        //Add the headers
        nestedTable1.addHeaderCell("Αριθμός").setBold().setItalic();
        nestedTable1.addHeaderCell("Εμφανίσεις").setBold().setItalic();
        nestedTable1.addHeaderCell("Καθυστερήσεις").setBold().setItalic();
        
        //and add the statistical data for teh 45 numbers
        for(int i = 0; i < 45; i++) {
            number = i + 1;
            nestedTable1.addCell(number.toString());
            Integer occurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, i + 1);
            nestedTable1.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleNumberDelays(fromDate, toDate, i + 1);
            nestedTable1.addCell(delays.toString());     
        }
        //add the nested Table to the left cell (column)
        left.add(nestedTable1);
        
        //Create the right Cell
        Cell right = new Cell();
        //also with no border
        right.setBorder(Border.NO_BORDER);
        
        //Add some text to define Tables contents
        right.add(new Paragraph("Εμφανίσεις και καθυστερήσεις Τζόκερ").setFontSize(14).setBold());
        right.add(new Paragraph("").setFontSize(14));
        right.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12)).setItalic();
        right.add(new Paragraph("").setFontSize(14));
        
        //Create another nested Table for the bonus stsatistics
        Table nestedTable2 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        nestedTable2.addHeaderCell("Αριθμός Τζόκερ").setBold().setItalic();
        nestedTable2.addHeaderCell("Εμφανίσεις").setBold().setItalic();
        nestedTable2.addHeaderCell("Καθυστερήσεις").setBold().setItalic();
        //add the bonus statistical data
        for(int j = 0; j < 20; j++) {
            bonus = j + 1;
            nestedTable2.addCell(bonus.toString());
            Integer occurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, j + 1);
            nestedTable2.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleBonusDelays(fromDate, toDate, j + 1);
            nestedTable2.addCell(delays.toString());
        } 
        //add the 2nd nested table to the right cell (column)
        right.add(nestedTable2);
        
        //add both left and right cells to the outer Table
        outerTable.addCell(left);
        outerTable.addCell(right);
        //and also the outer table to the document
        document.add(outerTable);
        //Close document
        document.close();
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
            System.out.print("Winning number: " + wnOccurrence.getWinningNumber());
            System.out.println("\t\tOccurrences: " + wnOccurrence.getOccurrences());
        }

        System.out.println("---------------------");
        List<BonusOccurrence> top5Bonusses = QueriesSQL.topFiveBonusesOccurred("2000-01-01", "2022-02-24");
        System.out.println("Top 5 bonus numbers by occurrence");
        for(BonusOccurrence bonusOccurrence: top5Bonusses) {
            System.out.print("Bonus number: " + bonusOccurrence.getBonus());
            System.out.println("\t\tOccurrences: " + bonusOccurrence.getOccurrences());
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