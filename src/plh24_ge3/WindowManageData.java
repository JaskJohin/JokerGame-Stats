package plh24_ge3;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
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
import javax.swing.JTextField;


/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowManageData
{
	// Variables declaration
	private String firstDrawDate;
	private String firstDrawId;
	private String lastDrawId;
	private final JDialog dialog;
	private final JComboBox comboBoxGameSelect;
	private final JRadioButton radioButtonSingleDraw;
	private final JTextField textFieldDrawId;
	private final JTextField textFieldDate1;
	private final JTextField textFieldDate2;
	private final JComboBox comboBoxPredefinedRange;


	// Methods
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
				// URL
				URL website = new URL(urlStr);

				// Start connection and set timeout to 2 seconds
				URLConnection connection = website.openConnection();
				connection.setConnectTimeout(2*1000);

				// Open BufferedReader
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader in = new BufferedReader(isr);

				// Get json string
				String jsonStr = in.readLine();

				// Close BufferedReader
				in.close();


				// Parse jsonStr into json element and get an object structure
				JsonElement jElement = new JsonParser().parse(jsonStr);
				JsonObject jObject = jElement.getAsJsonObject();

				// Get the last draw object
				JsonArray content = jObject.getAsJsonArray("content");

				// Get the drawId from the last draw
				firstDrawId = content.get(0).getAsJsonObject().get("drawId").toString();
			}
			catch (Exception ex) { ex.printStackTrace(); /* Silently continue */ }
		}
		else
		{
			firstDrawId = "1";
		}
	}


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
			// URL
			URL website = new URL(urlStr);

			// Start connection and set timeout to 2 seconds
			URLConnection connection = website.openConnection();
			connection.setConnectTimeout(2*1000);

			// Open BufferedReader
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			// Get json string
			String jsonStr = in.readLine();

			// Close BufferedReader
			in.close();


			// Parse jsonStr into json element and get an object structure
			JsonElement jElement = new JsonParser().parse(jsonStr);
			JsonObject jObject = jElement.getAsJsonObject();

			// Get the last draw object
			JsonObject lastDraw = jObject.getAsJsonObject("last");

			// Get the drawId from the last draw
			lastDrawId = lastDraw.get("drawId").toString();

			// Populate textFieldDrawId
			if (populateTextFieldDrawId)
			{
				textFieldDrawId.setText(lastDrawId);
			}
		}
		catch (Exception ex) { /* Silently continue */ }
	}


	// Button actions
	private void comboBoxGameSelectActionPerformed(java.awt.event.ActionEvent evt)
	{
		findDateOfFirstDraw();
		CompletableFuture.runAsync(() -> findIdOfFirstDraw());    // Run asynchronously
		CompletableFuture.runAsync(() -> findLastDrawId(false));  // Run asynchronously
	}


	private void buttonFindLatestDrawActionPerformed(java.awt.event.ActionEvent evt)
	{
		textFieldDrawId.setText(lastDrawId);
	}


	private void comboBoxPredefinedRangeActionPerformed(java.awt.event.ActionEvent evt)
	{
		// Current local date
		LocalDate dateNow = LocalDate.now();

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Change the text in textFieldDate1 and textFieldDate2
		switch (comboBoxPredefinedRange.getSelectedIndex())
		{
			case 0:    // Last week
				textFieldDate1.setText(formatter.format(dateNow.minusDays(7)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 1:    // Last month
				textFieldDate1.setText(formatter.format(dateNow.minusMonths(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 2:    // Last 3 months
				textFieldDate1.setText(formatter.format(dateNow.minusMonths(3)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 3:    // Last year
				textFieldDate1.setText(formatter.format(dateNow.minusYears(1)));
				textFieldDate2.setText(formatter.format(dateNow));
				break;
			case 4:    // First year
				textFieldDate1.setText(firstDrawDate);
				LocalDate date = LocalDate.parse(firstDrawDate);
				textFieldDate2.setText(formatter.format(date.plusYears(1)));
				break;
		}
	}

	private void buttonDownloadActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (radioButtonSingleDraw.isSelected())
		{
			String dId = textFieldDrawId.getText();
			if (dId.matches("\\d+") && (Integer.parseInt(dId) >= Integer.parseInt(firstDrawId)) && (Integer.parseInt(dId) <= Integer.parseInt(lastDrawId)))
			{
				// TODO
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
			String errorMsg = "Η αρχική ημερομηνία πρέπει να είναι >= " + firstDrawDate + " και η τελική <= " + formatter.format(dateNow) + ".";
			try
			{
				LocalDate date1 = LocalDate.parse(textFieldDate1.getText());
				LocalDate date2 = LocalDate.parse(textFieldDate2.getText());
				LocalDate firstDate = LocalDate.parse(firstDrawDate);
				if (date1.plusDays(1).isAfter(firstDate) && date2.minusDays(1).isBefore(dateNow) && date1.minusDays(1).isBefore(date2))
				{
					// TODO
				}
				else
				{
					JOptionPane.showMessageDialog(null, errorMsg, "Λάθος είσοδος", 0);
				}
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, errorMsg, "Λάθος είσοδος", 0);
			}
		}
	}

	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}


	// Constructor
	public WindowManageData()
	{
		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


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

			// Game selection panel
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
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				comboBoxGameSelect.addActionListener(this::comboBoxGameSelectActionPerformed);
				comboBoxGameSelect.setBackground(backColor);

			gameSelectPanel.add(labelGameSelect);
			gameSelectPanel.add(comboBoxGameSelect);


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
				radioButtonSingleDraw.setBackground(backColor);
				radioButtonSingleDraw.setSelected(true);

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
				radioButtonDateRange.setBackground(backColor);

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
				String dateRanges[] = {"Τελευταία εβδομάδα", "Τελευταίος μήνας", "Τελευταίο 3μηνο", "Τελευταίο έτος", "Πρώτο έτος"};
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


			// Download panel
			JPanel downloadPanel = new JPanel();
			downloadPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			downloadPanel.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			downloadPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameSelectPanel.getMinimumSize().height));
			downloadPanel.setBackground(backColor);


				// Button download
				JButton buttonDownload = new JButton("Αναζήτηση και προβολή δεδομένων");
				buttonDownload.addActionListener(this::buttonDownloadActionPerformed);

			downloadPanel.add(buttonDownload);

		middlePanel.add(gameSelectPanel);
		middlePanel.add(chooseMethodLabelPanel);
		middlePanel.add(singleDrawMethodPanel);
		middlePanel.add(dateRangeMethodPanel);
		middlePanel.add(downloadPanel);
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
		mainPanel.setPreferredSize(new Dimension(800, 482));
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
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(800, 486));

		// Find firstDrawDate & lastDrawId in advance, populate textFieldDrawId
		findDateOfFirstDraw();
		CompletableFuture.runAsync(() -> findIdOfFirstDraw());    // Run asynchronously
		CompletableFuture.runAsync(() -> findLastDrawId(true));   // Run asynchronously

		dialog.setVisible(true);
	}
}
