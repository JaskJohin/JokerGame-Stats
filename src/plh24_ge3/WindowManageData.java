package plh24_ge3;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import static plh24_ge3.Helper.getJsonStrFromApiURL;
import static plh24_ge3.Helper.getUrlStrForDateRange;
import static plh24_ge3.Helper.jokerJsonSingleDrawToObject;


/**
 * Connect to API "https://api.opap.gr/draws/v3.0/{gameId}/draw-date/{fromDate}/{toDate}"
 * and get the json strings that the API returns.
 * Each thread handles a specified range of urlStrList elements.
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
class getJsonStrListFromUrlStrListMT extends Thread
{
	// Variables declaration
	private int index1 = 0;
	private int index2 = 0;
	private final List<String> urlStrList;
	private final List<String> jsonStrList = new ArrayList<>();


	// Constructor
	/**
	 * Creates a thread that connects to the URL strings from a range of urlStrList
	 * from index1 to index2 - 1, and gets the respective json strings.
	 * @param index1         Start index.
	 * @param index2         End index.
	 * @param urlStrList     Input List that contains the URL strings.
	 */
	public getJsonStrListFromUrlStrListMT(int index1, int index2, List<String> urlStrList)
	{
		this.index1 = index1;    // Start index. In single thread it would be = 0.
		this.index2 = index2;    // End index. In ST it would be = urlStrList.size().
		this.urlStrList = urlStrList;
	}


	// Methods
	public List<String> getJsonStrList() {return jsonStrList;}

	@Override
	public void run()
	{
		// Call the API urls and get the respective json strings
		for (int i = index1; i < index2; i++)
		{
			// URL string
			String urlStr = urlStrList.get(i);

			try
			{
				// Get json string from the API
				String jsonStr = getJsonStrFromApiURL(urlStr);

				// Add jsonStr to jsonStrList
				jsonStrList.add(jsonStr);
			}
			catch (Exception ex) { /* Silently continue */ }
		}
	}
}



