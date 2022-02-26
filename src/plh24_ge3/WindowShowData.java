package plh24_ge3;

import POJOs.Content;
import POJOs.ContentPK;
import com.google.gson.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import static plh24_ge3.Helper.jokerJsonSingleDrawToObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowShowData
{
	// Variables declaration
	private final JDialog dialog;
	private final JComboBox comboBoxGameSelect;
	private final JComboBox comboBoxYearSelect;
	private final JRadioButton radioButtonApi;
	private final JTable dataViewTable;
        private static  EntityManagerFactory emf;
        private static EntityManager em;


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
	}

	/**
	 * Gather data for the selected game and year using the DB.
	 */
	private void getDataFromDB(String gameId, String year) throws Exception	{
            // Set the counters and sum to 0
            

            for(int i = 0; i < 12; i++) {
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
                System.out.println(endDate);
                //get the numbers of draws for current month from API
                JsonArray monthlyDraws = model.Utilities.GET_API_ARRAY("https://api.opap.gr/draws/v3.0/5104/draw-date/" + startDate +"/" + endDate + "/draw-id");
                //loop to check if record exists in the database
                for(int j = 0; j < monthlyDraws.size(); j++){
                    contentPK.setDrawid(monthlyDraws.get(j).getAsInt());
                    contentPK.setGameid(Integer.parseInt(gameId));
                    content.setContentPK(contentPK);
                    boolean control = QueriesSQL.checkIfRecordExists(content);
                    //if it doesn't exists, then add it so that presented statistical data are accurate
                    if(!control)  {
                        singleDrawObj = model.Utilities.GET_API("https://api.opap.gr/draws/v3.0/" +gameId + "/" + content.getContentPK().getDrawid());
                        AddDataController.storeDrawsDataByDrawID(singleDrawObj);
                    }
                }
                //set values for number of games, total earnings and number of jackpots
                drawCount = QueriesSQL.countMonthlyGames(startDate, endDate);
                System.out.println(drawCount);
                moneySum = QueriesSQL.sumMonthlyDivident(startDate, endDate);
                System.out.println(moneySum);
                jackpotCount = QueriesSQL.countJackpots(startDate, endDate);
                System.out.println(jackpotCount);
                //convert monyeSum to big decimal to format the number of decimal points shown
                bd = BigDecimal.valueOf(moneySum);
                BigDecimal moneySumBD = bd.setScale(2, RoundingMode.HALF_UP);
                
                // Put the data for this month to dataViewTable
                dataViewTable.setValueAt(drawCount, i, 1);
                dataViewTable.setValueAt(moneySumBD, i, 2);
                dataViewTable.setValueAt(jackpotCount, i, 3);
            
            }
            
        }

	// Button actions
	/**
	 * Action of the comboBoxGameSelect.
	 * @param evt 
	 */
	private void comboBoxGameSelectActionPerformed(java.awt.event.ActionEvent evt)
	{
		populateComboBoxYearSelect();
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
		topPanel.setBorder(BorderFactory.createEmptyBorder(6, 30, 0, 0));
		topPanel.setLayout(new GridLayout(0, 1, 0, -76));
		topPanel.setPreferredSize(new Dimension(490, 89));
		topPanel.setMinimumSize(new Dimension(490, 89));
		topPanel.setMaximumSize(new Dimension(490, 89));
		topPanel.setBackground(backColor);

			// Labels with the title
			JLabel labelTitle = new JLabel("Προβολή δεδομένων");
			labelTitle.setFont(new Font("Arial", 3, 42));
			labelTitle.setForeground(Color.ORANGE);

			JLabel labelTitleShadow = new JLabel("Προβολή δεδομένων");
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
				comboBoxGameSelect.addActionListener(this::comboBoxGameSelectActionPerformed);
				comboBoxGameSelect.setBackground(backColor);

				// Button download
				JButton buttonDownload = new JButton("Προβολή συγκεντρωτικών δεδομένων ανά μήνα για το έτος");
				buttonDownload.addActionListener((evt) ->
				{
					CompletableFuture.runAsync(() -> this.buttonDownloadActionPerformed(evt));
				});

				// ComboBox year select
				comboBoxYearSelect = new JComboBox();
				comboBoxYearSelect.setBackground(backColor);

			gameSelectAndDLPanel.add(labelGameSelect);
			gameSelectAndDLPanel.add(Box.createRigidArea(new Dimension(10,0)));
			gameSelectAndDLPanel.add(comboBoxGameSelect);
			gameSelectAndDLPanel.add(Box.createRigidArea(new Dimension(70,0)));
			gameSelectAndDLPanel.add(buttonDownload);
			gameSelectAndDLPanel.add(Box.createRigidArea(new Dimension(6,0)));
			gameSelectAndDLPanel.add(comboBoxYearSelect);


			// Choose search method label panel
			JPanel chooseMethodLabelPanel = new JPanel();
			chooseMethodLabelPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
			chooseMethodLabelPanel.setLayout(new FlowLayout(0, 0, 0));
			chooseMethodLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, chooseMethodLabelPanel.getMinimumSize().height));
			chooseMethodLabelPanel.setBackground(backColor);

				// Label choose method
				JLabel labelChooseMethod = new JLabel("Επιλέξτε από που θέλετε να αντληθούν τα δεδομένα");

			chooseMethodLabelPanel.add(labelChooseMethod);


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
				labelApiInfo.setFont(new Font("Arial", 0, 12));
				labelApiInfo.setForeground(Color.DARK_GRAY);

			apiMethodPanel.add(radioButtonApi);
			apiMethodPanel.add(labelApiInfo);
			apiMethodPanel.add(Box.createRigidArea(new Dimension(79,0)));


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
				JLabel labelDBInfo = new JLabel("(Αν τα δεδομένα δεν υπάρχουν ήδη, τότε θα προστεθούν στη βάση δεδομένων)");
				labelDBInfo.setBorder(BorderFactory.createEmptyBorder(1, 30, 0, 0));
				labelDBInfo.setFont(new Font("Arial", 0, 12));
				labelDBInfo.setForeground(Color.DARK_GRAY);

			DBMethodPanel.add(radioButtonDB);
			DBMethodPanel.add(labelDBInfo);
			DBMethodPanel.add(Box.createRigidArea(new Dimension(79,0)));


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

		middlePanel.add(gameSelectAndDLPanel);
		middlePanel.add(chooseMethodLabelPanel);
		middlePanel.add(apiMethodPanel);
		middlePanel.add(DBMethodPanel);
		middlePanel.add(dataViewPanel);
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
		mainPanel.setPreferredSize(new Dimension(772, 520));
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
		dialog.setTitle("Show data");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(780, 550));
		dialog.setResizable(false);
		dialog.setIconImages(icons);

		// Populate comboBoxYearSelect
		populateComboBoxYearSelect();

		dialog.setVisible(true);
	}
}
