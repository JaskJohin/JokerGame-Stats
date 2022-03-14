package view;

import POJOs.Content;
import POJOs.ContentPK;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.Leading;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import model.AddDataController;
import model.QueriesSQL;
import static view.Helper.getJsonStrFromApiURL;
import static view.Helper.getUrlStrForDateRange;
import static view.Helper.jokerJsonSingleDrawToObject;


/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowShowData
{
	// Variables declaration
	private String lastDrawDate;
	private final JDialog dialog;
	private final JComboBox comboBoxGameSelect;
	private final JComboBox comboBoxYearSelect;
	private final JRadioButton radioButtonApi;
	private final JTable dataViewTable;


	// Methods
	/**
	 * Populates comboBoxYearSelect with the years the selected game is active.
	 * This method is called when the window is first opened and every time a different
	 * game is selected.
	 */
	private void populateComboBoxYearSelect()
	{
		// Remove all items from comboBoxYearSelect
		comboBoxYearSelect.removeAllItems();

		// Current local date
		int yearNow = LocalDate.now().getYear();

		// First year of the selected game
		int firstYear = 1999;
		switch (comboBoxGameSelect.getSelectedItem().toString())
		{
			case "Κίνο":      firstYear = yearNow-2; break;
			case "Powerspin": firstYear = 2020; break;
			case "Super3":    firstYear = 2002; break;
			case "Πρότο":     firstYear = 2000; break;
			case "Λόττο":     firstYear = 2000; break;
			case "Τζόκερ":    firstYear = 2000; break;
			case "Extra5":    firstYear = 2002; break;
		}

		// Populate combobox
		for (int i = yearNow; i >= firstYear; i--)
		{
			comboBoxYearSelect.addItem(i);
		}
	}


	/** 
	 * Uses the API "https://api.opap.gr/draws/v3.0/{gameId}/last-result-and-active" to
	 * find the date of the latest draw of the selected game and stores it in the variable
	 * lastDrawDate. This method is called when the window is first opened and every time
	 * a different game is selected.
	 */
	private void findLastDrawDate()
	{
		// Get selected game id
		String gId = null;

		switch (comboBoxGameSelect.getSelectedItem().toString())
		{
			case "Κίνο":      gId = "1100"; break;
			case "Powerspin": gId = "1110"; break;
			case "Super3":    gId = "2100"; break;
			case "Πρότο":     gId = "2101"; break;
			case "Λόττο":     gId = "5103"; break;
			case "Τζόκερ":    gId = "5104"; break;
			case "Extra5":    gId = "5106"; break;
		}


		// URL string
		String urlStr = "https://api.opap.gr/draws/v3.0/"+gId+"/last-result-and-active";

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try
		{
			// Get json string from the API
			String jsonStr = getJsonStrFromApiURL(urlStr);

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Get the last draw object
			JsonObject lastDraw = jObject.getAsJsonObject("last");

			// Get the drawTime
			Long drawTime = lastDraw.get("drawTime").getAsLong();
			LocalDateTime ldt = Instant.ofEpochMilli(drawTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
			lastDrawDate = formatter.format(ldt);
		}
		catch (Exception ex) { /* Silently continue */ }
	}


	/**
	 * Gather data for the selected game and year using the api.
	 */
	private void getDataFromApi(String gameId, String year)
	{
		// Variables
		String date1;
		String date2;
		List<String> urlStrList = new ArrayList<>();  // List with all the url we'll call
		List<String> jsonStrList = new ArrayList<>();  // List with the json strings
		BigDecimal bd;   // BigDecimal - used for rounding
		int drawCount;
		double moneySum;
		int jackpotCount;

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


		// Create the 12 date pairs and the urlStrList
		date1 = year + "-01-01";
		date2 = year + "-01-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-02-01";
		date2 = formatter.format(LocalDate.parse(date1, formatter).plusMonths(1).minusDays(1));
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-03-01";
		date2 = year + "-03-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-04-01";
		date2 = year + "-04-30";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-05-01";
		date2 = year + "-05-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-06-01";
		date2 = year + "-06-30";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-07-01";
		date2 = year + "-07-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-08-01";
		date2 = year + "-08-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-09-01";
		date2 = year + "-09-30";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-10-01";
		date2 = year + "-10-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-11-01";
		date2 = year + "-11-30";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");
		date1 = year + "-12-01";
		date2 = year + "-12-31";
		urlStrList.add("https://api.opap.gr/draws/v3.0/"+gameId+"/draw-date/"+date1+"/"+date2+"/?limit=180");

		// Number of threads (simultaneous calls to the API)
		int threadNum = 12;

		// --- Create the threads to do the job ---
		GetJsonStrListFromUrlStrListMT[] threadArray = new GetJsonStrListFromUrlStrListMT[threadNum];
		int taskNum = urlStrList.size();    // Total number of tasks to do
		for (int i=0; i<threadNum; i++)
		{
			int index1 = i*taskNum/threadNum;      // Each thread does index2-index1
			int index2 = (i+1)*taskNum/threadNum;  // tasks, from index1 to index2-1
			threadArray[i] = new GetJsonStrListFromUrlStrListMT(index1, index2, urlStrList);
			threadArray[i].start();
		}

		// Wait for all threads to finish
		for (int i=0; i<threadNum; i++)
		{
			try
			{
				threadArray[i].join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}

		// Merge results from all threads
		for (GetJsonStrListFromUrlStrListMT thread : threadArray)
		{
			// Merge jsonStrList
			thread.getJsonStrList().forEach((jsonStr) ->
			{
				jsonStrList.add(jsonStr);
			});
		}

		// Parce all json strings in jsonStrList
		for (int i = 0; i < jsonStrList.size(); i++)
		{
			// Set the counters and sum to 0
			drawCount = 0;
			moneySum = 0;
			jackpotCount = 0;

			// Json string
			String jsonStr = jsonStrList.get(i);

			// First empty the table cells for this month.
			// They'll remain empty if json has no draw data.
			dataViewTable.setValueAt("", i, 1);
			dataViewTable.setValueAt("", i, 2);
			dataViewTable.setValueAt("", i, 3);

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Get the "content" json array
			JsonArray content = jObject.getAsJsonArray("content");

			for (JsonElement drawElement : content)
			{
				// Get the json object from this content json element
				JsonObject drawObject = drawElement.getAsJsonObject();

				// Create a JokerDrawData object from the json object
				JokerDrawData jokerDraw = jokerJsonSingleDrawToObject(drawObject);

				// Update the counters and sum
				drawCount++;
				moneySum += jokerDraw.getPrizeTier5_1dividend() + jokerDraw.getPrizeTier5dividend() +
					jokerDraw.getPrizeTier4_1dividend() + jokerDraw.getPrizeTier4dividend() +
					jokerDraw.getPrizeTier3_1dividend() + jokerDraw.getPrizeTier3dividend() +
					jokerDraw.getPrizeTier2_1dividend() + jokerDraw.getPrizeTier1_1dividend();
				if (jokerDraw.getPrizeTier5_1winners() == 0) {jackpotCount++;}
			}

			// Round the moneySum and make it BigDecimal
			bd = BigDecimal.valueOf(moneySum);
			BigDecimal moneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

			// Put the data for this month to dataViewTable
			dataViewTable.setValueAt(drawCount, i, 1);
			dataViewTable.setValueAt(moneySumBD, i, 2);
			dataViewTable.setValueAt(jackpotCount, i, 3);
		}

		// Calculate the "total" row
		int totalDrawCount = 0;
		double totalMoneySum = 0;
		int totalJackpotCount = 0;

		for (int i = 0; i < 12; i++)
		{
			totalDrawCount += (int) dataViewTable.getValueAt(i, 1);
			totalMoneySum += ((BigDecimal) dataViewTable.getValueAt(i, 2)).doubleValue();
			totalJackpotCount += (int) dataViewTable.getValueAt(i, 3);
		}

		bd = BigDecimal.valueOf(totalMoneySum);
		BigDecimal totalMoneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

		dataViewTable.setValueAt(totalDrawCount, 12, 1);
		dataViewTable.setValueAt(totalMoneySumBD, 12, 2);
		dataViewTable.setValueAt(totalJackpotCount, 12, 3);
	}


	/**
	 * Gather data for the selected game and year using the DB.
	 */
	private void getDataFromDB(String gameId, String year) throws Exception	{

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

		for(int i = 0; i < 12; i++) {
			//variables declaration
			int drawCount;
			double moneySum;
			int jackpotCount;
			BigDecimal bd;
			String startDate;
			String endDate;

			// First empty the table cells for this month.
			dataViewTable.setValueAt("", i, 1);
			dataViewTable.setValueAt("", i, 2);
			dataViewTable.setValueAt("", i, 3);

			//if i+1 value has one digit
			if((i + 1) < 10)
				startDate = year + "-0" + (i+1) + "-01";
			else //if i+1 value has two digits
				startDate = year + "-" + (i+1) + "-01";

			//create a Content object to be used for chacking if record exists in database
			Content content = new Content();
			JsonObject singleDrawObj;
			ContentPK contentPK = new ContentPK();

			//set startign date (1st day of the month
			LocalDate fromDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			//set finishing day (last day of the month also checking if year is a leap year)
			LocalDate toDate = fromDate.withDayOfMonth(fromDate.getMonth().length(fromDate.isLeapYear()));

			//convert both dates to String
			endDate = toDate.toString();

			//get the numbers of draws for current month from API
			JsonArray monthlyDraws = model.Utilities.GET_API_ARRAY("https://api.opap.gr/draws/v3.0/5104/draw-date/" + startDate + "/" + endDate + "/draw-id");

			//loop to check if record exists in the database
			for(int j = 0; j < monthlyDraws.size(); j++) {
				contentPK.setDrawid(monthlyDraws.get(j).getAsInt());
				contentPK.setGameid(Integer.parseInt(gameId));
				content.setContentPK(contentPK);
				boolean control = QueriesSQL.checkIfRecordExists(content);

				//if it doesn't exists, then add it so that presented statistical data are accurate
				if(!control) {
					singleDrawObj = model.Utilities.GET_API("https://api.opap.gr/draws/v3.0/" + gameId + "/" + content.getContentPK().getDrawid());
					AddDataController.storeDrawsDataByDrawID(singleDrawObj);
				}
			}

			//set values for number of games, total earnings and number of jackpots
			drawCount = QueriesSQL.countMonthlyGames(startDate, endDate);
			moneySum = QueriesSQL.sumMonthlyDivident(startDate, endDate);
			jackpotCount = QueriesSQL.countJackpots(startDate, endDate);

			//convert monyeSum to big decimal to format the number of decimal points shown
			bd = BigDecimal.valueOf(moneySum);
			BigDecimal moneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

			// Put the data for this month to dataViewTable
			dataViewTable.setValueAt(drawCount, i, 1);
			dataViewTable.setValueAt(moneySumBD, i, 2);
			dataViewTable.setValueAt(jackpotCount, i, 3);
		}

		// Calculate the "total" row
		int totalDrawCount = 0;
		double totalMoneySum = 0;
		int totalJackpotCount = 0;

		for (int i = 0; i < 12; i++)
		{
			totalDrawCount += (int) dataViewTable.getValueAt(i, 1);
			totalMoneySum += ((BigDecimal) dataViewTable.getValueAt(i, 2)).doubleValue();
			totalJackpotCount += (int) dataViewTable.getValueAt(i, 3);
		}

		BigDecimal bd = BigDecimal.valueOf(totalMoneySum);
		BigDecimal totalMoneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

		dataViewTable.setValueAt(totalDrawCount, 12, 1);
		dataViewTable.setValueAt(totalMoneySumBD, 12, 2);
		dataViewTable.setValueAt(totalJackpotCount, 12, 3);
	}


	// Button actions
	/**
	 * Action of the comboBoxGameSelect.
	 * @param evt 
	 */
	private void comboBoxGameSelectActionPerformed(java.awt.event.ActionEvent evt)
	{
		populateComboBoxYearSelect();
		CompletableFuture.runAsync(() -> findLastDrawDate());  // Run asynchronously
	}


	/**
	 * Action of the buttonDownload.
	 * @param evt 
	 */
	private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt)
	{
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

		// Get selected year
		String year = comboBoxYearSelect.getSelectedItem().toString();

		// Get the data
		if (radioButtonApi.isSelected())
		{
			getDataFromApi(gameId, year);
		}
		else
		{
			try {
				getDataFromDB(gameId, year);
			} catch (Exception ex) {
				Logger.getLogger(WindowShowData.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}


	/**
	 * Action of the buttonExportToPdf.
	 * @param evt 
	 */
	private void buttonExportToPdfActionPerformed(java.awt.event.ActionEvent evt)
	{
		// If lastDrawDate remains null, there's a connection error. So, show appropriate message.
		if (lastDrawDate == null) {findLastDrawDate();}
		if (lastDrawDate == null)
		{
			String message = "Σφάλμα σύνδεσης στο API του ΟΠΑΠ.";
			JOptionPane.showMessageDialog(null, message, "Σφάλμα σύνδεσης", 0);
			return;
		}

		// Create file name
		String basename = "Συγκεντρωτικά δεδομένα " + comboBoxGameSelect.getSelectedItem().toString() + " έως " + lastDrawDate;
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


		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Get selected game id and find the date of the first draw
		String gameId = null;
		String date1 = "2000-01-01";
		switch (comboBoxGameSelect.getSelectedItem().toString())
		{
			case "Κίνο":      gameId = "1100"; date1 = formatter.format(LocalDate.now().minusYears(3)); break;
			case "Powerspin": gameId = "1110"; date1 = "2020-06-30"; break;
			case "Super3":    gameId = "2100"; date1 = "2002-11-25"; break;
			case "Πρότο":     gameId = "2101"; date1 = "2000-01-01"; break;
			case "Λόττο":     gameId = "5103"; date1 = "2000-01-01"; break;
			case "Τζόκερ":    gameId = "5104"; date1 = "2000-01-01"; break;
			case "Extra5":    gameId = "5106"; date1 = "2002-11-25"; break;
		}


		// Variables
		int maxSimConnections = 50;  // Max number of simultaneous calls to the API
		int threadNum;               // Number of threads (simultaneous calls to the API)
		List<String> urlStrList;     // List with all the urls we will call
		List<String> jsonStrList = new ArrayList<>();  // List with the json strings
		List<JokerDrawData> JokerDrawDataList = new ArrayList<>();  // List with all the draw data


		// List with the API urls
		urlStrList = getUrlStrForDateRange(Integer.parseInt(gameId), date1, lastDrawDate);


		// Set number of threads = urlStrList.size(), but not more that maxSimConnections
		threadNum = urlStrList.size();
		if (threadNum > maxSimConnections) {threadNum = maxSimConnections;}

		// --- Create the threads to do the job ---
		GetJsonStrListFromUrlStrListMT[] threadArray = new GetJsonStrListFromUrlStrListMT[threadNum];
		int taskNum = urlStrList.size();    // Total number of tasks to do
		for (int i=0; i<threadNum; i++)
		{
			int index1 = i*taskNum/threadNum;      // Each thread does index2-index1
			int index2 = (i+1)*taskNum/threadNum;  // tasks, from index1 to index2-1
			threadArray[i] = new GetJsonStrListFromUrlStrListMT(index1, index2, urlStrList);
			threadArray[i].start();
		}

		// Wait for all threads to finish
		for (int i=0; i<threadNum; i++)
		{
			try
			{
				threadArray[i].join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}

		// Merge results from all threads
		for (GetJsonStrListFromUrlStrListMT thread : threadArray)
		{
			// Merge jsonStrList
			thread.getJsonStrList().forEach((jsonStr) ->
			{
				jsonStrList.add(jsonStr);
			});
		}

		// Parce all json strings in jsonStrList
		for (int i = jsonStrList.size()-1; i >= 0; i--)
		{
			String jsonStr = jsonStrList.get(i);

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Get the totalElements
			int totalElements = jObject.get("totalElements").getAsInt();

			// If there are no draw data, go to the next jsonStrList element
			if (totalElements == 0) {continue;}

			// Get the "content" json array
			JsonArray content = jObject.getAsJsonArray("content");

			for (JsonElement drawElement : content)
			{
				// Get the json object from this content json element
				JsonObject drawObject = drawElement.getAsJsonObject();

				// Create a JokerDrawData object from the json object
				JokerDrawData jokerDraw = jokerJsonSingleDrawToObject(drawObject);
				
				// Add to the list
				JokerDrawDataList.add(jokerDraw);
			}
		}


		// Initialize PDFwriter
		PdfWriter pdfWriter;
		try
		{
			pdfWriter = new PdfWriter(path);
		}
		catch (FileNotFoundException ex)
		{
			String message = "Σφάλμα δημιουργίας αρχείου.\nΒεβαιωθείτε πως έχετε δικαίωμα εγγραφής στο φάκελο.";
			JOptionPane.showMessageDialog(null, message, "Σφάλμα εγγραφής", 0);
			return;
		}

		//Initialize PDF document
		PdfDocument pdf = new PdfDocument(pdfWriter);

		// Initialize document
		Document document = new Document(pdf, PageSize.A4);
		document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1.0f));
		document.setMargins(23, 36, 10, 36); // (top, right, bottom, left)


		// Create PdfFonts to be used in the pdf document
		PdfFont fontRegular = null;
		PdfFont fontBold = null;
		try
		{
			fontRegular = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-Regular.ttf").toString());
			fontBold = PdfFontFactory.createFont(getClass().getResource("/resources/Roboto-Bold.ttf").toString());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			try
			{
				fontRegular = PdfFontFactory.createFont("Helvetica", "Cp1253");
				fontBold = PdfFontFactory.createFont("Helvetica-Bold", "Cp1253");
			}
			catch (Exception ex1) {System.err.println(ex1);}
		}


		// Variables
		BigDecimal bd;   // BigDecimal - used for rounding
		int drawCount = 0;
		double moneySum = 0;
		int jackpotCount = 0;
		int drawCountTotal = 0;
		double moneySumTotal = 0;
		int jackpotCountTotal = 0;
		int tableCounter = 0;  // Used for adding a page break every 3 tables
		JokerDrawData jokerDraw = JokerDrawDataList.get(JokerDrawDataList.size()-1);
		String drawDate = jokerDraw.getDrawDate();
		String year = drawDate.substring(0, 4);
		String prevYear = drawDate.substring(0, 4);
		String month = drawDate.substring(5, 7);
		String prevMonth = drawDate.substring(5, 7);

		// Add a title to the document
		Paragraph par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-14.8f).setMarginBottom(-1f);
		par.add(new Text(basename).setFont(fontBold).setFontSize(19));
		document.add(par);

		// Add the first year to the document
		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(4f).setMarginBottom(2f);
		par.add(new Text(year).setFont(fontBold).setFontSize(14));
		document.add(par);

		// First table
		Table table = new Table(UnitValue.createPercentArray(new float[]{50f, 70f, 100f, 40f}));
		table.useAllAvailableWidth();

		// Column 1 header
		Paragraph header1 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		header1.setMarginTop(0f).setMarginBottom(0f);
		header1.add(new Text("Μήνας").setFont(fontBold).setFontSize(12));
		table.addHeaderCell(header1);

		// Column 2 header
		Paragraph header2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		header2.setMarginTop(0f).setMarginBottom(0f);
		header2.add(new Text("Πλήθος κληρώσεων").setFont(fontBold).setFontSize(12));
		table.addHeaderCell(header2);

		// Column 3 header
		Paragraph header3 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		header3.setMarginTop(0f).setMarginBottom(0f);
		header3.add(new Text("Χρήματα που διανεμήθηκαν (€)").setFont(fontBold).setFontSize(12));
		table.addHeaderCell(header3);

		// Column 4 header
		Paragraph header4 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		header4.setMarginTop(0f).setMarginBottom(0f);
		header4.add(new Text("Τζακ-ποτ").setFont(fontBold).setFontSize(12));
		table.addHeaderCell(header4);


		// Start the counters and sum
		drawCount++;
		moneySum += jokerDraw.getPrizeTier5_1dividend() + jokerDraw.getPrizeTier5dividend() +
			jokerDraw.getPrizeTier4_1dividend() + jokerDraw.getPrizeTier4dividend() +
			jokerDraw.getPrizeTier3_1dividend() + jokerDraw.getPrizeTier3dividend() +
			jokerDraw.getPrizeTier2_1dividend() + jokerDraw.getPrizeTier1_1dividend();
		if (jokerDraw.getPrizeTier5_1winners() == 0) {jackpotCount++;}

		for (int i = JokerDrawDataList.size()-2; i >= 0; i--)
		{
			jokerDraw = JokerDrawDataList.get(i);

			drawDate = jokerDraw.getDrawDate();
			year = drawDate.substring(0, 4);
			month = drawDate.substring(5, 7);


			// If month has changed
			if (!month.equals(prevMonth))
			{
				// Month name
				String monthName = null;
				switch (prevMonth)
				{
					case "01": monthName = "Ιανουάριος"; break;
					case "02": monthName = "Φεβρουάριος"; break;
					case "03": monthName = "Μάρτιος"; break;
					case "04": monthName = "Απρίλιος"; break;
					case "05": monthName = "Μάιος"; break;
					case "06": monthName = "Ιούνιος"; break;
					case "07": monthName = "Ιούλιος"; break;
					case "08": monthName = "Αύγουστος"; break;
					case "09": monthName = "Σεπτέμβριος"; break;
					case "10": monthName = "Οκτώβριος"; break;
					case "11": monthName = "Νοέμβριος"; break;
					case "12": monthName = "Δεκέμβριος"; break;
				}

				// Round the moneySum and make it BigDecimal
				bd = BigDecimal.valueOf(moneySum);
				BigDecimal moneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

				// Add the data to the table
				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(monthName).setFont(fontRegular).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(drawCount)).setFont(fontRegular).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(moneySumBD)).setFont(fontRegular).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(jackpotCount)).setFont(fontRegular).setFontSize(11));
				table.addCell(par);

				// Add values to "Total" variables
				drawCountTotal += drawCount;
				moneySumTotal += moneySum;
				jackpotCountTotal += jackpotCount;

				// Reset the counters
				drawCount = 0;
				moneySum = 0;
				jackpotCount = 0;
			}

			// If year has changed
			if (!year.equals(prevYear))
			{
				// Round the moneySumTotal and make it BigDecimal
				bd = BigDecimal.valueOf(moneySumTotal);
				BigDecimal moneySumTotalBD = bd.setScale(2, RoundingMode.HALF_UP);

				// Add "Total" row to the table
				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text("Σύνολο").setFont(fontBold).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(drawCountTotal)).setFont(fontBold).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(moneySumTotalBD)).setFont(fontBold).setFontSize(11));
				table.addCell(par);

				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
				par.add(new Text(String.valueOf(jackpotCountTotal)).setFont(fontBold).setFontSize(11));
				table.addCell(par);

				// Add last table
				document.add(table);

				// Reset the "Total" counters
				drawCountTotal = 0;
				moneySumTotal = 0;
				jackpotCountTotal = 0;

				// Change page every 3 tables
				tableCounter++;
				if (tableCounter == 3)
				{
					document.add(new AreaBreak());
					tableCounter = 0;
				}

				// Add new year
				par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
				par.setMarginTop(11f).setMarginBottom(2f);
				par.add(new Text(year).setFont(fontBold).setFontSize(14));
				document.add(par);

				// Create table for the new year
				table = new Table(UnitValue.createPercentArray(new float[]{50f, 70f, 100f, 40f}));
				table.useAllAvailableWidth();
				table.addHeaderCell(header1);
				table.addHeaderCell(header2);
				table.addHeaderCell(header3);
				table.addHeaderCell(header4);
			}

			// Update the counters and sum
			drawCount++;
			moneySum += jokerDraw.getPrizeTier5_1dividend() + jokerDraw.getPrizeTier5dividend() +
				jokerDraw.getPrizeTier4_1dividend() + jokerDraw.getPrizeTier4dividend() +
				jokerDraw.getPrizeTier3_1dividend() + jokerDraw.getPrizeTier3dividend() +
				jokerDraw.getPrizeTier2_1dividend() + jokerDraw.getPrizeTier1_1dividend();
			if (jokerDraw.getPrizeTier5_1winners() == 0) {jackpotCount++;}

			prevYear = drawDate.substring(0, 4);
			prevMonth = drawDate.substring(5, 7);
		}

		// Month name
		String monthName = null;
		switch (prevMonth)
		{
			case "01": monthName = "Ιανουάριος"; break;
			case "02": monthName = "Φεβρουάριος"; break;
			case "03": monthName = "Μάρτιος"; break;
			case "04": monthName = "Απρίλιος"; break;
			case "05": monthName = "Μάιος"; break;
			case "06": monthName = "Ιούνιος"; break;
			case "07": monthName = "Ιούλιος"; break;
			case "08": monthName = "Αύγουστος"; break;
			case "09": monthName = "Σεπτέμβριος"; break;
			case "10": monthName = "Οκτώβριος"; break;
			case "11": monthName = "Νοέμβριος"; break;
			case "12": monthName = "Δεκέμβριος"; break;
		}

		// Round the moneySum and make it BigDecimal
		bd = BigDecimal.valueOf(moneySum);
		BigDecimal moneySumBD = bd.setScale(2, RoundingMode.HALF_UP);

		// Add the last data to the table
		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(monthName).setFont(fontRegular).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(drawCount)).setFont(fontRegular).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(moneySumBD)).setFont(fontRegular).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(jackpotCount)).setFont(fontRegular).setFontSize(11));
		table.addCell(par);

		// Add values to "Total" variables
		drawCountTotal += drawCount;
		moneySumTotal += moneySum;
		jackpotCountTotal += jackpotCount;

		// Round the moneySumTotal and make it BigDecimal
		bd = BigDecimal.valueOf(moneySumTotal);
		BigDecimal moneySumTotalBD = bd.setScale(2, RoundingMode.HALF_UP);

		// Add "Total" row to the table
		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text("Έως " + lastDrawDate).setFont(fontBold).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(drawCountTotal)).setFont(fontBold).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(moneySumTotalBD)).setFont(fontBold).setFontSize(11));
		table.addCell(par);

		par = new Paragraph().setTextAlignment(TextAlignment.CENTER);
		par.setMarginTop(-0.7f).setMarginBottom(-0.7f);
		par.add(new Text(String.valueOf(jackpotCountTotal)).setFont(fontBold).setFontSize(11));
		table.addCell(par);


		// Add the last table
		document.add(table);

		// Close document
		document.close();
		pdf.close();
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
	public WindowShowData()
	{
		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


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


		// Font
		Font fontRobotoRegular = null;
		try
		{
			fontRobotoRegular = Font.createFont(Font.PLAIN, getClass().getResourceAsStream("/resources/Roboto-Regular.ttf"));
		}
		catch (FontFormatException | IOException ex)
		{
			System.err.println(ex);
			fontRobotoRegular = new Font("Dialog", 0, 12);  // Fallback, not suppose to happen
		}


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
				JLabel labelTitle = new JLabel("Προβολή δεδομένων");
				labelTitle.setFont(new Font(null, 3, 42));
				labelTitle.setForeground(Color.ORANGE);

				JLabel labelTitleShadow = new JLabel("Προβολή δεδομένων");
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

			// Game selection & year selection panel
			JPanel gameSelectPanel = new JPanel();
			gameSelectPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			gameSelectPanel.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			gameSelectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameSelectPanel.getMinimumSize().height));
			gameSelectPanel.setBackground(backColor);

				// Label game select
				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");

				// ComboBox game select
				String opapGames[] = {"Τζόκερ"};
				comboBoxGameSelect = new JComboBox(opapGames);
				comboBoxGameSelect.addActionListener(this::comboBoxGameSelectActionPerformed);
				comboBoxGameSelect.setBackground(backColor);

				// Label year
				JLabel labelYear = new JLabel("Έτος");
				labelYear.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

				// ComboBox year select
				comboBoxYearSelect = new JComboBox();
				comboBoxYearSelect.setBackground(backColor);

			gameSelectPanel.add(labelGameSelect);
			gameSelectPanel.add(Box.createRigidArea(new Dimension(10,0)));
			gameSelectPanel.add(comboBoxGameSelect);
			gameSelectPanel.add(Box.createRigidArea(new Dimension(50,0)));
			gameSelectPanel.add(labelYear);
			gameSelectPanel.add(comboBoxYearSelect);
			gameSelectPanel.add(Box.createRigidArea(new Dimension(50,0)));


			// Choose search method label panel
			JPanel chooseMethodLabelPanel = new JPanel();
			chooseMethodLabelPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
			chooseMethodLabelPanel.setLayout(new FlowLayout(0, 0, 0));
			chooseMethodLabelPanel.setBackground(backColor);

				// Label choose method
				JLabel labelChooseMethod = new JLabel("Επιλέξτε από που θέλετε να αντληθούν τα δεδομένα");

			chooseMethodLabelPanel.add(labelChooseMethod);
			chooseMethodLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, chooseMethodLabelPanel.getMinimumSize().height));


			// Choose search method and download panel
			JPanel chooseMethodAndDLPanel = new JPanel();
			chooseMethodAndDLPanel.setLayout(new BoxLayout(chooseMethodAndDLPanel, BoxLayout.X_AXIS));
			chooseMethodAndDLPanel.setBackground(backColor);

				// Choose search method panel
				JPanel chooseMethodPanel = new JPanel();
				chooseMethodPanel.setLayout(new BoxLayout(chooseMethodPanel, BoxLayout.Y_AXIS));
				chooseMethodPanel.setBackground(backColor);

					// API method panel
					JPanel apiMethodPanel = new JPanel();
					apiMethodPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
					apiMethodPanel.setLayout(new FlowLayout(0, 0, 0));
					apiMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, apiMethodPanel.getMinimumSize().height));
					apiMethodPanel.setBackground(backColor);

						// Radio button for API method
						radioButtonApi = new JRadioButton();
						radioButtonApi.setText("API");
						radioButtonApi.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
						radioButtonApi.setBackground(backColor);
						radioButtonApi.setSelected(true);

						// Label API method info
						JLabel labelApiInfo = new JLabel("(Η βάση δεδομένων δε θα πειραχθεί)");
						labelApiInfo.setBorder(BorderFactory.createEmptyBorder(1, 107, 0, 0));
						labelApiInfo.setFont(fontRobotoRegular.deriveFont(0, 12));
						labelApiInfo.setForeground(Color.DARK_GRAY);

					apiMethodPanel.add(radioButtonApi);
					apiMethodPanel.add(labelApiInfo);
					apiMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, apiMethodPanel.getMinimumSize().height));


					// DB method panel
					JPanel DBMethodPanel = new JPanel();
					DBMethodPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
					DBMethodPanel.setLayout(new FlowLayout(0, 0, 0));
					DBMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, DBMethodPanel.getMinimumSize().height));
					DBMethodPanel.setBackground(backColor);

						// Radio button for DB method
						JRadioButton radioButtonDB = new JRadioButton();
						radioButtonDB.setText("Βάση δεδομένων");
						radioButtonDB.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
						radioButtonDB.setBackground(backColor);
						radioButtonDB.setSelected(false);

						// Group radio butttons
						ButtonGroup groupChooseMethod = new ButtonGroup();
						groupChooseMethod.add(radioButtonApi);
						groupChooseMethod.add(radioButtonDB);

						// Label DB method info
						JLabel labelDBInfo = new JLabel("(Αν τα δεδομένα δεν υπάρχουν ήδη, τότε θα προστεθούν στη βάση)");
						labelDBInfo.setBorder(BorderFactory.createEmptyBorder(1, 30, 0, 0));
						labelDBInfo.setFont(fontRobotoRegular.deriveFont(0, 12));
						labelDBInfo.setForeground(Color.DARK_GRAY);

					DBMethodPanel.add(radioButtonDB);
					DBMethodPanel.add(labelDBInfo);
					DBMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, DBMethodPanel.getMinimumSize().height));

				chooseMethodPanel.add(apiMethodPanel);
				chooseMethodPanel.add(DBMethodPanel);


				// Button download
				JButton buttonDownload = new JButton("Προβολή συγκεντρωτικών");
				buttonDownload.setPreferredSize(new Dimension(buttonDownload.getMinimumSize().width, 46));
				buttonDownload.setMaximumSize(new Dimension(buttonDownload.getMinimumSize().width, 46));
				buttonDownload.setText("<html><center>Προβολή συγκεντρωτικών<br>δεδομένων ανά μήνα</center></html>");
				buttonDownload.addActionListener((evt) ->
				{
					CompletableFuture.runAsync(() -> this.buttonDownloadActionPerformed(evt));
				});

			chooseMethodAndDLPanel.add(chooseMethodPanel);
			chooseMethodAndDLPanel.add(buttonDownload);


			// Data view panel
			JPanel dataViewPanel = new JPanel();
			dataViewPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			dataViewPanel.setLayout(new BorderLayout());
			dataViewPanel.setBackground(backColor);

				// Columns and initial data of the JTable for data per month
				String[] columns = {"Μήνας", "Πλήθος κληρώσεων",
					"Χρήματα που διανεμήθηκαν (€)", "Τζακ-ποτ"};
				Object[][] data = {
					{"Ιανουάριος", "", "", ""},
					{"Φεβρουάριος", "", "", ""},
					{"Μάρτιος", "", "", ""},
					{"Απρίλιος", "", "", ""},
					{"Μάιος", "", "", ""},
					{"Ιούνιος", "", "", ""},
					{"Ιούλιος", "", "", ""},
					{"Αύγουστος", "", "", ""},
					{"Σεπτέμβριος", "", "", ""},
					{"Οκτώβριος", "", "", ""},
					{"Νοέμβριος", "", "", ""},
					{"Δεκέμβριος", "", "", ""},
					{"ΣΥΝΟΛΟ", "", "", ""},
				};

				// Center renderer for table columns
				DefaultTableCellRenderer centerText = new DefaultTableCellRenderer();
				centerText.setHorizontalAlignment(SwingConstants.CENTER);

				// JTable for Joker single draw
				dataViewTable = new JTable(data, columns);
				dataViewTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
				dataViewTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
				dataViewTable.getColumnModel().getColumn(2).setCellRenderer(centerText);
				dataViewTable.getColumnModel().getColumn(3).setCellRenderer(centerText);

				// Make table cells unselectable and uneditable
				dataViewTable.setEnabled(false);

				// Disable table column re-ordering
				dataViewTable.getTableHeader().setReorderingAllowed(false);

				// Make the JScrollPane take the same size as the JTable
				dataViewTable.setPreferredScrollableViewportSize(dataViewTable.getPreferredSize());

			dataViewPanel.add(new JScrollPane(dataViewTable), BorderLayout.NORTH);
			dataViewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dataViewPanel.getMinimumSize().height));


			// Data view panel
			JPanel exportToPdfPanel = new JPanel();
			exportToPdfPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			exportToPdfPanel.setLayout(new BoxLayout(exportToPdfPanel, BoxLayout.X_AXIS));
			exportToPdfPanel.setBackground(backColor);

				// Button export to pdf
				JButton buttonExportToPdf = new JButton("Εξαγωγή συγκεντρωτικών δεδομένων σε αρχείο pdf (API)");
				buttonExportToPdf.addActionListener(this::buttonExportToPdfActionPerformed);

			exportToPdfPanel.add(Box.createHorizontalGlue());
			exportToPdfPanel.add(buttonExportToPdf);

		middlePanel.add(gameSelectPanel);
		middlePanel.add(chooseMethodLabelPanel);
		middlePanel.add(chooseMethodAndDLPanel);
		middlePanel.add(dataViewPanel);
		middlePanel.add(exportToPdfPanel);
		middlePanel.add(Box.createVerticalGlue());


		// Empty expanding panel
		JPanel expandingPanel = new JPanel();
		expandingPanel.setLayout(new BorderLayout());
		expandingPanel.setBackground(backColor);


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

		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(buttonClose);


		/*
		 * Main panel
		 */
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new Dimension(870, 558));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(expandingPanel);
		mainPanel.add(bottomPanel);


		/*
		 * Main window
		 */
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Show data");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(880, 588));
		dialog.setResizable(false);
		dialog.setIconImages(icons);

		// Populate comboBoxYearSelect
		populateComboBoxYearSelect();
		CompletableFuture.runAsync(() -> findLastDrawDate());  // Run asynchronously

		dialog.setVisible(true);
	}
}
