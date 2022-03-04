
package plh24_ge3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JScrollBar;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class WindowShowStatz {
    
    // Variables declaration
    private final JTable dataViewTable;
    private final JDialog dialog;
    
        // Close button method
    	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
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
			dateRangeAndPrintPdfPanel.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			dateRangeAndPrintPdfPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateRangeAndPrintPdfPanel.getMinimumSize().height));
			dateRangeAndPrintPdfPanel.setBackground(backColor);		

				// Game select
				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");
				String comboBoxGameSelectItems[] = {"Τζόκερ (id: 5104)"};
				JComboBox comboBoxGameSelect = new JComboBox(comboBoxGameSelectItems);
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				comboBoxGameSelect.setBackground(backColor);		

			dateRangeAndPrintPdfPanel.add(labelGameSelect);
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(10,0)));
			dateRangeAndPrintPdfPanel.add(comboBoxGameSelect);
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(70,0)));
			dateRangeAndPrintPdfPanel.add(Box.createRigidArea(new Dimension(6,0)));

			// Data view panel
			JPanel dataViewPanel = new JPanel();
			dataViewPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
			dataViewPanel.setLayout(new BorderLayout());
			dataViewPanel.setBackground(backColor);

				// Columns and initial data of the JTable for data per number
				String[] columns = {"Αριθμός", "Εμφανίσεις", "Καθυστερήσεις"};
				Object[][] data = {
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

				// JTable for Joker single draw
				dataViewTable = new JTable(data, columns);
				dataViewTable.getColumnModel().getColumn(0).setCellRenderer(centerText);
				dataViewTable.getColumnModel().getColumn(1).setCellRenderer(centerText);
				dataViewTable.getColumnModel().getColumn(2).setCellRenderer(centerText);
		
				// Make table cells unselectable and uneditable
				dataViewTable.setEnabled(false);

				// Disable table column re-ordering
				dataViewTable.getTableHeader().setReorderingAllowed(false);
                                
                                JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL, 10, 40, 0, 100);
                                dataViewTable.add(scrollBar,BorderLayout.EAST);

				// Make the JScrollPane take the same size as the JTable
				dataViewTable.setPreferredScrollableViewportSize(dataViewTable.getPreferredSize());

			dataViewPanel.add(new JScrollPane(dataViewTable), BorderLayout.NORTH);
                        
		middlePanel.add(dateRangeAndPrintPdfPanel);
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
		dialog.setVisible(true);
	}
}