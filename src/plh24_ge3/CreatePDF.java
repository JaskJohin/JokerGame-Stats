package plh24_ge3;

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
import java.text.ParseException;
import model.QueriesSQL;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class CreatePDF {

    public void createPdf(String dest) throws IOException, ParseException {
        
        //just for demo purposes. Need to be replaced with user input in final version.
        String fromDate = "2020-01-01";
        String toDate = "2020-02-20";
        
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
        nestedTable1.addCell("Αριθμός").setBold().setItalic();
        nestedTable1.addCell("Εμφανίσεις").setBold().setItalic();
        nestedTable1.addCell("Καθυστερήσεις").setBold().setItalic();
        
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
        nestedTable2.addCell("Αριθμός Τζόκερ").setBold().setItalic();
        nestedTable2.addCell("Εμφανίσεις").setBold().setItalic();
        nestedTable2.addCell("Καθυστερήσεις").setBold().setItalic();
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

}