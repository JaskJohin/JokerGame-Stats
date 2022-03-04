package plh24_ge3;

import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.AverageDistributedPrizeCat;
import model.QueriesSQL;
import org.jfree.ui.RefineryUtilities;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowShowGraphStats
{
	// Variables declaration
	private final JDialog dialog;
	private final JTextField textFieldDate1;
	private final JTextField textFieldDate2;

	// Button actions
	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}

	// Constructor
	public WindowShowGraphStats()
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
			JLabel labelTitle = new JLabel("Προβολή στατιστικών σε γραφική μορφή");
			labelTitle.setFont(new Font("Arial", 2, 23));
			labelTitle.setForeground(Color.ORANGE);

			JLabel labelTitleShadow = new JLabel("Προβολή στατιστικών σε γραφική μορφή");
			labelTitleShadow.setFont(new Font("Arial", 2, 23));
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

				JLabel labelGameSelect = new JLabel("Επιλέξτε τυχερό παιχνίδι");
				String comboBoxGameSelectItems[] = {"Τζόκερ (id: 5104)"};
				JComboBox comboBoxGameSelect = new JComboBox(comboBoxGameSelectItems);
				comboBoxGameSelect.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				comboBoxGameSelect.setBackground(backColor);

			gameSelectPanel.add(labelGameSelect);
			gameSelectPanel.add(comboBoxGameSelect);

			// Date range method panel
			JPanel dateRangeMethodPanel = new JPanel();
			dateRangeMethodPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
			dateRangeMethodPanel.setLayout(new FlowLayout(0, 0, 0));
			dateRangeMethodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateRangeMethodPanel.getMinimumSize().height));
			dateRangeMethodPanel.setBackground(backColor);			

				// Label from
				JLabel labelFrom = new JLabel("Από");
					//labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 53, 0, 6));
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

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

			dateRangeMethodPanel.add(labelFrom);
			dateRangeMethodPanel.add(textFieldDate1);
			dateRangeMethodPanel.add(labelUpTo);
			dateRangeMethodPanel.add(textFieldDate2);

		middlePanel.add(dateRangeMethodPanel, BorderLayout.CENTER);
		//dateRangeMethodPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);        
		middlePanel.add(gameSelectPanel, BorderLayout.CENTER);
		middlePanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);


		// Checkbox panel for selecting stats             
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		checkBoxPanel.setBackground(backColor);

			// Πεντε πιο συχνα εμφανιζομενοι αριθμοι
			JPanel checkBoxOne = new JPanel();
			checkBoxOne.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			checkBoxOne.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			checkBoxOne.setMaximumSize(new Dimension(Integer.MAX_VALUE, checkBoxOne.getMinimumSize().height));
			checkBoxOne.setBackground(backColor);

				JButton Button1 = new JButton("Προβολή Γραφήματος 1");
				//buttonDisplay.setPreferredSize(new Dimension(116, 26));
				Button1.addActionListener(this::buttonDisplayActionPerformed1);
				JLabel labelOne = new JLabel("(Πέντε πιο συχνά εμφανιζόμενοι αριθμοί)");
				//labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 53, 0, 6));
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

			// Πεντε πιο συχνα εμφανιζομενοι αριθμοι ΤΖΟΚΕΡ
			JPanel checkBoxTwo = new JPanel();
			checkBoxTwo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			checkBoxTwo.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			checkBoxTwo.setMaximumSize(new Dimension(Integer.MAX_VALUE, checkBoxTwo.getMinimumSize().height));
			checkBoxTwo.setBackground(backColor);  

				JButton Button2 = new JButton("Προβολή Γραφήματος 2");
				//buttonDisplay.setPreferredSize(new Dimension(116, 26));
				Button2.addActionListener(this::buttonDisplayActionPerformed2);
				JLabel labelTwo = new JLabel("(Πέντε πιο συχνά εμφανιζόμενοι αριθμοί ΤΖΟΚΕΡ)");
				//labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 53, 0, 6));
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

			// Μέσος όρος κερδών ανά κατηγορία    
			JPanel checkBoxThree = new JPanel();
			checkBoxThree.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			checkBoxThree.setLayout(new FlowLayout(0, 0, 0));  // align,hgap,vgap (1,5,5)
			checkBoxThree.setMaximumSize(new Dimension(Integer.MAX_VALUE, checkBoxThree.getMinimumSize().height));
			checkBoxThree.setBackground(backColor); 

				JButton Button3 = new JButton("Προβολή Γραφήματος 3");
				//buttonDisplay.setPreferredSize(new Dimension(116, 26));
				Button3.addActionListener(this::buttonDisplayActionPerformed3);
				JLabel labelThree = new JLabel("(Μέσος όρος κερδών ανά κατηγορία)");
				labelFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

			checkBoxOne.add(Button1);
			checkBoxOne.add(labelOne);
			checkBoxTwo.add(Button2);
			checkBoxTwo.add(labelTwo);
			checkBoxThree.add(Button3);
			checkBoxThree.add(labelThree);

		checkBoxPanel.add(checkBoxOne, BorderLayout.CENTER);
		checkBoxPanel.add(checkBoxTwo, BorderLayout.CENTER);
		checkBoxPanel.add(checkBoxThree, BorderLayout.CENTER);
		checkBoxPanel.add(Box.createVerticalGlue(), BorderLayout.EAST);

		JPanel checkBoxes = new JPanel();
		checkBoxes.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		checkBoxes.setLayout(new BoxLayout(checkBoxes, BoxLayout.Y_AXIS));
		checkBoxes.setBackground(backColor);


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
		mainPanel.add(checkBoxPanel);
		mainPanel.add(bottomPanel);


		/*
		 * Main window
		 */
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Show stats in graph form");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setMinimumSize(new Dimension(590, 360));
//		dialog.setResizable(false);
		dialog.setIconImages(icons);
		dialog.setVisible(true);
	}


	private void buttonDisplayActionPerformed1(java.awt.event.ActionEvent evt)
	{
		try {
			System.out.println(textFieldDate1.getText());
			System.out.println(textFieldDate2.getText());
			List<Integer> numbers = new ArrayList<>();
			List<Integer> occurency = new ArrayList<>();

			for(int i=1; i<=45; i++) {
				numbers.add(i);
				int num = QueriesSQL.singleNumberOccurrences(textFieldDate1.getText(), textFieldDate2.getText(), i);
				occurency.add(num);
			}
			System.out.println("--------------------------");
			for(int i=1; i<=45; i++) {
				System.out.print(numbers.get(i-1)+"     ");
				System.out.println(occurency.get(i-1));
			}
			System.out.println("--------------------------");
			int temp = 0;
			int temp2 = 0;
			for(int i=0; i < 45; i++) {  
				for(int j=1; j < 45; j++) {  
					if(occurency.get(j-1) < occurency.get(j)) {  
						//swap elements  
						temp = occurency.get(j-1);  
						occurency.set(j-1, occurency.get(j));
						occurency.set(j, temp);

						temp2 = numbers.get(j-1);  
						numbers.set(j-1, numbers.get(j));
						numbers.set(j, temp2);
					}
				}
			}

			System.out.println("*******************");
			for(int i=1; i<=5; i++){
				System.out.print(numbers.get(i-1)+"     ");
				System.out.println(occurency.get(i-1));
			}
			System.out.println("********************");

			Graph_Form chart = new Graph_Form(
				"Numbers Vs Occurencies", "5 Most Occured Numbers", numbers, occurency);

			chart.pack( );
			RefineryUtilities.centerFrameOnScreen( chart );
			chart.setVisible(true);
		} catch (ParseException ex) {
			Logger.getLogger(WindowShowGraphStats.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println("----------------->");
	}

	private void buttonDisplayActionPerformed2(java.awt.event.ActionEvent evt)
	{
		try {
			System.out.println(textFieldDate1.getText());
			System.out.println(textFieldDate2.getText());
			List<Integer> numbers = new ArrayList<>();
			List<Integer> occurency = new ArrayList<>();

			for(int i=1; i<=20; i++) {
				numbers.add(i);
				int num = QueriesSQL.singleBonusOccurrences(textFieldDate1.getText(), textFieldDate2.getText(), i);
				occurency.add(num);
			}
			System.out.println("--------------------------");                  
			for(int i=1; i<=20; i++) {
				System.out.print(numbers.get(i-1)+"     ");
				System.out.println(occurency.get(i-1));
			}
			System.out.println("--------------------------");
			int temp = 0;
			int temp2 = 0;
			for(int i=0; i < 20; i++) {  
				for(int j=1; j < 20; j++) {  
					if(occurency.get(j-1) < occurency.get(j)) {  
						//swap elements  
						temp = occurency.get(j-1);  
						occurency.set(j-1, occurency.get(j));
						occurency.set(j, temp);

						temp2 = numbers.get(j-1);  
						numbers.set(j-1, numbers.get(j));
						numbers.set(j, temp2);
					}
				}
			}

			System.out.println("*******************");                  
			for(int i=1; i<=5; i++){
				System.out.print(numbers.get(i-1)+"     ");
				System.out.println(occurency.get(i-1));
			}
			System.out.println("********************");

			Graph_Form chart = new Graph_Form(
				"Joker Numbers Vs Occurencies",
				"5 Most Occured Joker Numbers",
				numbers, occurency);

			chart.pack( );
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		} catch (ParseException ex) {
			Logger.getLogger(WindowShowGraphStats.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println("----------------->");
	}

	private void buttonDisplayActionPerformed3(java.awt.event.ActionEvent evt)
	{
		try {
			System.out.println(textFieldDate1.getText().toString());
			System.out.println(textFieldDate2.getText().toString());

			List<Integer> categoryId = new ArrayList<Integer>();
			List<Long> averageDistributed = new ArrayList<Long>();

			List<AverageDistributedPrizeCat> num = QueriesSQL.averageDistributedPerCategory(textFieldDate1.getText().toString(), textFieldDate2.getText().toString());

			for(int i=0; i<=7; i++) {
				categoryId.add(num.get(i).getCategoryId());
				averageDistributed.add(num.get(i).getAverageDistributed());
			}

			System.out.println("--------------------------");                  
			for(int i=0; i<=7; i++){
				System.out.print(categoryId.get(i) + "     ");
				System.out.println(averageDistributed.get(i));
			}
			System.out.println("--------------------------");

			Graph_Form chart = new Graph_Form(
				"Joker Distribution Vs Category",
				categoryId, averageDistributed,
				"Distribution Vs Category");

			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		} catch (ParseException ex) {
			Logger.getLogger(WindowShowGraphStats.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println("----------------->");
	}
}
