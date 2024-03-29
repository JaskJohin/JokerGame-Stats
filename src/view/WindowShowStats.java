package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import POJOs.AverageDistributedPrizeCat;
import POJOs.BonusOccurrence;
import model.QueriesSQL;
import POJOs.WinningNumberOccurrence;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.OverlayLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;


/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowShowStats
{
	// Variables declaration
	private String firstDrawDate;
	private String searchDate1 = "";
	private String searchDate2;
	private ChartPanel chartTop5Numbers;
	private ChartPanel chartTop5Bonus;
	private ChartPanel chartAvgDistributed;
	private final JDialog dialog;
	private final JComboBox comboBoxGameSelect;
	private final JTextField textFieldDate1;
	private final JTextField textFieldDate2;
	private final JComboBox comboBoxPredefinedRange;
	private final JComboBox comboBoxSelectView;
	private final JPanel viewPanelCards;
	private final JTable numbersStatsTable;
	private final JTable bonusNumbersStatsTable;
	private final JPanel chart1Panel;
	private final JPanel chart2Panel;
	private final JPanel chart3Panel;


	// Methods
	/** 
	 * Stores the date of the 1st draw of the selected game to the variable firstDrawDate.
	 * firstDrawDate is used for checking if the dates are valid. This method is called
	 * when the window is first opened and every time a different game is selected.
	 */
	private void findDateOfFirstDraw()
	{
		// The 1st draw can be found from: "https://api.opap.gr/draws/v3.0/{gameId}/1";
		// Kino is an exception. It's earliest data are from about 3 years in the past.
		if (comboBoxGameSelect.getSelectedItem().equals("Κίνο"))
		{
			// Current local date
			LocalDate dateNow = LocalDate.now();

			// Date format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			firstDrawDate = formatter.format(dateNow.minusYears(3));
		}

		switch (comboBoxGameSelect.getSelectedItem().toString())
		{
			case "Powerspin": firstDrawDate = "2020-06-30"; break;
			case "Super3":    firstDrawDate = "2002-11-25"; break;
			case "Πρότο":     firstDrawDate = "2000-01-01"; break;
			case "Λόττο":     firstDrawDate = "2000-01-01"; break;
			case "Τζόκερ":    firstDrawDate = "2000-01-01"; break;
			case "Extra5":    firstDrawDate = "2002-11-25"; break;
		}
	}


	/**
	 * Gather statistics for the selected game using the DB.
	 * @param gameId  Game id
	 * @param date1   Start date
	 * @param date2   End date
	 */
	private void fetchDataFromDB(String gameId, String date1, String date2)
	{
		int wnOccurrences, wnDelays, bonusOccurrences, bonusDelays;
		String fromDate = date1;
		String toDate = date2;

		// Populate numbersStatsTable
		for(int i = 0; i < 45; i++)
		{
			try
			{
				numbersStatsTable.setValueAt("", i, 1);
				numbersStatsTable.setValueAt("", i, 2);
				wnOccurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, (i + 1));
				wnDelays = QueriesSQL.singleNumberDelays(fromDate, toDate, (i + 1));

				numbersStatsTable.setValueAt(wnOccurrences, i, 1);
				if (wnOccurrences != 0)
				{
					numbersStatsTable.setValueAt(wnDelays, i, 2);
				}
				else
				{
					numbersStatsTable.setValueAt("-", i, 2);
				}
			}
			catch (ParseException ex)
			{
				ex.printStackTrace();
			}
		}

		// Populate bonusNumbersStatsTable 
		for(int j = 0; j < 20; j++)
		{
			try
			{
				bonusNumbersStatsTable.setValueAt("", j, 1);
				bonusNumbersStatsTable.setValueAt("", j, 2);
				bonusOccurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, (j + 1));
				bonusDelays = QueriesSQL.singleBonusDelays(fromDate, toDate, (j + 1));

				bonusNumbersStatsTable.setValueAt(bonusOccurrences, j, 1);
				if (bonusOccurrences != 0)
				{
					bonusNumbersStatsTable.setValueAt(bonusDelays, j, 2);
				}
				else
				{
					bonusNumbersStatsTable.setValueAt("-", j, 2);
				}
			}
			catch (ParseException ex)
			{
				ex.printStackTrace();
			}
		}
	}


	/**
	 * Create the top 5 winning numbers chart
	 * @param gameId  Game id
	 * @param date1   Start date
	 * @param date2   End date
	 */
	private void createChart1(String gameId, String date1, String date2)
	{
		// Winning numbers occurence list
		List<WinningNumberOccurrence> wnOccList = null;
		try
		{
			wnOccList = QueriesSQL.topFiveWinningNumbersOccurred(date1, date2);
		}
		catch (ParseException ex)
		{
			ex.printStackTrace();
		}

		// Create the dataset
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		// Row keys
		wnOccList.forEach((wnOcc) -> {
			final String series = "Αριθμός: " + wnOcc.getWinningNumber();

			dataSet.addValue(wnOcc.getOccurrences(), series, "");
		});


		// Create chart
		JFreeChart chart = createChart(dataSet, "Πέντε πιο συχνά εμφανιζόμενοι αριθμοί", "Αριθμοί", "Πλήθος εμφανίσεων");
		chartTop5Numbers = new ChartPanel(chart);

		// Refresh graph1Panel to show the new chart
		chart1Panel.removeAll();
		chart1Panel.add(chartTop5Numbers);
		chart1Panel.revalidate();
		chart1Panel.repaint();
	}


	/**
	 * Create the top 5 bonus numbers chart
	 * @param gameId  Game id
	 * @param date1   Start date
	 * @param date2   End date
	 */
	private void createChart2(String gameId, String date1, String date2)
	{
		// Bonus numbers occurence list
		List<BonusOccurrence> bonusOccList = null;
		try
		{
			bonusOccList = QueriesSQL.topFiveBonusesOccurred(date1, date2);
		}
		catch (ParseException ex)
		{
			ex.printStackTrace();
		}

		// Create the dataset
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		// Row keys
		bonusOccList.forEach((bonusOcc) -> {
			final String series = "Αριθμός: " + bonusOcc.getBonus();

			dataSet.addValue(bonusOcc.getOccurrences(), series, "");
		});


		// Create chart
		JFreeChart chart = createChart(dataSet, "Πέντε πιο συχνά εμφανιζόμενοι αριθμοί Τζόκερ", "Αριθμοί Τζόκερ", "Πλήθος εμφανίσεων");
		chartTop5Bonus = new ChartPanel(chart);

		// Refresh graph1Panel to show the new chart
		chart2Panel.removeAll();
		chart2Panel.add(chartTop5Bonus);
		chart2Panel.revalidate();
		chart2Panel.repaint();
	}


	/**
	 * Create the average distributed winnings per category chart
	 * @param gameId  Game id
	 * @param date1   Start date
	 * @param date2   End date
	 */
	private void createChart3(String gameId, String date1, String date2)
	{
		// Bonus numbers occurence list
		List<AverageDistributedPrizeCat> averageDistrList = null;
		try
		{
			averageDistrList = QueriesSQL.averageDistributedPerCategory(date1, date2);
		}
		catch (ParseException ex)
		{
			ex.printStackTrace();
		}

		// Create the dataset
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		// Row keys
		averageDistrList.forEach((averageDistr) -> {
			final String series = "Κατηγορία: " + averageDistr.getCategoryId();

			dataSet.addValue(averageDistr.getAverageDistributed(), series, "");
		});


		// Create chart
		JFreeChart chart = createChart(dataSet, "Μέσος όρος κερδών ανά κατηγορία", "Κατηρορίες", "Διανεμημένα κέρδη");
		chartAvgDistributed = new ChartPanel(chart);

		// Refresh graph1Panel to show the new chart
		chart3Panel.removeAll();
		chart3Panel.add(chartAvgDistributed);
		chart3Panel.revalidate();
		chart3Panel.repaint();
	}


	/**
	 * Creates a chart. Uses the jfreechart library.
	 * @param dataset     Dataset
	 * @param title       Chart title
	 * @param xAxisLabel  Domain axis label
	 * @param yAxisLabel  Range axis label
	 * @return 
	 */
	private JFreeChart createChart(final CategoryDataset dataset, String title, String xAxisLabel, String yAxisLabel)
	{
		// Create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(
			title,                     // Chart title
			xAxisLabel,                // Domain axis label
			yAxisLabel,                // Range axis label
			dataset,                   // Data
			PlotOrientation.VERTICAL,  // Orientation
			true,                      // Include legend
			true,                      // Tooltips
			false                      // URLs
		);

		chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(new java.awt.Color(240, 240, 240));
		plot.setRangeGridlinePaint(Color.DARK_GRAY);

		NumberFormat number = NumberFormat.getNumberInstance();
		number.setMaximumFractionDigits(0);

		// Customise the range axis...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setNumberFormatOverride(number);
		rangeAxis.setAutoRangeIncludesZero(true);

		// Customise the renderer...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER));
		renderer.setBaseItemLabelsVisible(true);
		//renderer.setDrawShapes(true);

		renderer.setSeriesStroke(
			0, new BasicStroke(
				2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				1.0f, new float[] {10.0f, 6.0f}, 0.0f
			)
		);
		renderer.setSeriesStroke(
			1, new BasicStroke(
				2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				1.0f, new float[] {6.0f, 6.0f}, 0.0f
			)
		);
		renderer.setSeriesStroke(
			2, new BasicStroke(
				2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				1.0f, new float[] {2.0f, 6.0f}, 0.0f
			)
		);

		return chart;
	}


 	/**
	 * Method to create pdf file from statistical data
	 * @param fromDate  Start date
	 * @param toDate    End date
	 * @param dest      Full name of file to save
	 * @param basename  Base name of file to save
	 * @throws ParseException 
	 */
	public void createPdf(String fromDate, String toDate, String dest, String basename) throws ParseException {

		//Variable declaration
		Integer number, bonus;

		//Initialize PDF writer
		PdfWriter writer;
		try {
			writer = new PdfWriter(dest);
		} catch (FileNotFoundException ex) {
			String message = "Σφάλμα δημιουργίας αρχείου.\nΒεβαιωθείτε πως έχετε δικαίωμα εγγραφής στο φάκελο.";
			JOptionPane.showMessageDialog(null, message, "Σφάλμα εγγραφής", 0);
			return;
		}

		//Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf, PageSize.A4);
		document.setMargins(26, 26, 26, 26); // (top, right, bottom, left)

		//Create PdfFonts to be used for text formatting
		PdfFont normalFont = null;
		PdfFont boldFont = null;
		PdfFont boldItalicFont = null;
		PdfFont italicFont = null;
		try {
			normalFont = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-Regular.ttf").toString());
			boldFont = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-Bold.ttf").toString());
			boldItalicFont = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-BoldItalic.ttf").toString());
			italicFont = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-Italic.ttf").toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				normalFont = PdfFontFactory.createFont("Helvetica", "Cp1253");
				boldFont = PdfFontFactory.createFont("Helvetica-Bold", "Cp1253");
				boldItalicFont = PdfFontFactory.createFont("Helvetica-BoldOblique", "Cp1253");
				italicFont = PdfFontFactory.createFont("Helvetica-Oblique", "Cp1253");
			} catch (Exception ex1) {System.err.println(ex1);}
		}

		//Add title to the document
		Paragraph par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-4f).setMarginBottom(6f);
		par.add(new Text(basename).setFont(boldFont).setFontSize(18));
		document.add(par);

		//initialize outer table to define to columns for the document
		Table outerTable = new Table(2).useAllAvailableWidth();

		//Initialize the left column of the outer table
		Cell left = new Cell();

		//Outer Table cells will have no border
		left.setBorder(Border.NO_BORDER);

		//Add a description of the contents of the table
		Paragraph par1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		Text text1 = new Text("Εμφανίσεις και καθυστερήσεις αριθμών");
		text1.setFont(boldFont);
		text1.setFontSize(14);
		par1.add(text1);

		//Add paragraph to the table cell
		left.add(par1);
		//Add a blank line
		left.add(new Paragraph("\n").setFontSize(6));

		//Create a nested Table to store the winning number statistics
		Table nestedTable1 = new Table(UnitValue
				.createPercentArray(new float[]{100f, 100f, 100f}))
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		nestedTable1.setMarginRight(4);

		//Add the headers. For each header define formatting individually
		//1st column header
		Paragraph ltHeader1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		ltHeader1.setMarginTop(10f);
		Text textHeader1 = new Text("Αριθμός");
		textHeader1.setFont(boldItalicFont);
		textHeader1.setFontSize(13);
		ltHeader1.add(textHeader1);
		nestedTable1.addHeaderCell(ltHeader1);
		//2nd column header
		Paragraph ltHeader2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		ltHeader2.setMarginTop(10f);
		Text textHeader2 = new Text("Εμφανίσεις");
		textHeader2.setFont(boldItalicFont);
		textHeader2.setFontSize(13);
		ltHeader2.add(textHeader2);
		nestedTable1.addHeaderCell(ltHeader2);
		//3rd column header
		Paragraph ltHeader3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		Text textHeader3 = new Text("Καθυστέρηση εμφάνισης");
		textHeader3.setFont(boldItalicFont);
		textHeader3.setFontSize(13);
		ltHeader3.add(textHeader3);
		nestedTable1.addHeaderCell(ltHeader3);

		//Add the statistical data for the 45 (winning) numbers
		for(int i = 0; i < 45; i++) {
			number = i + 1;
			Paragraph p1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t1 = new Text(number.toString());
			t1.setFont(normalFont);
			t1.setFontSize(12);
			p1.add(t1);
			nestedTable1.addCell(p1);
			Integer occurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, i + 1);
			Paragraph p2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t2 = new Text(occurrences.toString());
			t2.setFont(normalFont);
			t2.setFontSize(12);
			p2.add(t2);
			nestedTable1.addCell(p2);
			Integer delays = QueriesSQL.singleNumberDelays(fromDate, toDate, i + 1);
			Paragraph p3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t3 = new Text(delays.toString());
			t3.setFont(normalFont);
			t3.setFontSize(12);
			if (occurrences.toString().equals("0")) {t3 = new Text("-");}
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
		Paragraph par3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		Text text3 = new Text("Εμφανίσεις και καθυστερήσεις Τζόκερ");
		text3.setFont(boldFont);
		text3.setFontSize(14);
		par3.add(text3);

		right.add(par3);
		right.add(new Paragraph("\n").setFontSize(6));

		//Create a nested table to store bonus statistics
		Table nestedTable2 = new Table(UnitValue
				.createPercentArray(new float[]{100f, 100f, 100f}))
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		nestedTable2.setMarginLeft(4);

		//Add 1st column header
		Paragraph rtHeader1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		Text textHeaderRight1 = new Text("Αριθμός Τζόκερ");
		textHeaderRight1.setFont(boldItalicFont);
		textHeaderRight1.setFontSize(13);
		rtHeader1.add(textHeaderRight1);
		nestedTable2.addHeaderCell(rtHeader1);

		//Add 2nd column header
		Paragraph rtextHeaderRight2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		rtextHeaderRight2.setMarginTop(10f);
		Text textHeaderRight2 = new Text("Εμφανίσεις");
		textHeaderRight2.setFont(boldItalicFont);
		textHeaderRight2.setFontSize(13);
		rtextHeaderRight2.add(textHeaderRight2);
		nestedTable2.addHeaderCell(rtextHeaderRight2);

		//Add 3rd column header
		Paragraph rtextHeaderRight3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		Text textHeaderRight3 = new Text("Καθυστέρηση εμφάνισης");
		textHeaderRight3.setFont(boldItalicFont);
		textHeaderRight3.setFontSize(13);
		rtextHeaderRight3.add(textHeaderRight3);
		nestedTable2.addHeaderCell(rtextHeaderRight3);

		//Add the bonus statistical data
		for(int j = 0; j < 20; j++) {
			bonus = j + 1;
			Paragraph p1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t1 = new Text(bonus.toString());
			t1.setFont(normalFont);
			t1.setFontSize(12);
			p1.add(t1);
			nestedTable2.addCell(p1);
			Integer occurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, j + 1);
			Paragraph p2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t2 = new Text(occurrences.toString());
			t2.setFont(normalFont);
			t2.setFontSize(12);
			p2.add(t2);
			nestedTable2.addCell(p2);
			Integer delays = QueriesSQL.singleBonusDelays(fromDate, toDate, j + 1);
			Paragraph p3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
			Text t3 = new Text(delays.toString());
			t3.setFont(normalFont);
			t3.setFontSize(12);
			if (occurrences.toString().equals("0")) {t3 = new Text("-");}
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

		//Add new line
		document.add(new Paragraph("\n").setFontSize(12));

		//Note
		Paragraph note = new Paragraph();
		Text line1 = new Text("Σημείωση:\n");
		line1.setFont(italicFont);
		line1.setFontSize(12);
		Text line2 = new Text("Καθυστέρηση εμφάνισης '0' σημαίνει ότι ο αριθμός εμφανίστηκε στην πιο πρόσφατη κλήρωση,");
		line2.setFont(normalFont);
		line2.setFontSize(12);
		Text line3 = new Text("καθυστέρηση εμφάνισης '1' σημαίνει ότι ο αριθμός εμφανίστηκε στην προτελευταία κλήρωση, κτλ.\n");
		line3.setFont(normalFont);
		line3.setFontSize(12);
		Text line4 = new Text("Καθυστέρηση εμφάνισης '-' σημαίνει ότι ο αριθμός δεν έχει εμφανίστεί καθόλου στο συγκεκριμένο εύρος ημερομηνιών.");
		line4.setFont(normalFont);
		line4.setFontSize(12);
		note.add(line1);
		note.add(line2);
		note.add(line3);
		note.add(line4);
		document.add(note);

		//New page
		document.add(new AreaBreak());

		// Generate images from the chart panels
		int w = chart1Panel.getWidth();
		int h = chart1Panel.getHeight();
		BufferedImage bi1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g1 = bi1.createGraphics();
		chart1Panel.paint(g1);

		w = chart2Panel.getWidth();
		h = chart2Panel.getHeight();
		BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi2.createGraphics();
		chart2Panel.paint(g2);

		w = chart3Panel.getWidth();
		h = chart3Panel.getHeight();
		BufferedImage bi3 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g3 = bi3.createGraphics();
		chart3Panel.paint(g3);

		// Add images to pdf
		try
		{
			ImageData imgData1 = ImageDataFactory.create(bi1, null);
			com.itextpdf.layout.element.Image pdfImg1 = new com.itextpdf.layout.element.Image(imgData1);
			pdfImg1.setWidth(540);  // 543 max
			pdfImg1.setHeight(246);

			ImageData imgData2 = ImageDataFactory.create(bi2, null);
			com.itextpdf.layout.element.Image pdfImg2 = new com.itextpdf.layout.element.Image(imgData2);
			pdfImg2.setWidth(540);  // 543 max
			pdfImg2.setHeight(246);

			ImageData imgData3 = ImageDataFactory.create(bi3, null);
			com.itextpdf.layout.element.Image pdfImg3 = new com.itextpdf.layout.element.Image(imgData3);
			pdfImg3.setWidth(540);  // 543 max
			pdfImg3.setHeight(246);

			document.add(pdfImg1);
			document.add(new Paragraph("\n").setFontSize(10));
			document.add(pdfImg2);
			document.add(new Paragraph("\n").setFontSize(10));
			document.add(pdfImg3);
		}
		catch (IOException ex) {ex.printStackTrace();}

		//Close document
		document.close();
	}


	// Button actions
	/**
	 * Action of the comboBoxGameSelect.
	 * @param evt 
	 */
	private void comboBoxGameSelectActionPerformed(java.awt.event.ActionEvent evt)
	{
		findDateOfFirstDraw();
	}


	/**
	 * Action of the comboBoxPredefinedRange.
	 * @param evt 
	 */
	private void comboBoxPredefinedRangeActionPerformed(java.awt.event.ActionEvent evt)
	{
		// Current local date
		LocalDate dateNow = LocalDate.now();

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// First draw date
		LocalDate first = null;

		// Change the text in textFieldDate1 and textFieldDate2
		switch (comboBoxPredefinedRange.getSelectedIndex())
		{
			case 0:    // Last week
				textFieldDate1.setText(formatter.format(dateNow.minusDays(6)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 1:    // Last month
				textFieldDate1.setText(formatter.format(dateNow.minusMonths(1).plusDays(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 2:    // Last 3 months
				textFieldDate1.setText(formatter.format(dateNow.minusMonths(3).plusDays(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 3:    // Last year
				textFieldDate1.setText(formatter.format(dateNow.minusYears(1).plusDays(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 4:    // Last 5 years
				textFieldDate1.setText(formatter.format(dateNow.minusYears(5).plusDays(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 5:    // First year
				textFieldDate1.setText(firstDrawDate);
				first = LocalDate.parse(firstDrawDate);
				textFieldDate2.setText(formatter.format(first.plusYears(1).minusDays(1)));
				break;
			case 6:    // First 5 years
				textFieldDate1.setText(firstDrawDate);
				first = LocalDate.parse(firstDrawDate);
				textFieldDate2.setText(formatter.format(first.plusYears(5).minusDays(1)));
				break;
			case 7:    // All
				textFieldDate1.setText(firstDrawDate);
				textFieldDate2.setText(formatter.format(dateNow));
				break;
		}
	}


	/**
	 * Action of the buttonFetchData.
	 * @param evt 
	 */
	private void buttonFetchDataActionPerformed(java.awt.event.ActionEvent evt)
	{
		// Check if Java DB server is started
		try
		{
			DriverManager.getConnection("jdbc:derby://localhost/opapGameStatistics");
		}
		catch (SQLException ex)
		{
			String errorMsg = "Ο server της βάσης δεδομένων δεν είναι ενεργοποιημένος.";
			JOptionPane.showMessageDialog(null, errorMsg, "Σφάλμα σύνδεσης στη ΒΔ", 0);
			return;
		}

		// Get selected game id
		String gameId = null;
		switch (comboBoxGameSelect.getSelectedItem().toString())
		{
			case "Κίνο":      gameId = "1100"; break;
			case "Powerspin": gameId = "1110"; break;
			case "Super3":    gameId = "2100"; break;
			case "Πρότο":     gameId = "2101"; break;
			case "Λόττο":     gameId = "5103"; break;
			case "Τζόκερ":    gameId = "5104"; break;
			case "Extra5":    gameId = "5106"; break;
		}


		// Date variables
		LocalDate firstDate = LocalDate.parse(firstDrawDate);
		LocalDate dateNow = LocalDate.now();
		LocalDate date1;
		LocalDate date2;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Check if given dates are valid
		String errorMsgDates = "Οι ημερομηνίες πρέπει να είναι της μορφής YYYY-MM-DD.";
		try
		{
			date1 = LocalDate.parse(textFieldDate1.getText());
			date2 = LocalDate.parse(textFieldDate2.getText());
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, errorMsgDates, "Λάθος είσοδος", 0);
			return;
		}

		// Chech if the date range is valid
		String errorMsgRange1 = "Η αρχική ημερομηνία πρέπει να είναι από " + firstDrawDate + " και η τελική έως " + formatter.format(dateNow) + ".";
		if (!date1.plusDays(1).isAfter(firstDate) || !date2.minusDays(1).isBefore(dateNow))
		{
			JOptionPane.showMessageDialog(null, errorMsgRange1, "Λάθος είσοδος", 0);
			return;
		}

		String errorMsgRange2 = "Η αρχική ημερομηνία πρέπει να είναι πριν την τελική.";
		if (!date1.minusDays(1).isBefore(date2))
		{
			JOptionPane.showMessageDialog(null, errorMsgRange2, "Λάθος είσοδος", 0);
			return;
		}

		// Set the search dates
		searchDate1 = textFieldDate1.getText();
		searchDate2 = textFieldDate2.getText();

		// Fetch data
		fetchDataFromDB(gameId, textFieldDate1.getText(), textFieldDate2.getText());

		// Create Chart 1
		createChart1(gameId, textFieldDate1.getText(), textFieldDate2.getText());

		// Create Chart 2
		createChart2(gameId, textFieldDate1.getText(), textFieldDate2.getText());

		// Create Chart 3
		createChart3(gameId, textFieldDate1.getText(), textFieldDate2.getText());
	}


	/**
	 * Action of the comboBoxSelectView.
	 * @param evt 
	 */
	private void comboBoxSelectViewActionPerformed(java.awt.event.ActionEvent evt)
	{
		CardLayout cl = (CardLayout)(viewPanelCards.getLayout());
		switch (comboBoxSelectView.getSelectedIndex())
		{
			case 0:
				cl.show(viewPanelCards, "numbersTables");
				break;
			case 1:
				cl.show(viewPanelCards, "chart1");
				break;
			case 2:
				cl.show(viewPanelCards, "chart2");
				break;
			case 3:
				cl.show(viewPanelCards, "chart3");
				break;
		}
	}


	/**
	 * Action of the buttonExportToPdf.
	 * @param evt 
	 */
	private void buttonExportToPdfActionPerformed(java.awt.event.ActionEvent evt)
	{
		// Check if the statistics have been calculated
		String errorMsg = "Δεν έχει γίνει υπολογισμός των στατιστικών!";
		if (searchDate1.isEmpty())
		{
			JOptionPane.showMessageDialog(null, errorMsg, "Σφάλμα", 0);
			return;
		}

		// Check if Java DB server is started
		try
		{
			DriverManager.getConnection("jdbc:derby://localhost/opapGameStatistics");
		}
		catch (SQLException ex)
		{
			errorMsg = "Ο server της βάσης δεδομένων δεν είναι ενεργοποιημένος.";
			JOptionPane.showMessageDialog(null, errorMsg, "Σφάλμα σύνδεσης στη ΒΔ", 0);
			return;
		}

		// Create file name
		String basename = "Στατιστικά " + comboBoxGameSelect.getSelectedItem().toString() + " από " + searchDate1 + " έως " + searchDate2;
		String filename = basename + ".pdf";


		// Window saveAs
		JFileChooser fileChooser = new JFileChooser()
		{
			@Override
			public JDialog createDialog(Component parent)
			{
				JDialog dialog = super.createDialog(parent);
				dialog.setMinimumSize(new Dimension(600, 400));
				return dialog;
			}
		};
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Επιλέξτε το όνομα του αρχείου pdf");
		fileChooser.setSelectedFile(new File(filename));

		int userSelection = fileChooser.showSaveDialog(null);

		// If user doesn't click Save, do nothing
		if (userSelection != JFileChooser.APPROVE_OPTION) {return;}

		// Path of chosen file
		File fileToSave = fileChooser.getSelectedFile();
		String path = fileToSave.getAbsolutePath();

		// File replace confirmation
		if (fileToSave.exists())
		{
			int input = JOptionPane.showConfirmDialog(null,
				"Το αρχείο θα αντικατασταθεί. Θέλετε να συνεχίσετε;",
				"Επιβεβαίωση αντικατάστασης", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
			if (input != 0) {return;}
		}

		// Call the function to create the pdf with the statistics for the given date range
		try
		{
			createPdf(searchDate1, searchDate2, path, basename);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * Action of the buttonClose.
	 * @param evt 
	 */
	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}


	// Constructor
	public WindowShowStats()
	{
		// Initialize the chart panels
		chartTop5Numbers = new ChartPanel(createChart(null, "Πέντε πιο συχνά εμφανιζόμενοι αριθμοί", "Αριθμοί", "Πλήθος εμφανίσεων"));
		chartTop5Bonus = new ChartPanel(createChart(null, "Πέντε πιο συχνά εμφανιζόμενοι αριθμοί Τζόκερ", "Αριθμοί Τζόκερ", "Πλήθος εμφανίσεων"));
		chartAvgDistributed = new ChartPanel(createChart(null, "Μέσος όρος κερδών ανά κατηγορία", "Κατηρορίες", "Διανεμημένα κέρδη"));
		// Icons list
		final List<Image> icons = new ArrayList<>();
		try
		{
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_16.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_20.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_24.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_28.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_32.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_40.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_48.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_56.png")));
			icons.add(ImageIO.read(getClass().getResource("/resources/icon_64.png")));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}


		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


		/*
		 * Top panel with the window title
		 */
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 18, 0));
		topPanel.setLayout(new FlowLayout(1, 0, 0));
		topPanel.setBackground(backColor);

			JPanel titlePanel = new JPanel();
			titlePanel.setLayout(new OverlayLayout(titlePanel));
			titlePanel.setBackground(backColor);

				// Labels with the title
				JLabel labelTitle = new JLabel("Προβολή στατιστικών");
				labelTitle.setFont(new Font(null, 3, 42));
				labelTitle.setForeground(Color.ORANGE);

				JLabel labelTitleShadow = new JLabel("Προβολή στατιστικών");
				labelTitleShadow.setBorder(BorderFactory.createEmptyBorder(5, 1, 0, 0));
				labelTitleShadow.setFont(new Font(null, 3, 42));
				labelTitleShadow.setForeground(Color.BLUE);

			titlePanel.add(labelTitle);
			titlePanel.add(labelTitleShadow);

		topPanel.add(titlePanel);


		/*
		 * Middle panel with all the functionality
		 */
		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.setBackground(backColor);

			// Game selection & fetch data panel
			JPanel gameSelectAndFetchPanel = new JPanel();
			gameSelectAndFetchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			gameSelectAndFetchPanel.setLayout(new BoxLayout(gameSelectAndFetchPanel, BoxLayout.X_AXIS));
			gameSelectAndFetchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameSelectAndFetchPanel.getMinimumSize().height));
			gameSelectAndFetchPanel.setBackground(backColor);

				// Label game select
				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");

				// ComboBox game select
				String opapGames[] = {"Τζόκερ"};
				comboBoxGameSelect = new JComboBox(opapGames);
				comboBoxGameSelect.setMaximumSize(new Dimension(comboBoxGameSelect.getMinimumSize().width, comboBoxGameSelect.getMinimumSize().height));
				comboBoxGameSelect.addActionListener(this::comboBoxGameSelectActionPerformed);
				comboBoxGameSelect.setBackground(backColor);

				// Label from
				JLabel labelFrom = new JLabel("Εύρος ημερομηνιών    Από");
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

				// Text field for date 1
				textFieldDate1 = new JTextField("2000-01-01");
				textFieldDate1.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDate1.setMaximumSize(new Dimension(74, 20));

				// Label up to
				JLabel labelUpTo = new JLabel("Έως");
				labelUpTo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));

				// Text field for date 2
				textFieldDate2 = new JTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
				textFieldDate2.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDate2.setMaximumSize(new Dimension(74, 20));

				// ComboBox quick select
				String dateRanges[] = {"Τελευταία εβδομάδα", "Τελευταίος μήνας",
					"Τελευταίο 3μηνο", "Τελευταίο έτος", "Τελευταία 5ετία",
					"Πρώτο έτος", "Πρώτη 5ετία", "Όλες οι κληρώσεις"};
				comboBoxPredefinedRange = new JComboBox(dateRanges);
				comboBoxPredefinedRange.setMaximumSize(new Dimension(comboBoxPredefinedRange.getMinimumSize().width, comboBoxPredefinedRange.getMinimumSize().height));
				comboBoxPredefinedRange.addActionListener(this::comboBoxPredefinedRangeActionPerformed);
				comboBoxPredefinedRange.setSelectedIndex(0);
				comboBoxPredefinedRange.setBackground(backColor);

				// Button fetch data
				JButton buttonFetchData = new JButton("Υπολογισμός στατιστικών");
				buttonFetchData.addActionListener(this::buttonFetchDataActionPerformed);

			gameSelectAndFetchPanel.add(labelGameSelect);
			gameSelectAndFetchPanel.add(Box.createRigidArea(new Dimension(10,0)));
			gameSelectAndFetchPanel.add(comboBoxGameSelect);
			gameSelectAndFetchPanel.add(Box.createRigidArea(new Dimension(50,0)));
			gameSelectAndFetchPanel.add(labelFrom);
			gameSelectAndFetchPanel.add(textFieldDate1);
			gameSelectAndFetchPanel.add(labelUpTo);
			gameSelectAndFetchPanel.add(textFieldDate2);
			gameSelectAndFetchPanel.add(Box.createRigidArea(new Dimension(10,0)));
			gameSelectAndFetchPanel.add(comboBoxPredefinedRange);
			gameSelectAndFetchPanel.add(Box.createHorizontalGlue());
			gameSelectAndFetchPanel.add(buttonFetchData);


			// Data view panel
			viewPanelCards = new JPanel();    // CardLayout: Tables / Graph1 / Graph2 / Graph3
			viewPanelCards.setLayout(new CardLayout());
			viewPanelCards.setBackground(backColor);

				// Numbers tables panel
				JPanel numbersTablesPanel = new JPanel();
				numbersTablesPanel.setLayout(new BoxLayout(numbersTablesPanel, BoxLayout.X_AXIS));
				numbersTablesPanel.setBackground(backColor);

					// Columns and initial data of the JTable for numbers stats
					String[] numbersColumns = {"Αριθμός", "Εμφανίσεις", "Καθυστέρηση εμφάνισης"};
					Object[][] numbersData = {
						{"1", "", ""},
						{"2", "", ""},
						{"3", "", ""},
						{"4", "", ""},
						{"5", "", ""},
						{"6", "", ""},
						{"7", "", ""},
						{"8", "", ""},
						{"9", "", ""},
						{"10", "", ""},
						{"11", "", ""},
						{"12", "", ""},
						{"13", "", ""},
						{"14", "", ""},
						{"15", "", ""},
						{"16", "", ""},
						{"17", "", ""},
						{"18", "", ""},
						{"19", "", ""},
						{"20", "", ""},
						{"21", "", ""},
						{"22", "", ""},
						{"23", "", ""},
						{"24", "", ""},
						{"25", "", ""},
						{"26", "", ""},
						{"27", "", ""},
						{"28", "", ""},
						{"29", "", ""},
						{"30", "", ""},
						{"31", "", ""},
						{"32", "", ""},
						{"33", "", ""},
						{"34", "", ""},
						{"35", "", ""},
						{"36", "", ""},
						{"37", "", ""},
						{"38", "", ""},
						{"39", "", ""},
						{"40", "", ""},
						{"41", "", ""},
						{"42", "", ""},
						{"43", "", ""},
						{"44", "", ""},
						{"45", "", ""},
					};

					// Center renderer for table columns
					DefaultTableCellRenderer centerText = new DefaultTableCellRenderer();
					centerText.setHorizontalAlignment(SwingConstants.CENTER);

					// JTable for numbers stats
					numbersStatsTable = new JTable(numbersData, numbersColumns);
					numbersStatsTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
					numbersStatsTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
					numbersStatsTable.getColumnModel().getColumn(2).setCellRenderer(centerText);

					// Make table cells unselectable and uneditable
					numbersStatsTable.setEnabled(false);

					// Disable table column re-ordering
					numbersStatsTable.getTableHeader().setReorderingAllowed(false);

					// Make table fill the entire Viewport height
					numbersStatsTable.setFillsViewportHeight(true);

					// Scrollpane for the jokerDRTable
					JScrollPane spNumbers = new JScrollPane(numbersStatsTable);
					spNumbers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


					// Columns and initial data of the JTable for numbers stats
					String[] bonusNumbersColumns = {"Τζόκερ", "Εμφανίσεις", "Καθυστέρηση εμφάνισης"};
					Object[][] bonusNumbersData = {
						{"1", "", ""},
						{"2", "", ""},
						{"3", "", ""},
						{"4", "", ""},
						{"5", "", ""},
						{"6", "", ""},
						{"7", "", ""},
						{"8", "", ""},
						{"9", "", ""},
						{"10", "", ""},
						{"11", "", ""},
						{"12", "", ""},
						{"13", "", ""},
						{"14", "", ""},
						{"15", "", ""},
						{"16", "", ""},
						{"17", "", ""},
						{"18", "", ""},
						{"19", "", ""},
						{"20", "", ""}
					};

					// JTable for numbers stats
					bonusNumbersStatsTable = new JTable(bonusNumbersData, bonusNumbersColumns);
					bonusNumbersStatsTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
					bonusNumbersStatsTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
					bonusNumbersStatsTable.getColumnModel().getColumn(2).setCellRenderer(centerText);

					// Make table cells unselectable and uneditable
					bonusNumbersStatsTable.setEnabled(false);

					// Disable table column re-ordering
					bonusNumbersStatsTable.getTableHeader().setReorderingAllowed(false);

					// Make table fill the entire Viewport height
					bonusNumbersStatsTable.setFillsViewportHeight(true);

					// Scrollpane for the jokerDRTable
					JScrollPane spBonusNumbers = new JScrollPane(bonusNumbersStatsTable);
					spBonusNumbers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

				numbersTablesPanel.add(spNumbers);
				numbersTablesPanel.add(Box.createRigidArea(new Dimension(20,0)));
				numbersTablesPanel.add(spBonusNumbers);

				// Chart 1 panel
				chart1Panel = new JPanel();
				chart1Panel.setLayout(new GridLayout(1, 1));
				chart1Panel.setBackground(backColor);
				chart1Panel.add(chartTop5Numbers);


				// Chart 2 panel
				chart2Panel = new JPanel();
				chart2Panel.setLayout(new GridLayout(1, 1));
				chart2Panel.setBackground(backColor);
				chart2Panel.add(chartTop5Bonus);


				// Chart 3 panel
				chart3Panel = new JPanel();
				chart3Panel.setLayout(new GridLayout(1, 1));
				chart3Panel.setBackground(backColor);
				chart3Panel.add(chartAvgDistributed);


			viewPanelCards.add(numbersTablesPanel, "numbersTables");
			viewPanelCards.add(chart1Panel, "chart1");
			viewPanelCards.add(chart2Panel, "chart2");
			viewPanelCards.add(chart3Panel, "chart3");


			// View options panel
			JPanel viewSelectionPanel = new JPanel();
			viewSelectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			viewSelectionPanel.setLayout(new BoxLayout(viewSelectionPanel, BoxLayout.X_AXIS));
			viewSelectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, viewSelectionPanel.getMinimumSize().height));
			viewSelectionPanel.setBackground(backColor);

				// Label select view
				JLabel labelSelectView = new JLabel("Επιλέξτε προβολή");

				// ComboBox select view
				String viewSelections[] = {"Πίνακες τυχερών αριθμών",
					"Γράφημα 1: Πέντε πιο συχνά εμφανιζόμενοι αριθμοί",
					"Γράφημα 2: Πέντε πιο συχνά εμφανιζόμενοι αριθμοί Τζόκερ",
					"Γράφημα 3: Μέσος όρος κερδών ανά κατηγορία"};
				comboBoxSelectView = new JComboBox(viewSelections);
				comboBoxSelectView.addActionListener(this::comboBoxSelectViewActionPerformed);
				comboBoxSelectView.setMaximumSize(new Dimension(comboBoxSelectView.getMinimumSize().width, comboBoxSelectView.getMinimumSize().height));
				comboBoxSelectView.setSelectedIndex(0);
				comboBoxSelectView.setBackground(backColor);

				// Button export to pdf
				JButton buttonExportToPdf = new JButton("Εξαγωγή στατιστικών σε αρχείο pdf");
				buttonExportToPdf.addActionListener((evt) ->
				{
					CompletableFuture.runAsync(() -> this.buttonExportToPdfActionPerformed(evt));
				});

			viewSelectionPanel.add(labelSelectView);
			viewSelectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
			viewSelectionPanel.add(comboBoxSelectView);
			viewSelectionPanel.add(Box.createHorizontalGlue());
			viewSelectionPanel.add(buttonExportToPdf);

		middlePanel.add(gameSelectAndFetchPanel);
		middlePanel.add(Box.createRigidArea(new Dimension(0,14)));
		middlePanel.add(viewPanelCards);
		middlePanel.add(Box.createRigidArea(new Dimension(0,14)));
		middlePanel.add(viewSelectionPanel);
		middlePanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);


		/*
		 * Bottom panel with the Close button
		 */
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);

			// Close button
			JButton buttonClose = new JButton("Κλείσιμο");
			buttonClose.setPreferredSize(new Dimension(116, 26));
			buttonClose.addActionListener(this::buttonCloseActionPerformed);

		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonClose);


		/*
		 * Main panel
		 */
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new Dimension(1150, 614));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(bottomPanel);


		/*
		 * Main window
		 */
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Show stats");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(1160, 644));
		dialog.setResizable(false);
		dialog.setIconImages(icons);

		// Find firstDrawDate
		findDateOfFirstDraw();

		dialog.setVisible(true);
	}
}