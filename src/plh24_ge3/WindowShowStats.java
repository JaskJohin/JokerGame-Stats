package plh24_ge3;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class WindowShowStats
{
        public static final String DEST = "files/TzokerStatisticalData.pdf";
    
	// Variables declaration
	private final JDialog dialog;

	// Button actions
	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}
        
        //Print PDF
        private void buttonPrintPdfActionPerformed(java.awt.event.ActionEvent evt)
	{
            
            try {
                //Χρειάζεται να περιληφθούν πεδία για καταχώριση ημερομηνιών από το χρήστη
                //για την π[αραγωγή των στατιστικών σε συγκεκριμένο εύρος
                
                File file = new File(DEST);
                file.getParentFile().mkdirs();
                new CreatePDF().createPdf(DEST);
                JOptionPane.showMessageDialog(null, "Το αρχείο δημιουργήθηκε",
                                          "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(WindowShowStats.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(WindowShowStats.class.getName()).log(Level.SEVERE, null, ex);
            }
            
	}
        
        //Show stats in graph form
	private void buttonGraphStatsActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowShowGraphStats();
	}
            
	// Constructor
	public WindowShowStats()
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
		topPanel.setPreferredSize(new Dimension(500, 89));
		topPanel.setMinimumSize(new Dimension(500, 89));
		topPanel.setMaximumSize(new Dimension(500, 89));
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
                
                // Button to print stats to PDF file
                JButton buttonPrintPdf = new JButton("Εκτύπωση σε PDF");
                buttonPrintPdf.setPreferredSize(new Dimension(206, 20));
                buttonPrintPdf.addActionListener(this::buttonPrintPdfActionPerformed);

			// Game selection & show graph stats panel
			JPanel gameSelectPanel = new JPanel();
			gameSelectPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			gameSelectPanel.setLayout(new FlowLayout(1, 5, 5));  // align,hgap,vgap (1,5,5)
			gameSelectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameSelectPanel.getMinimumSize().height));
			gameSelectPanel.setBackground(backColor);

				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");
				String comboBoxGameSelectItems[] = {"Τζόκερ (id: 5104)"};
				JComboBox comboBoxGameSelect = new JComboBox(comboBoxGameSelectItems);
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				comboBoxGameSelect.setBackground(backColor);
                                
                                // Show graph stats button
                                JButton buttonGraphStats = new JButton("Προβολή στατιστικών σε γραφική μορφή");
                                buttonGraphStats.addActionListener(this::buttonGraphStatsActionPerformed);

			gameSelectPanel.add(labelGameSelect);
			gameSelectPanel.add(comboBoxGameSelect);
                        gameSelectPanel.add(buttonGraphStats);
                  
                middlePanel.add(buttonPrintPdf);
		middlePanel.add(gameSelectPanel, BorderLayout.CENTER);
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
		mainPanel.setPreferredSize(new Dimension(596, 362));
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
		dialog.setMinimumSize(new Dimension(590, 360));
//		dialog.setResizable(false);
		dialog.setIconImages(icons);
		dialog.setVisible(true);
	}
}