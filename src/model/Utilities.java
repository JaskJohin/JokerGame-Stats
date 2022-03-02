package model;

import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
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
    //method to retrieve data from OPAP API where data root is JsonObject
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
    
    //method to retrieve data from OPAP API where data root is JsonArray
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
    
    //method to create pdf file from statistical data
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
        //create PdfFonts to be used for text formatting
        PdfFont normalFont = PdfFontFactory.createFont("fonts/NotoSans-Regular.ttf");
        PdfFont boldFont = PdfFontFactory.createFont("fonts/NotoSans-Bold.ttf");
        PdfFont boldItalicFont = PdfFontFactory.createFont("fonts/NotoSans-BoldItalic.ttf");
        PdfFont italicFont = PdfFontFactory.createFont("fonts/NotoSans-Italic.ttf");
        
        //initialize outer table to define to columns for the document
        Table outerTable = new Table(2).useAllAvailableWidth();
        //initialize the left column of the outer table
        Cell left = new Cell();
        //outer Table cells will have no border
        left.setBorder(Border.NO_BORDER);
        //add a description of the contents of the table
        Paragraph par1 = new Paragraph();
        Text text1 = new Text("Εμφανίσεις και καθυστερήσεις αριθμών");
        text1.setFont(boldFont);
        text1.setFontSize(16);
        par1.add(text1);
        //add some more information for the date range 
        //for which statistical data are presented for winning numbers
        Paragraph par2 = new Paragraph();
        Text text2 = new Text("κληρώσεις: " + fromDate + " - " + toDate);
        text2.setFont(italicFont);
        text2.setFontSize(12);
        par2.add(text2);
        //add both paragraphs to the table cell
        left.add(par1);
        left.add(par2);
        //add a blank line
        left.add(new Paragraph("\n").setFontSize(14));
        
        //create a nested Table to store the winning number statistics
        Table nestedTable1 = new Table(UnitValue
                .createPercentArray(new float[]{100f, 100f, 100f}))
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        //Add the headers. For each header define text and formatting individually
        //1st column header
        Paragraph ltHeader1 = new Paragraph();
        Text textHeader1 = new Text("Αριθμός");
        textHeader1.setFont(boldItalicFont);
        textHeader1.setFontSize(13);
        ltHeader1.add(textHeader1);
        nestedTable1.addHeaderCell(ltHeader1);
        //2nd cloumn header
        Paragraph ltHeader2 = new Paragraph();
        Text textHeader2 = new Text("Εμφανίσεις");
        textHeader2.setFont(boldItalicFont);
        textHeader2.setFontSize(13);
        ltHeader2.add(textHeader2);
        nestedTable1.addHeaderCell(ltHeader2);
        //third column header
        Paragraph ltHeader3 = new Paragraph();
        Text textHeader3 = new Text("Καθυστερήσεις");
        textHeader3.setFont(boldItalicFont);
        textHeader3.setFontSize(13);
        ltHeader3.add(textHeader3);
        nestedTable1.addHeaderCell(ltHeader3);
        
        //and add the statistical data for the 45 (winning) numbers
        for(int i = 0; i < 45; i++) {
            number = i + 1;
            Paragraph p1 = new Paragraph();
            Text t1 = new Text(number.toString());
            t1.setFont(normalFont);
            t1.setFontSize(12);
            p1.add(t1);
            nestedTable1.addCell(p1);
            Integer occurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, i + 1);
            Paragraph p2 = new Paragraph();
            Text t2 = new Text(occurrences.toString());
            t2.setFont(normalFont);
            t2.setFontSize(12);
            p2.add(t2);
            nestedTable1.addCell(p2);
            Integer delays = QueriesSQL.singleNumberDelays(fromDate, toDate, i + 1);
            Paragraph p3 = new Paragraph();
            Text t3 = new Text(delays.toString());
            t3.setFont(normalFont);
            t3.setFontSize(12);
            p3.add(t3);
            nestedTable1.addCell(p3);     
        }
        //add the nested Table to the left cell (column)
        left.add(nestedTable1);
        
        //Create the right Cell
        Cell right = new Cell();
        //also with no border
        right.setBorder(Border.NO_BORDER);
        //same as above but now for the right table 
        //in which bonus statistics will be displayed
        Paragraph par3 = new Paragraph();
        Text text3 = new Text("Εμφανίσεις και καθυστερήσεις Τζόκερ");
        text3.setFont(boldFont);
        text3.setFontSize(16);
        par3.add(text3);
        
        Paragraph par4 = new Paragraph();
        Text text4 = new Text("κληρώσεις: " + fromDate + " - " + toDate);
        text4.setFont(italicFont);
        text4.setFontSize(12);
        par4.add(text4);
        
        right.add(par3);
        right.add(par4);
        right.add(new Paragraph("\n").setFontSize(14));
        
        //Create a nested table to store bonus statistics
        Table nestedTable2 = new Table(UnitValue
                .createPercentArray(new float[]{100f, 100f, 100f}))
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        //add 1st column header
        Paragraph rtHeader1 = new Paragraph();
        Text textHeaderRight1 = new Text("Αριθμός Τζόκερ");
        textHeaderRight1.setFont(boldItalicFont);
        textHeaderRight1.setFontSize(13);
        rtHeader1.add(textHeaderRight1);
        nestedTable2.addHeaderCell(rtHeader1);
        //add 2nd column header
        Paragraph rtextHeaderRight2 = new Paragraph();
        Text textHeaderRight2 = new Text("Εμφανίσεις");
        textHeaderRight2.setFont(boldItalicFont);
        textHeaderRight2.setFontSize(13);
        rtextHeaderRight2.add(textHeaderRight2);
        nestedTable2.addHeaderCell(rtextHeaderRight2);
        //add 3rd column header
        Paragraph rtextHeaderRight3 = new Paragraph();
        Text textHeaderRight3 = new Text("Καθυστερήσεις");
        textHeaderRight3.setFont(boldItalicFont);
        textHeaderRight3.setFontSize(13);
        rtextHeaderRight3.add(textHeaderRight3);
        nestedTable2.addHeaderCell(rtextHeaderRight3);

        //add the bonus statistical data
        for(int j = 0; j < 20; j++) {
            bonus = j + 1;
            Paragraph p1 = new Paragraph();
            Text t1 = new Text(bonus.toString());
            t1.setFont(normalFont);
            t1.setFontSize(12);
            p1.add(t1);
            nestedTable2.addCell(p1);
            Integer occurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, j + 1);
            Paragraph p2 = new Paragraph();
            Text t2 = new Text(occurrences.toString());
            t2.setFont(normalFont);
            t2.setFontSize(12);
            p2.add(t2);
            nestedTable2.addCell(p2);
            Integer delays = QueriesSQL.singleBonusDelays(fromDate, toDate, j + 1);
            Paragraph p3 = new Paragraph();
            Text t3 = new Text(delays.toString());
            t3.setFont(normalFont);
            t3.setFontSize(12);
            p3.add(t3);
            nestedTable2.addCell(p3);
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

        List<WinningNumberOccurrence> top5Nums = QueriesSQL.topFiveWinningNumbersOccurred("2000-01-01", "2022-02-24");
        System.out.println("Top 5 winning numbers by occurrence");
        for(WinningNumberOccurrence wnOccurrence: top5Nums) {
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