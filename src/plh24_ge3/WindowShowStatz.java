
package plh24_ge3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import model.QueriesSQL;
import model.Utilities;
import static plh24_ge3.WindowShowStatz.DEST;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class WindowShowStatz {
    
    public static final String DEST = "files/TzokerStatisticalData.pdf";
    
    // Variables declaration
    private final JTable numbersViewTable;
    private final JTable bonusNumbersViewTable;
    private final JDialog dialog;
    private final JTextField textFieldDate1, textFieldDate2;
    
        // Close button method
    	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}
        
    private void buttonFetchDataActionPerformed(java.awt.event.ActionEvent evt) {
        int wnOccurrences, wnDelays, bonusOccurrences, bonusDelays;
        String fromDate = textFieldDate1.getText();
        String toDate = textFieldDate2.getText();
        
        for(int i = 0; i < 45; i++) {
            try {
                numbersViewTable.setValueAt("", i, 1);
                numbersViewTable.setValueAt("", i, 2);
                wnOccurrences = QueriesSQL.singleNumberOccurrences(fromDate, toDate, (i + 1));
                wnDelays = QueriesSQL.singleNumberDelays(fromDate, toDate, (i + 1));
                
                numbersViewTable.setValueAt(wnOccurrences, i, 1);
                numbersViewTable.setValueAt(wnDelays, i, 2);
                } catch (ParseException ex) {
                Logger.getLogger(WindowShowStatz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(int j = 0; j < 20; j++) {
            try {
                bonusNumbersViewTable.setValueAt("", j, 1);
                bonusNumbersViewTable.setValueAt("", j, 2);
                bonusOccurrences = QueriesSQL.singleBonusOccurrences(fromDate, toDate, (j + 1));
                bonusDelays = QueriesSQL.singleBonusDelays(fromDate, toDate, (j + 1));

                bonusNumbersViewTable.setValueAt(bonusOccurrences, j, 1);
                bonusNumbersViewTable.setValueAt(bonusDelays, j, 2);
            } catch (ParseException ex) {
                Logger.getLogger(WindowShowStatz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }

    //Show stats in graph form
	private void buttonGraphStatsActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowShowGraphStats();
	}        
        
        //Print PDF
     
        private void buttonPrintPdfActionPerformed(java.awt.event.ActionEvent evt)
	{
            
            try {
                // TO DO
                //Χρειάζεται να περιληφθούν πεδία για καταχώριση ημερομηνιών από το χρήστη
                //για την π[αραγωγή των στατιστικών σε συγκεκριμένο εύρος
                String fromDate = "2021-01-01"; // ΝΑ ΑΝΤΙΚΑΤΑΣΤΑΘΕΙ ΜΕ INPUT ΑΠΟ ΧΡΗΣΤΗ
                String toDate = "2021-02-20";// ΝΑ ΑΝΤΙΚΑΤΑΣΤΑΘΕΙ ΜΕ INPUT ΑΠΟ ΧΡΗΣΤΗ
                //create a new File instance
                File file = new File(DEST);
                //create the directory where the file will be store (if not exists)
                file.getParentFile().mkdirs();
                //call the function to create the pdf with the statistics for the given date range
                new Utilities().createPdf(fromDate, toDate, DEST);
                //show a completion notification to the user
                JOptionPane.showMessageDialog(null, "Το αρχείο δημιουργήθηκε",
                                          "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ParseException ex) {
                Logger.getLogger(WindowShowStats.class.getName()).log(Level.SEVERE, null, ex);
            }        
	}
 
        
    // Constructor
    public WindowShowStatz()
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
			JLabel labelTitle = new JLabel("Προβολή στατιστικών");
			labelTitle.setFont(new Font("Arial", 3, 42));
			labelTitle.setForeground(Color.ORANGE);

			JLabel labelTitleShadow = new JLabel("Προβολή στατιστικών");
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

			// Date range selection & print to PDF
			JPanel dateRangeAndPrintPdfPanel = new JPanel();
			dateRangeAndPrintPdfPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			dateRangeAndPrintPdfPanel.setLayout(new FlowLayout(1, 5, 5));  // align,hgap,vgap (1,5,5)
			dateRangeAndPrintPdfPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateRangeAndPrintPdfPanel.getMinimumSize().height));
			dateRangeAndPrintPdfPanel.setBackground(backColor);		

                // Label from
				JLabel labelFrom = new JLabel("Από:");
					//labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 53, 0, 6));
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

				// Text field for date 1
				textFieldDate1 = new JTextField();
				textFieldDate1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				textFieldDate1.setPreferredSize(new Dimension(74, 20));

				// Label up to
				JLabel labelUpTo = new JLabel("Έως:");
				labelUpTo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));

				// Text field for date 2
				textFieldDate2 = new JTextField();
				textFieldDate2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				textFieldDate2.setPreferredSize(new Dimension(74, 20));
				// Game select
				JLabel labelGameSelect = new JLabel("Επιλέξτε παιχνίδι ");
				String comboBoxGameSelectItems[] = {"Τζόκερ (id: 5104)"};
				JComboBox comboBoxGameSelect = new JComboBox(comboBoxGameSelectItems);
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				comboBoxGameSelect.setBackground(backColor);
                
                JLabel labelDateRangeSelect = new JLabel("Εισάγετε ημ/νίες, ");

			dateRangeAndPrintPdfPanel.add(labelGameSelect);
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(10,0)));
			dateRangeAndPrintPdfPanel.add(comboBoxGameSelect);
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(70,0)));
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(6,0)));
            dateRangeAndPrintPdfPanel.add(labelDateRangeSelect);
            dateRangeAndPrintPdfPanel.add(labelFrom);
            dateRangeAndPrintPdfPanel.add(textFieldDate1);
            dateRangeAndPrintPdfPanel.add(labelUpTo);
            dateRangeAndPrintPdfPanel.add(textFieldDate2);

			// Data view panel
			JPanel dataViewPanel = new JPanel();
			dataViewPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			dataViewPanel.setLayout(new BorderLayout());
			dataViewPanel.setBackground(backColor);

				// Columns and initial data of the JTable for data per number
				String[] numbersColumns = {"Αριθμός", "Εμφανίσεις", "Καθυστερήσεις"};
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
				DefaultTableCellRenderer centerText1 = new DefaultTableCellRenderer();
				centerText1.setHorizontalAlignment(SwingConstants.CENTER);

				// JTable for Joker single draw
				numbersViewTable = new JTable(numbersData, numbersColumns);
				numbersViewTable.getColumnModel().getColumn(0).setCellRenderer(centerText1);
				numbersViewTable.getColumnModel().getColumn(1).setCellRenderer(centerText1);
				numbersViewTable.getColumnModel().getColumn(2).setCellRenderer(centerText1);
		
				// Make table cells unselectable and uneditable
				numbersViewTable.setEnabled(false);

				// Disable table column re-ordering
				numbersViewTable.getTableHeader().setReorderingAllowed(false);
                          
                                JScrollBar scrollBar1 = new JScrollBar(JScrollBar.VERTICAL, 10, 40, 0, 100);
                                numbersViewTable.add(scrollBar1,BorderLayout.EAST);

				// Make the JScrollPane take the same size as the JTable
				numbersViewTable.setPreferredScrollableViewportSize(numbersViewTable.getPreferredSize());

			dataViewPanel.add(new JScrollPane(numbersViewTable), BorderLayout.WEST);
            //create a button to fetch data
            JButton buttonFetchData = new JButton("Αναζήτηση");
            //add action to the button
            buttonFetchData.addActionListener(this::buttonFetchDataActionPerformed);
            //create a new panel to correctly position the button in the midle panel
            JPanel splitMiddlePanel = new JPanel(new GridLayout(5, 1));
            //create some empty space
            JLabel top1 = new JLabel("");
            JLabel top2 = new JLabel("");
            JLabel bottom1 = new JLabel("");
            JLabel bottom2 = new JLabel("");
            //add this ampety space
            splitMiddlePanel.add(top1);
            splitMiddlePanel.add(top2);
            //show Fetch Data button
            splitMiddlePanel.add(buttonFetchData);
            //add some more empty space
            splitMiddlePanel.add(bottom1);
            splitMiddlePanel.add(bottom2);
            dataViewPanel.add(splitMiddlePanel);
                        
                        // Columns and initial data of the JTable for data per bonus number
				String[] bonusNumbersColumns = {"Τζόκερ", "Εμφανίσεις", "Καθυστερήσεις"};
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
                    {"20", "", ""},                             
				};
                    // Show graph stats button
                    JButton buttonGraphStats = new JButton("Προβολή στατιστικών σε γραφική μορφή");
                    buttonGraphStats.setPreferredSize(new Dimension(206, 20));
                    buttonGraphStats.addActionListener(this::buttonGraphStatsActionPerformed);

                    // Button to print stats to PDF file
                    JButton buttonPrintPdf = new JButton("Εκτύπωση σε PDF");
                    buttonPrintPdf.setPreferredSize(new Dimension(206, 20));
                    buttonPrintPdf.addActionListener(this::buttonPrintPdfActionPerformed);

                    // Center renderer for table columns
				DefaultTableCellRenderer centerText = new DefaultTableCellRenderer();
				centerText.setHorizontalAlignment(SwingConstants.CENTER);

				// JTable for Joker single draw
				bonusNumbersViewTable = new JTable(bonusNumbersData, bonusNumbersColumns);
				bonusNumbersViewTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
				bonusNumbersViewTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
				bonusNumbersViewTable.getColumnModel().getColumn(2).setCellRenderer(centerText);
		
				// Make table cells unselectable and uneditable
				bonusNumbersViewTable.setEnabled(false);

				// Disable table column re-ordering
				bonusNumbersViewTable.getTableHeader().setReorderingAllowed(false);
                          
                    JScrollBar scrollBar2 = new JScrollBar(JScrollBar.VERTICAL, 10, 40, 0, 100);
                    bonusNumbersViewTable.add(scrollBar2,BorderLayout.EAST);

				// Make the JScrollPane take the same size as the JTable
				bonusNumbersViewTable.setPreferredScrollableViewportSize(bonusNumbersViewTable.getPreferredSize());

			dataViewPanel.add(new JScrollPane(bonusNumbersViewTable), BorderLayout.EAST);
                        
                       
                  
		middlePanel.add(dateRangeAndPrintPdfPanel);
		middlePanel.add(dataViewPanel);
		middlePanel.add(Box.createVerticalGlue());
        middlePanel.add(buttonGraphStats);
        middlePanel.add(buttonPrintPdf);
      

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
		dialog.setVisible(true);
	}
}