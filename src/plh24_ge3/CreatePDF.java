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
import com.itextpdf.text.pdf.PdfPTable;
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
        String fromDate = "2020-01-01";
        String toDate = "2020-02-20";
        Integer number, bonus;

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

        
        Table outerTable = new Table(2).useAllAvailableWidth();
        
        
        Cell left = new Cell();
        left.setBorder(Border.NO_BORDER);
        left.add(new Paragraph("Εμφανίσεις και καθυστερήσεις αριθμών").setFontSize(14).setBold());
        left.add(new Paragraph("").setFontSize(14));
        left.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12));
        left.add(new Paragraph("").setFontSize(14));
        
        Table nestedTable1 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        nestedTable1.addCell("Αριθμός");
        nestedTable1.addCell("Εμφανίσεις");
        nestedTable1.addCell("Καθυστερήσεις");
                
        for(int i = 0; i < 45; i++) {
 
            number = i + 1;
            nestedTable1.addCell(number.toString());
            Integer occurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, i + 1);
            nestedTable1.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleNumberDelays(fromDate, toDate, i + 1);
            nestedTable1.addCell(delays.toString());     
        }
        
        left.add(nestedTable1);
        
        Cell right = new Cell();
        right.setBorder(Border.NO_BORDER);
        
        //document.add(new Paragraph("_________________________________________________________________").setFontSize(14));
        //document.add(new Paragraph("").setFontSize(14));
        //document.add(new Paragraph("").setFontSize(14));
        right.add(new Paragraph("Εμφανίσεις και καθυστερήσεις Τζόκερ").setFontSize(14).setBold());
        right.add(new Paragraph("").setFontSize(14));
        right.add(new Paragraph("κληρώσεις: " + fromDate + " - " + toDate).setFontSize(12));
        right.add(new Paragraph("").setFontSize(14));
        
        Table nestedTable2 = new Table(UnitValue.createPercentArray(new float[]{100f, 100f, 100f}));
        nestedTable2.addCell("Αριθμός Τζόκερ");
        nestedTable2.addCell("Εμφανίσεις");
        nestedTable2.addCell("Καθυστερήσεις");
        
        for(int j = 0; j < 20; j++) {
 
            bonus = j + 1;
            nestedTable2.addCell(bonus.toString());
            Integer occurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, j + 1);
            nestedTable2.addCell(occurrences.toString());
            Integer delays = QueriesSQL.singleBonusDelays(fromDate, toDate, j + 1);
            nestedTable2.addCell(delays.toString());
        
        } 
        right.add(nestedTable2);
        
        outerTable.addCell(left);
        outerTable.addCell(right);
        document.add(outerTable);
        //Close document
        document.close();
    }

}