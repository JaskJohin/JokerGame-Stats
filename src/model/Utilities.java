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
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class Utilities {
    
    //Method to retrieve data from OPAP API where data root is JsonObject
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
    
    //Method to retrieve data from OPAP API where data root is JsonArray
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
    
    //Method to create pdf file from statistical data
    public void createPdf(String fromDate, String toDate, String dest) throws IOException, ParseException {
        
        //Variable declaration
        Integer number, bonus;

        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
      
        //Create PdfFonts to be used for text formatting
        PdfFont normalFont = PdfFontFactory.createFont(getClass().getResource("/resources/NotoSans-Regular.ttf").toString());
        PdfFont boldFont = PdfFontFactory.createFont(getClass().getResource("/resources/NotoSans-Bold.ttf").toString());
        PdfFont boldItalicFont = PdfFontFactory.createFont(getClass().getResource("/resources/NotoSans-BoldItalic.ttf").toString());
        PdfFont italicFont = PdfFontFactory.createFont(getClass().getResource("/resources/NotoSans-Italic.ttf").toString());
        
        //initialize outer table to define to columns for the document
        Table outerTable = new Table(2).useAllAvailableWidth();
        
        //Initialize the left column of the outer table
        Cell left = new Cell();
        
        //Outer Table cells will have no border
        left.setBorder(Border.NO_BORDER);
        
        //Add a description of the contents of the table
        Paragraph par1 = new Paragraph();
        Text text1 = new Text("Εμφανίσεις και καθυστερήσεις αριθμών");
        text1.setFont(boldFont);
        text1.setFontSize(16);
        par1.add(text1);
        
        //Add some more information for the date range 
        //for which statistical data are presented for winning numbers
        Paragraph par2 = new Paragraph();
        Text text2 = new Text("κληρώσεις: " + fromDate + " - " + toDate);
        text2.setFont(italicFont);
        text2.setFontSize(12);
        par2.add(text2);
        
        //Add both paragraphs to the table cell
        left.add(par1);
        left.add(par2);
        //Add a blank line
        left.add(new Paragraph("\n").setFontSize(14));
        
        //Create a nested Table to store the winning number statistics
        Table nestedTable1 = new Table(UnitValue
                .createPercentArray(new float[]{100f, 100f, 100f}))
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        //Add the headers. For each header define formatting individually
        //1st column header
        Paragraph ltHeader1 = new Paragraph();
        Text textHeader1 = new Text("Αριθμός");
        textHeader1.setFont(boldItalicFont);
        textHeader1.setFontSize(13);
        ltHeader1.add(textHeader1);
        nestedTable1.addHeaderCell(ltHeader1);
        //2nd column header
        Paragraph ltHeader2 = new Paragraph();
        Text textHeader2 = new Text("Εμφανίσεις");
        textHeader2.setFont(boldItalicFont);
        textHeader2.setFontSize(13);
        ltHeader2.add(textHeader2);
        nestedTable1.addHeaderCell(ltHeader2);
        //3rd column header
        Paragraph ltHeader3 = new Paragraph();
        Text textHeader3 = new Text("Καθυστερήσεις");
        textHeader3.setFont(boldItalicFont);
        textHeader3.setFontSize(13);
        ltHeader3.add(textHeader3);
        nestedTable1.addHeaderCell(ltHeader3);
        
        //Add the statistical data for the 45 (winning) numbers
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
        
        //Add the nested table to the left cell (column)
        left.add(nestedTable1);
        
        //Create the right cell
        Cell right = new Cell();
        right.setBorder(Border.NO_BORDER);
        
        //Same as above but now for the right table 
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
        
        //Add 1st column header
        Paragraph rtHeader1 = new Paragraph();
        Text textHeaderRight1 = new Text("Αριθμός Τζόκερ");
        textHeaderRight1.setFont(boldItalicFont);
        textHeaderRight1.setFontSize(13);
        rtHeader1.add(textHeaderRight1);
        nestedTable2.addHeaderCell(rtHeader1);
        
        //Add 2nd column header
        Paragraph rtextHeaderRight2 = new Paragraph();
        Text textHeaderRight2 = new Text("Εμφανίσεις");
        textHeaderRight2.setFont(boldItalicFont);
        textHeaderRight2.setFontSize(13);
        rtextHeaderRight2.add(textHeaderRight2);
        nestedTable2.addHeaderCell(rtextHeaderRight2);
        
        //Add 3rd column header
        Paragraph rtextHeaderRight3 = new Paragraph();
        Text textHeaderRight3 = new Text("Καθυστερήσεις");
        textHeaderRight3.setFont(boldItalicFont);
        textHeaderRight3.setFontSize(13);
        rtextHeaderRight3.add(textHeaderRight3);
        nestedTable2.addHeaderCell(rtextHeaderRight3);

        //Add the bonus statistical data
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
        
        //Add the 2nd nested table to the right cell (column)
        right.add(nestedTable2);
        
        //Add both left and right cells to the outer table
        outerTable.addCell(left);
        outerTable.addCell(right);
        
        //Also the outer table to the document
        document.add(outerTable);
        
        //Close document
        document.close();
    }
}