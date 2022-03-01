package plh24_ge3;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.UnitValue;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import model.QueriesSQL;

/**
 * Simple table example.
 */
public class CreatePDF {
    public static final String DEST = "files/chapter01/test1.pdf";

    public static void main(String args[]) throws IOException, ParseException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CreatePDF().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, ParseException {
        String fromDate = "2020-01-01";
        String toDate = "2020-02-20";
        Integer number;

        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
        
        FontSet fontSet = new FontSet();
        fontSet.addFont("fonts/NotoSans-Bold.ttf");
        fontSet.addFont("fonts/NotoSans-BoldItalic.ttf");
        fontSet.addFont("fonts/NotoSans-Italic.ttf");
        fontSet.addFont("fonts/NotoSans-Regular.ttf");
        document.setFontProvider(new FontProvider(fontSet));
        document.setProperty(Property.FONT, new String[]{"NotoGreekFonts"});

        document.add(new Paragraph("Εμφανίσεις και καθυστερήσεις αριθμών").setFontSize(14));
        document.add(new Paragraph("").setFontSize(14));
        document.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12));
        document.add(new Paragraph("").setFontSize(14));
        
        
        Table table1 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        table1.addCell("Αριθμός");
        table1.addCell("Εμφανίσεις");
        table1.addCell("Καθυστερήσεις");
                
        for(int i = 0; i < 45; i++) {
 
            number = i + 1;
            table1.addCell(number.toString());
            Integer occurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, i + 1);
            table1.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleNumberDelays(fromDate, toDate, i + 1);
            table1.addCell(delays.toString());     
        }
        
        document.add(table1);
        document.add(new Paragraph("_________________________________________________________________").setFontSize(14));
        document.add(new Paragraph("").setFontSize(14));
        document.add(new Paragraph("").setFontSize(14));
        document.add(new Paragraph("Εμφανίσεις και καθυστερήσεις Τζόκερ").setFontSize(14));
        document.add(new Paragraph("").setFontSize(14));
        document.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12));
        document.add(new Paragraph("").setFontSize(14));
        
        Table table2 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        table2.addCell("Αριθμός Τζόκερ");
        table2.addCell("Εμφανίσεις");
        table2.addCell("Καθυστερήσεις");
        
        for(int j = 0; j < 20; j++) {
 
            number = j + 1;
            table2.addCell(number.toString());
            Integer occurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, j + 1);
            table2.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleBonusDelays(fromDate, toDate, j + 1);
            table2.addCell(delays.toString());
        
        } 
        
        document.add(table2);
        //Close document
        document.close();
    }

}