/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowManageData
{
	// Variables declaration
	private String firstDrawDate;
	private int firstDrawId;
	private int lastDrawId = 0;
	private String lastSearchjsonStr;
	private List<String> lastSearchjsonStrList = new ArrayList<>();
	private final JDialog dialog;
	private final JComboBox comboBoxGameSelect;
	private final JRadioButton radioButtonSingleDraw;
	private final JTextField textFieldDrawId;
	private final JTextField textFieldDate1;
	private final JTextField textFieldDate2;
	private final JComboBox comboBoxPredefinedRange;
	private final JTextField textFieldDBDate1;
	private final JTextField textFieldDBDate2;
	private final JPanel viewPanelCards;
	private final JLabel labelDrawValue;
	private final JLabel labelDateValue;
	private final JLabel labelwinNum1Value;
	private final JLabel labelwinNum2Value;
	private final JLabel labelwinNum3Value;
	private final JLabel labelwinNum4Value;
	private final JLabel labelwinNum5Value;
	private final JLabel labelwinNum6Value;
	private final JLabel labelTotalColumnsValue;
	private final JTable jokerSDTable;
	private final DefaultTableModel modeljokerDRTable;
	private final JTable jokerDRTable;


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
	 * Stores the id of the 1st draw of the selected game to the variable firstDrawId.
	 * firstDrawId is used for checking if the drawId is valid. For every game it is
	 * equal to 1, except for Kino. This method is called when the window is first opened
	 * and every time a different game is selected.
	 */
	private void findIdOfFirstDraw()
	{
		// Kino's earliest data are from about 3 years in the past.
		if (comboBoxGameSelect.getSelectedItem().equals("Κίνο"))
		{
			// Current local date
			LocalDate dateNow = LocalDate.now();

			// Date format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Early Kino draw date - 3 years ago
			String date = formatter.format(dateNow.minusYears(3));

			// URL string
			String urlStr = "https://api.opap.gr/draws/v3.0/1100/draw-date/"+date+"/"+date;

			try
			{
				// Get json string from the API
				String jsonStr = getJsonStrFromApiURL(urlStr);

				// Parse jsonStr into json element and get an object structure
				JsonElement jElement = new JsonParser().parse(jsonStr);
				JsonObject jObject = jElement.getAsJsonObject();

				// Get the last draw object
				JsonArray content = jObject.getAsJsonArray("content");

				// Get the drawId
				firstDrawId = content.get(0).getAsJsonObject().get("drawId").getAsInt();
			}
			catch (Exception ex) { /* Silently continue */ }
		}
		else
		{
			firstDrawId = 1;
		}
	}


	/** 
	 * Uses the API "https://api.opap.gr/draws/v3.0/{gameId}/last-result-and-active" to
	 * find the id of the latest draw of the selected game and stores it in the variable
	 * lastDrawId. Optionally it populates the textFieldDrawId. This method is called when
	 * the window is first opened and every time a different game is selected. It is also
	 * called when trying to use the API, if lastDrawId == null, and is basically used
	 * to check the internet connection to the API.
	 * @param populateTextFieldDrawId   When true, it populates the textFieldDrawId.
	 */
	private void findLastDrawId(boolean populateTextFieldDrawId)
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

		try
		{
			// Get json string from the API
			String jsonStr = getJsonStrFromApiURL(urlStr);

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Get the last draw object
			JsonObject lastDraw = jObject.getAsJsonObject("last");

			// Get the drawId from the last draw
			lastDrawId = lastDraw.get("drawId").getAsInt();

			// Populate textFieldDrawId
			if (populateTextFieldDrawId)
			{
				textFieldDrawId.setText(String.valueOf(lastDrawId));
			}
		}
		catch (Exception ex) { /* Silently continue */ }
	}


	/** 
	 * Changes the GUI accordingly, when selecting a different game and when selecting
	 * single draw or date range.
	 */
	private void changeCards()
	{
		CardLayout cl = (CardLayout)(viewPanelCards.getLayout());
		if (comboBoxGameSelect.getSelectedItem().equals("Τζόκερ"))
		{
			if (radioButtonSingleDraw.isSelected())
			{
				cl.show(viewPanelCards, "jokerSingleDraw");
			}
			else
			{
				cl.show(viewPanelCards, "jokerDateRange");
			}
		}
	}


	/**
	 * Connects to the "https://api.opap.gr/draws/v3.0/5104/{drawId}" API, gets the data
	 * of a single draw of the game Joker and shows them in the GUI.
	 */
	private void getJokerSingleDrawData()
	{
		// Selected Joker draw
		String drawStr = textFieldDrawId.getText();

		// URL string
		String urlStr = "https://api.opap.gr/draws/v3.0/5104/" + drawStr;

		try
		{
			// Get json string from the API
			String jsonStr = getJsonStrFromApiURL(urlStr);

			// Store json string to attribute lastSearchjsonStr (for later use in DB)
			lastSearchjsonStr = jsonStr;

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Create a JokerDrawData object from the json object
			JokerDrawData jokerDraw = jokerJsonSingleDrawToObject(jObject);


			// Populate GUI for single draw
			labelDrawValue.setText(String.valueOf(jokerDraw.getDrawId()));
			labelDateValue.setText(String.valueOf(jokerDraw.getDrawDate()));
			labelTotalColumnsValue.setText(String.valueOf(jokerDraw.getColumns()));
			labelwinNum1Value.setText(String.valueOf(jokerDraw.getWinningNum1()));
			labelwinNum2Value.setText(String.valueOf(jokerDraw.getWinningNum2()));
			labelwinNum3Value.setText(String.valueOf(jokerDraw.getWinningNum3()));
			labelwinNum4Value.setText(String.valueOf(jokerDraw.getWinningNum4()));
			labelwinNum5Value.setText(String.valueOf(jokerDraw.getWinningNum5()));
			labelwinNum6Value.setText(String.valueOf(jokerDraw.getBonusNum()));

			jokerSDTable.setValueAt(jokerDraw.getPrizeTier5_1winners(),  0, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier5_1dividend(), 0, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier5winners(),    1, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier5dividend(),   1, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier4_1winners(),  2, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier4_1dividend(), 2, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier4winners(),    3, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier4dividend(),   3, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier3_1winners(),  4, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier3_1dividend(), 4, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier3winners(),    5, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier3dividend(),   5, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier2_1winners(),  6, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier2_1dividend(), 6, 2);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier1_1winners(),  7, 1);
			jokerSDTable.setValueAt(jokerDraw.getPrizeTier1_1dividend(), 7, 2);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}


	/**
	 * Connects to the "https://api.opap.gr/draws/v3.0/5104/draw-date/{fromDate}/{toDate}"
	 * API, gets the data for a date range of the game Joker and shows them in the GUI.
	 */
	private void getJokerDateRangeData()
	{
		// First remove the old data from the jokerDRTable
		modeljokerDRTable.setRowCount(0);


		// Variables
		int maxSimConnections = 50;  // Max number of simultaneous calls to the API
		int threadNum;               // Number of threads (simultaneous calls to the API)
		List<String> urlStrList;     // List with all the urls we will call
		List<String> jsonStrList = new ArrayList<>();  // List with the json strings

		// List with the API urls
		urlStrList = getUrlStrForDateRange(5104, textFieldDate1.getText(), textFieldDate2.getText());


		// Set number of threads = urlStrList.size(), but not more that maxSimConnections
		threadNum = urlStrList.size();
		if (threadNum > maxSimConnections) {threadNum = maxSimConnections;}

		// --- Create the threads to do the job ---
		getJsonStrListFromUrlStrListMT[] threadArray = new getJsonStrListFromUrlStrListMT[threadNum];
		int taskNum = urlStrList.size();    // Total number of tasks to do
		for (int i=0; i<threadNum; i++)
		{
			int index1 = i*taskNum/threadNum;      // Each thread does index2-index1
			int index2 = (i+1)*taskNum/threadNum;  // tasks, from index1 to index2-1
			threadArray[i] = new getJsonStrListFromUrlStrListMT(index1, index2, urlStrList);
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


		// Clear attribute lastSearchjsonStrList
		lastSearchjsonStrList.clear();


		// Merge results from all threads
		for (getJsonStrListFromUrlStrListMT thread : threadArray)
		{
			// Merge jsonStrList
			thread.getJsonStrList().forEach((jsonStr) ->
			{
				jsonStrList.add(jsonStr);
				lastSearchjsonStrList.add(jsonStr);  // Add here too (for later use in DB)
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

				// Create the text for winning column
				int n1 = jokerDraw.getWinningNum1();
				int n2 = jokerDraw.getWinningNum2();
				int n3 = jokerDraw.getWinningNum3();
				int n4 = jokerDraw.getWinningNum4();
				int n5 = jokerDraw.getWinningNum5();
				int n6 = jokerDraw.getBonusNum();
				String winCol = "<html><pre><font face=\"arial\" color=\"rgb(32,32,192)\">" +
					n1 + "  " + n2 + "  " + n3 + "  " + n4 + "  " + n5 + "</font>" +
					"<font face=\"arial\" color=\"rgb(210,105,0)\">  " + n6 +
					"</font></pre></html>";

				// Add a row to jokerDRTable
				modeljokerDRTable.addRow(new Object[] {
					jokerDraw.getDrawId(), jokerDraw.getDrawDate(),
					jokerDraw.getColumns(), winCol,
					jokerDraw.getPrizeTier5_1winners(), jokerDraw.getPrizeTier5_1dividend(),
					jokerDraw.getPrizeTier5winners(),   jokerDraw.getPrizeTier5dividend(),
					jokerDraw.getPrizeTier4_1winners(), jokerDraw.getPrizeTier4_1dividend(),
					jokerDraw.getPrizeTier4winners(),   jokerDraw.getPrizeTier4dividend(),
					jokerDraw.getPrizeTier3_1winners(), jokerDraw.getPrizeTier3_1dividend(),
					jokerDraw.getPrizeTier3winners(),   jokerDraw.getPrizeTier3dividend(),
					jokerDraw.getPrizeTier2_1winners(), jokerDraw.getPrizeTier2_1dividend(),
					jokerDraw.getPrizeTier1_1winners(), jokerDraw.getPrizeTier1_1dividend()
				});
			}
		}
	}



	// Button actions
	/**
	 * Action of the comboBoxGameSelect.
	 * @param evt 
	 */
	private void comboBoxGameSelectActionPerformed(java.awt.event.ActionEvent evt)
	{
		changeCards();
		findDateOfFirstDraw();
		CompletableFuture.runAsync(() -> findIdOfFirstDraw());    // Run asynchronously
		CompletableFuture.runAsync(() -> findLastDrawId(false));  // Run asynchronously
	}


	/**
	 * Action of the radioButtonSingleDraw.
	 * @param evt 
	 */
	private void radioButtonSingleDrawActionPerformed(java.awt.event.ActionEvent evt)
	{
		changeCards();
	}


	/**
	 * Action of the radioButtonDateRange.
	 * @param evt 
	 */
	private void radioButtonDateRangeActionPerformed(java.awt.event.ActionEvent evt)
	{
		changeCards();
	}


	/**
	 * Action of the buttonFindLatestDraw.
	 * @param evt 
	 */
	private void buttonFindLatestDrawActionPerformed(java.awt.event.ActionEvent evt)
	{
		textFieldDrawId.setText(String.valueOf(lastDrawId));
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
		LocalDate first;

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
	 * Action of the buttonDownload.
	 * @param evt 
	 */
	private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt)
	{
		// If lastDrawId remains 0, there's a connection error. So, show appropriate message.
		lastDrawId = 0;
		findLastDrawId(false);
		if (lastDrawId == 0)
		{
			String message = "Σφάλμα σύνδεσης στο API του ΟΠΑΠ.";
			JOptionPane.showMessageDialog(null, message, "Σφάλμα σύνδεσης", 0);
			return;
		}


		if (radioButtonSingleDraw.isSelected())
		{
			String dId = textFieldDrawId.getText();
			if (dId.matches("\\d+") && (Integer.parseInt(dId) >= firstDrawId) && (Integer.parseInt(dId) <= lastDrawId))
			{
				if (comboBoxGameSelect.getSelectedItem().equals("Τζόκερ"))
				{
					getJokerSingleDrawData();
				}
			}
			else
			{
				String message = "Ο αριθμός κλήρωσης πρέπει να είναι από " + firstDrawId + " έως " + lastDrawId + ".";
				JOptionPane.showMessageDialog(null, message, "Λάθος είσοδος", 0);
			}
		}
		else
		{
			LocalDate dateNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			// Check if given dates are valid
			String regexDate = "\\d\\d\\d\\d-\\d\\d-\\d\\d";
			String errorMsgDates = "Οι ημερομηνίες πρέπει να είναι της μορφής YYYY-MM-DD.";

			if (textFieldDate1.getText().matches(regexDate) || !textFieldDate2.getText().matches(regexDate))
			{
				try
				{
					LocalDate date1 = LocalDate.parse(textFieldDate1.getText());
					LocalDate date2 = LocalDate.parse(textFieldDate2.getText());
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, errorMsgDates, "Λάθος είσοδος", 0);
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, errorMsgDates, "Λάθος είσοδος", 0);
				return;
			}

			// Chech if the date range is valid
			LocalDate date1 = LocalDate.parse(textFieldDate1.getText());
			LocalDate date2 = LocalDate.parse(textFieldDate2.getText());
			LocalDate firstDate = LocalDate.parse(firstDrawDate);

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

			// Get the date for the selected date range
			try
			{
				if (comboBoxGameSelect.getSelectedItem().equals("Τζόκερ"))
				{
					getJokerDateRangeData();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Σφάλμα κλήσης στο API.", "Σφάλμα", 0);
			}
		}
	}


	/**
	 * Action of the buttonStoreInDB.
	 * @param evt 
	 */
	private void buttonStoreInDBActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (radioButtonSingleDraw.isSelected())
		{
			// Αν είναι ενεργοποιημένη η επιλογή "Συγκεκριμένη κλήρωση", το json string
			// της τελευταίας αναζήτησης είναι αποθηκευμένο στο attribute lastSearchjsonStr

			// Json string from the last single draw search
			String jsonStr = lastSearchjsonStr;

			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();


			// TODO   Έλεγχος αν υπάρχει ήδη η συγκεκριμένη κλήρωση στη βάση

			// TODO   Αποθήκευση κλήρωσης στη βάση (αν δεν υπάρχει ήδη)


		}
		else
		{
			// Αν είναι ενεργοποιημένη η επιλογή "Εύρος ημερομηνιών", τα json strings
			// της τελευταίας αναζήτησης είναι αποθηκευμένα στο attribute lastSearchjsonStrList

			// Parce all json strings in lastSearchjsonStrList
			for (int i = lastSearchjsonStrList.size()-1; i >= 0; i--)
			{
				String jsonStr = lastSearchjsonStrList.get(i);

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
					
					
					// TODO   Έλεγχος αν υπάρχουν ήδη (κάποια από) τα δεδομένα στη βάση

					// TODO   Αποθήκευση δεδομένων στη βάση (όσων δεν υπάρχουν ήδη)


				}
			}
		}
	}


	/**
	 * Action of the buttonDelDRInDB.
	 * @param evt 
	 */
	private void buttonDelDRInDBActionPerformed(java.awt.event.ActionEvent evt)
	{
		LocalDate fromDate = LocalDate.parse(textFieldDBDate1.getText());
		LocalDate toDate = LocalDate.parse(textFieldDBDate2.getText());

		// TODO   Διαγραφή δεδομένων από τη βάση με εύρος ημερομηνιών

	}


	/**
	 * Action of the buttonDelAllForSelGameInDB.
	 * @param evt 
	 */
	private void buttonDelAllForSelGameInDBActionPerformed(java.awt.event.ActionEvent evt)
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

		// TODO   Διαγραφή όλων των δεδομένων από τη βάση για το επιλεγμένο παιχνίδι

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
	public WindowManageData()
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


		/*
		 * Top panel with the window title
		 */
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(6, 30, 0, 0));
		topPanel.setLayout(new GridLayout(0, 1, 0, -76));
		topPanel.setPreferredSize(new Dimension(514, 89));
		topPanel.setMinimumSize(new Dimension(514, 89));
		topPanel.setMaximumSize(new Dimension(514, 89));
		topPanel.setBackground(backColor);

			// Labels with the title
			JLabel labelTitle = new JLabel("Διαχείριση δεδομένων");
			labelTitle.setFont(new Font("Arial", 3, 42));
			labelTitle.setForeground(Color.ORANGE);

			JLabel labelTitleShadow = new JLabel("Διαχείριση δεδομένων");
			labelTitleShadow.setFont(new Font("Arial", 3, 42));
			labelTitleShadow.setForeground(Color.BLUE);

		topPanel.add(labelTitle);
		topPanel.add(labelTitleShadow);


		/*
		 * Middle panel with all the functionality
		 */
		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.setBackground(backColor);

			// Game selection & download panel
			JPanel gameSelectAndDLPanel = new JPanel();
			gameSelectAndDLPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			gameSelectAndDLPanel.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			gameSelectAndDLPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameSelectAndDLPanel.getMinimumSize().height));
			gameSelectAndDLPanel.setBackground(backColor);

				// Label game select
				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");

				// ComboBox game select
				String opapGames[] = {"Τζόκερ"};
				comboBoxGameSelect = new JComboBox(opapGames);
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 70));
				comboBoxGameSelect.addActionListener(this::comboBoxGameSelectActionPerformed);
				comboBoxGameSelect.setBackground(backColor);

				// Button download
				JButton buttonDownload = new JButton("Αναζήτηση και προβολή δεδομένων");
				buttonDownload.addActionListener((evt) ->
				{
					CompletableFuture.runAsync(() -> this.buttonDownloadActionPerformed(evt));
				});

			gameSelectAndDLPanel.add(labelGameSelect);
			gameSelectAndDLPanel.add(comboBoxGameSelect);
			gameSelectAndDLPanel.add(buttonDownload);


			// Choose search method label panel
			JPanel chooseMethodLabelPanel = new JPanel();
			chooseMethodLabelPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
			chooseMethodLabelPanel.setLayout(new FlowLayout(0, 0, 0));
			chooseMethodLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, chooseMethodLabelPanel.getMinimumSize().height));
			chooseMethodLabelPanel.setBackground(backColor);

				// Label choose method
				JLabel labelChooseMethod = new JLabel("Επιλέξτε τρόπο αναζήτησης κληρώσεων");

			chooseMethodLabelPanel.add(labelChooseMethod);


			// Single draw method panel
			JPanel singleDrawMethodPanel = new JPanel();
			singleDrawMethodPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
			singleDrawMethodPanel.setLayout(new FlowLayout(0, 0, 0));
			singleDrawMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, singleDrawMethodPanel.getMinimumSize().height));
			singleDrawMethodPanel.setBackground(backColor);

				// Radio button for single draw
				radioButtonSingleDraw = new JRadioButton();
				radioButtonSingleDraw.setText("Συγκεκριμένη κλήρωση");
				radioButtonSingleDraw.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				radioButtonSingleDraw.addActionListener(this::radioButtonSingleDrawActionPerformed);
				radioButtonSingleDraw.setBackground(backColor);
				radioButtonSingleDraw.setSelected(false);

				// Label draw id
				JLabel labelDrawId = new JLabel("Αριθμός κλήρωσης");
				labelDrawId.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 6));

				// Text field for draw id
				textFieldDrawId = new JTextField();
				textFieldDrawId.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDrawId.setPreferredSize(new Dimension(58, 20));

				// Button to find latest draw
				JButton buttonFindLatestDraw = new JButton("Εύρεση τελευταίας κλήρωσης");
				buttonFindLatestDraw.setPreferredSize(new Dimension(206, 20));
				buttonFindLatestDraw.addActionListener(this::buttonFindLatestDrawActionPerformed);

			singleDrawMethodPanel.add(radioButtonSingleDraw);
			singleDrawMethodPanel.add(labelDrawId);
			singleDrawMethodPanel.add(textFieldDrawId);
			singleDrawMethodPanel.add(Box.createRigidArea(new Dimension(79,0)));
			singleDrawMethodPanel.add(buttonFindLatestDraw);


			// Date range method panel
			JPanel dateRangeMethodPanel = new JPanel();
			dateRangeMethodPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
			dateRangeMethodPanel.setLayout(new FlowLayout(0, 0, 0));
			dateRangeMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateRangeMethodPanel.getMinimumSize().height));
			dateRangeMethodPanel.setBackground(backColor);

				// Radio button for date range
				JRadioButton radioButtonDateRange = new JRadioButton();
				radioButtonDateRange.setText("Εύρος ημερομηνιών");
				radioButtonDateRange.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				radioButtonDateRange.addActionListener(this::radioButtonDateRangeActionPerformed);
				radioButtonDateRange.setBackground(backColor);
				radioButtonDateRange.setSelected(true);

				// Group radio butttons
				ButtonGroup groupChooseMethod = new ButtonGroup();
				groupChooseMethod.add(radioButtonSingleDraw);
				groupChooseMethod.add(radioButtonDateRange);

				// Label from
				JLabel labelFrom = new JLabel("Από");
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 53, 0, 6));

				// Text field for date 1
				textFieldDate1 = new JTextField();
				textFieldDate1.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDate1.setPreferredSize(new Dimension(74, 20));

				// Label up to
				JLabel labelUpTo = new JLabel("Έως");
				labelUpTo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));

				// Text field for date 2
				textFieldDate2 = new JTextField();
				textFieldDate2.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDate2.setPreferredSize(new Dimension(74, 20));

				// Label predefined date range
				JLabel labelPredefinedRange = new JLabel("Προκαθορισμένες επιλογές");
				labelPredefinedRange.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 6));

				// ComboBox quick select
				String dateRanges[] = {"Τελευταία εβδομάδα", "Τελευταίος μήνας",
					"Τελευταίο 3μηνο", "Τελευταίο έτος", "Τελευταία 5ετία",
					"Πρώτο έτος", "Πρώτη 5ετία", "Όλες οι κληρώσεις"};
				comboBoxPredefinedRange = new JComboBox(dateRanges);
				comboBoxPredefinedRange.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
				comboBoxPredefinedRange.setPreferredSize(new Dimension(152, 20));
				comboBoxPredefinedRange.addActionListener(this::comboBoxPredefinedRangeActionPerformed);
				comboBoxPredefinedRange.setSelectedIndex(0);
				comboBoxPredefinedRange.setBackground(backColor);

			dateRangeMethodPanel.add(radioButtonDateRange);
			dateRangeMethodPanel.add(labelFrom);
			dateRangeMethodPanel.add(textFieldDate1);
			dateRangeMethodPanel.add(labelUpTo);
			dateRangeMethodPanel.add(textFieldDate2);
			dateRangeMethodPanel.add(labelPredefinedRange);
			dateRangeMethodPanel.add(comboBoxPredefinedRange);


			// Data base panel
			JPanel dbPanel = new JPanel();
			dbPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			dbPanel.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			dbPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dbPanel.getMinimumSize().height));
			dbPanel.setBackground(backColor);

				// Label DB management
				JLabel labelDBManagement = new JLabel("Διαχείρηση βάσης δεδομένων");
				labelDBManagement.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));

				// Button store in DB
				JButton buttonStoreInDB = new JButton("Αποθήκευση δεδομένων");
				buttonStoreInDB.addActionListener(this::buttonStoreInDBActionPerformed);

				// Button delete all in date range
				JButton buttonDelDRInDB = new JButton("Διαγραφή των κληρώσεων στο εύρος");
				buttonDelDRInDB.addActionListener(this::buttonDelDRInDBActionPerformed);

				// Label from
				JLabel labelDBFrom = new JLabel("Από");
				labelDBFrom.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));

				// Text field for date 1
				textFieldDBDate1 = new JTextField();
				textFieldDBDate1.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDBDate1.setPreferredSize(new Dimension(74, 20));

				// Label up to
				JLabel labelDBUpTo = new JLabel("Έως");
				labelDBUpTo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));

				// Text field for date 2
				textFieldDBDate2 = new JTextField();
				textFieldDBDate2.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				textFieldDBDate2.setPreferredSize(new Dimension(74, 20));

				// Button delete all for selected game
				JButton buttonDelAllForSelGameInDB = new JButton("Διαγραφή όλων για το επιλεγμένο παιχνίδι");
				buttonDelAllForSelGameInDB.addActionListener(this::buttonDelAllForSelGameInDBActionPerformed);

			dbPanel.add(labelDBManagement);
			dbPanel.add(buttonStoreInDB);
			dbPanel.add(Box.createRigidArea(new Dimension(20,0)));
			dbPanel.add(buttonDelDRInDB);
			dbPanel.add(labelDBFrom);
			dbPanel.add(textFieldDBDate1);
			dbPanel.add(labelDBUpTo);
			dbPanel.add(textFieldDBDate2);
			dbPanel.add(Box.createRigidArea(new Dimension(20,0)));
			dbPanel.add(buttonDelAllForSelGameInDB);


			// Data view panel
			viewPanelCards = new JPanel();    // CardLayout: Single draw / Date range
			viewPanelCards.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			viewPanelCards.setLayout(new CardLayout());
			viewPanelCards.setBackground(backColor);

				// Panel: Joker single draw
				JPanel jokerSingleDrawPanel = new JPanel();
				jokerSingleDrawPanel.setLayout(new FlowLayout(0, 0, 0));
				jokerSingleDrawPanel.setBackground(backColor);

					JPanel jokerSDLeftPanel = new JPanel();
					jokerSDLeftPanel.setLayout(new BoxLayout(jokerSDLeftPanel, BoxLayout.Y_AXIS));
					jokerSDLeftPanel.setBackground(backColor);

						JPanel drawNumPanel = new JPanel();
						drawNumPanel.setLayout(new FlowLayout(0, 0, 0));
						drawNumPanel.setBackground(backColor);

							JLabel labelDraw = new JLabel("Κλήρωση");
							labelDraw.setBorder(BorderFactory.createEmptyBorder(0, 68, 0, 0));
							labelDraw.setFont(new Font("Arial", 0, 14));
							labelDraw.setForeground(Color.DARK_GRAY);

							labelDrawValue = new JLabel("");
							labelDrawValue.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
							labelDrawValue.setFont(new Font("Arial", 0, 14));
							labelDrawValue.setForeground(Color.DARK_GRAY);

						drawNumPanel.add(labelDraw);
						drawNumPanel.add(labelDrawValue);

						JPanel drawDatePanel = new JPanel();
						drawDatePanel.setLayout(new FlowLayout(0, 0, 0));
						drawDatePanel.setBackground(backColor);

							labelDateValue = new JLabel("");
							labelDateValue.setBorder(BorderFactory.createEmptyBorder(4, 78, 0, 0));
							labelDateValue.setPreferredSize(new Dimension(180, 16));
							labelDateValue.setFont(new Font("Arial", 0, 14));
							labelDateValue.setForeground(Color.DARK_GRAY);

						drawDatePanel.add(labelDateValue);

						JPanel winColumnPanel = new JPanel();
						winColumnPanel.setLayout(new FlowLayout(0, 0, 0));
						winColumnPanel.setBackground(backColor);

							JLabel labelWinColumn = new JLabel("Νικήτρια στήλη");
							labelWinColumn.setBorder(BorderFactory.createEmptyBorder(14, 55, 6, 0));
							labelWinColumn.setFont(new Font("Arial", 0, 18));

						winColumnPanel.add(labelWinColumn);

						JPanel winColumnValuePanel = new JPanel();
						winColumnValuePanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
						winColumnValuePanel.setLayout(new FlowLayout(0, 0, 0));
						winColumnValuePanel.setBackground(backColor);

							labelwinNum1Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum1Value.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
							labelwinNum1Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum1Value.setFont(new Font("Arial", 0, 18));
							labelwinNum1Value.setForeground(new Color(32,32,192));

							labelwinNum2Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum2Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum2Value.setFont(new Font("Arial", 0, 18));
							labelwinNum2Value.setForeground(new Color(32,32,192));

							labelwinNum3Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum3Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum3Value.setFont(new Font("Arial", 0, 18));
							labelwinNum3Value.setForeground(new Color(32,32,192));

							labelwinNum4Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum4Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum4Value.setFont(new Font("Arial", 0, 18));
							labelwinNum4Value.setForeground(new Color(32,32,192));

							labelwinNum5Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum5Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum5Value.setFont(new Font("Arial", 0, 18));
							labelwinNum5Value.setForeground(new Color(32,32,192));

							labelwinNum6Value = new JLabel("", SwingConstants.CENTER);
							labelwinNum6Value.setPreferredSize(new Dimension(36, 16));
							labelwinNum6Value.setFont(new Font("Arial", 0, 18));
							labelwinNum6Value.setForeground(new Color(210,105,0));

						winColumnValuePanel.add(labelwinNum1Value);
						winColumnValuePanel.add(labelwinNum2Value);
						winColumnValuePanel.add(labelwinNum3Value);
						winColumnValuePanel.add(labelwinNum4Value);
						winColumnValuePanel.add(labelwinNum5Value);
						winColumnValuePanel.add(labelwinNum6Value);

						JPanel totalColumnsPanel = new JPanel();
						totalColumnsPanel.setLayout(new FlowLayout(0, 0, 0));
						totalColumnsPanel.setBackground(backColor);

							JLabel labelTotalColumns = new JLabel("Σύνολο στηλών:");
							labelTotalColumns.setBorder(BorderFactory.createEmptyBorder(14, 34, 0, 0));
							labelTotalColumns.setFont(new Font("Arial", 0, 14));
							labelTotalColumns.setForeground(Color.DARK_GRAY);

							labelTotalColumnsValue = new JLabel("");
							labelTotalColumnsValue.setBorder(BorderFactory.createEmptyBorder(14, 4, 0, 0));
							labelTotalColumnsValue.setFont(new Font("Arial", 0, 14));
							labelTotalColumnsValue.setForeground(Color.DARK_GRAY);

						totalColumnsPanel.add(labelTotalColumns);
						totalColumnsPanel.add(labelTotalColumnsValue);

					jokerSDLeftPanel.add(drawNumPanel);
					jokerSDLeftPanel.add(drawDatePanel);
					jokerSDLeftPanel.add(winColumnPanel);
					jokerSDLeftPanel.add(winColumnValuePanel);
					jokerSDLeftPanel.add(totalColumnsPanel);

					JPanel jokerSDRightPanel = new JPanel();
					jokerSDRightPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
					jokerSDRightPanel.setLayout(new BorderLayout());
					jokerSDRightPanel.setBackground(backColor);

						// Columns and initial data of the JTable for Joker single draw
						String[] columnsSD = {"Κατηγορίες επιτυχιών", "Επιτυχίες",
							"Κέρδη ανά επιτυχία"};
						Object[][] dataSD = {
							{"5+1", "", ""},
							{"5", "", ""},
							{"4+1", "", ""},
							{"4", "", ""},
							{"3+1", "", ""},
							{"3", "", ""},
							{"2+1", "", ""},
							{"1+1", "", ""}
						};

						// Center renderer for table columns
						DefaultTableCellRenderer centerText = new DefaultTableCellRenderer();
						centerText.setHorizontalAlignment(SwingConstants.CENTER);
						
						// JTable for Joker single draw
						jokerSDTable = new JTable(dataSD, columnsSD);
						jokerSDTable.setPreferredSize(new Dimension(500, 128));
						jokerSDTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
						jokerSDTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
						jokerSDTable.getColumnModel().getColumn(2).setCellRenderer(centerText);

						// Make table cells unselectable and uneditable
						jokerSDTable.setEnabled(false);

						// Disable table column re-ordering
						jokerSDTable.getTableHeader().setReorderingAllowed(false);

						// Make the JScrollPane take the same size as the JTable
						jokerSDTable.setPreferredScrollableViewportSize(jokerSDTable.getPreferredSize());

					jokerSDRightPanel.add(new JScrollPane(jokerSDTable), BorderLayout.CENTER);

				jokerSingleDrawPanel.add(jokerSDLeftPanel);
				jokerSingleDrawPanel.add(jokerSDRightPanel);


				// Panel: Joker date range
				JPanel jokerDateRangePanel = new JPanel();
				jokerDateRangePanel.setLayout(new GridLayout(1,1));
				jokerDateRangePanel.setBackground(backColor);

					// Columns and initial data of the JTable for Joker date range
					String[] columnsDR = {"<html><center>Αριθμός<br>κλήρωσης</center></html>",
						"<html><center>Ημερομηνία</center></html>",
						"<html><center>Στήλες</center></html>",
						"<html><center>Νικήτρια στήλη</center></html>",
						"<html><center>'5+1'<br>επιτυχίες</center></html>",
						"<html><center>'5+1'<br>κέρδη</center></html>",
						"<html><center>'5'<br>επιτυχίες</center></html>",
						"<html><center>'5'<br>κέρδη</center></html>",
						"<html><center>'4+1'<br>επιτυχίες</center></html>",
						"<html><center>'4+1'<br>κέρδη</center></html>",
						"<html><center>'4'<br>επιτυχίες</center></html>",
						"<html><center>'4'<br>κέρδη</center></html>",
						"<html><center>'3+1'<br>επιτυχίες</center></html>",
						"<html><center>'3+1'<br>κέρδη</center></html>",
						"<html><center>'3'<br>επιτυχίες</center></html>",
						"<html><center>'3'<br>κέρδη</center></html>",
						"<html><center>'2+1'<br>επιτυχίες</center></html>",
						"<html><center>'2+1'<br>κέρδη</center></html>",
						"<html><center>'1+1'<br>επιτυχίες</center></html>",
						"<html><center>'1+1'<br>κέρδη</center></html>"};

					// Table model
					modeljokerDRTable = new DefaultTableModel(columnsDR, 0);

					// JTable for Joker date range
					jokerDRTable = new JTable(modeljokerDRTable);
					jokerDRTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(0).setMinWidth(61);
					jokerDRTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(1).setMinWidth(71);
					jokerDRTable.getColumnModel().getColumn(2).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(2).setMinWidth(63);
					jokerDRTable.getColumnModel().getColumn(3).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(3).setMinWidth(118);
					jokerDRTable.getColumnModel().getColumn(4).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(4).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(5).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(5).setMinWidth(92);
					jokerDRTable.getColumnModel().getColumn(6).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(6).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(7).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(7).setMinWidth(64);
					jokerDRTable.getColumnModel().getColumn(8).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(8).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(9).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(9).setMinWidth(50);
					jokerDRTable.getColumnModel().getColumn(10).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(10).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(11).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(11).setMinWidth(39);
					jokerDRTable.getColumnModel().getColumn(12).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(12).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(13).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(13).setMinWidth(39);
					jokerDRTable.getColumnModel().getColumn(14).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(14).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(15).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(15).setMinWidth(39);
					jokerDRTable.getColumnModel().getColumn(16).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(16).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(17).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(17).setMinWidth(39);
					jokerDRTable.getColumnModel().getColumn(18).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(18).setMinWidth(53);
					jokerDRTable.getColumnModel().getColumn(19).setCellRenderer(centerText);
					jokerDRTable.getColumnModel().getColumn(19).setMinWidth(39);

					// Make table cells unselectable and uneditable
					jokerDRTable.setEnabled(false);

					// Disable table column re-ordering
					jokerDRTable.getTableHeader().setReorderingAllowed(false);

					// Make the JScrollPane take the same size as the JTable
					jokerDRTable.setPreferredScrollableViewportSize(jokerDRTable.getPreferredSize());

					// Make table fill the entire Viewport height
					jokerDRTable.setFillsViewportHeight(true);

					// Scrollpane for the jokerDRTable
					JScrollPane spDR = new JScrollPane(jokerDRTable);
					spDR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

				jokerDateRangePanel.add(spDR);

			viewPanelCards.add(jokerDateRangePanel, "jokerDateRange");
			viewPanelCards.add(jokerSingleDrawPanel, "jokerSingleDraw");


		// Add elements to middle panel
		middlePanel.add(gameSelectAndDLPanel);
		middlePanel.add(chooseMethodLabelPanel);
		middlePanel.add(singleDrawMethodPanel);
		middlePanel.add(dateRangeMethodPanel);
		middlePanel.add(dbPanel);
		middlePanel.add(viewPanelCards);
		middlePanel.add(Box.createVerticalGlue());


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
		mainPanel.setPreferredSize(new Dimension(1198, 630));
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
		dialog.setTitle("Manage data");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(1206, 650));
		dialog.setIconImages(icons);

		// Find firstDrawDate & lastDrawId in advance, populate textFieldDrawId
		findDateOfFirstDraw();
		CompletableFuture.runAsync(() -> findIdOfFirstDraw());    // Run asynchronously
		CompletableFuture.runAsync(() -> findLastDrawId(true));   // Run asynchronously

		dialog.setVisible(true);
	}
}
