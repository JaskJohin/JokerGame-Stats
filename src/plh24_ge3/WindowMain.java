package plh24_ge3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowMain    // MainClass
{
	// Button actions
	private void button1ActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowManageData();
	}

	private void button2ActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowShowData();
	}

	private void button3ActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowShowStats();
	}

	private void buttonAboutActionPerformed(java.awt.event.ActionEvent evt)
	{
		new WindowAbout();
	}

	private void buttonExitActionPerformed(java.awt.event.ActionEvent evt)
	{
		System.exit(0);
	}


	// Constructor
	public WindowMain()
	{
		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


		// Labels
		JLabel labelTitle = new JLabel("Τζόκερ stats");
		labelTitle.setFont(new Font("Arial", 3, 53));
		labelTitle.setForeground(Color.ORANGE);

		JLabel labelTitleShadow = new JLabel("Τζόκερ stats");
		labelTitleShadow.setFont(new Font("Arial", 3, 53));
		labelTitleShadow.setForeground(Color.BLUE);


		// Buttons
		JButton button1 = new JButton("Διαχείριση δεδομένων");
		button1.addActionListener(this::button1ActionPerformed);

		JButton button2 = new JButton("Προβολή δεδομένων");
		button2.addActionListener(this::button2ActionPerformed);

		JButton button3 = new JButton("Προβολή στατιστικών δεδομένων");
		button3.addActionListener(this::button3ActionPerformed);

		JButton buttonExit = new JButton("Έξοδος");
		buttonExit.setPreferredSize(new java.awt.Dimension(90, 26));
        buttonExit.addActionListener(this::buttonExitActionPerformed);

		JButton buttonAbout = new JButton("Σχετικά");
		buttonAbout.setPreferredSize(new java.awt.Dimension(90, 26));
		buttonAbout.addActionListener(this::buttonAboutActionPerformed);


		// Panels
		JPanel topPanel = new JPanel();    // Panel with the window title label
		topPanel.setBorder(BorderFactory.createEmptyBorder(6, 30, 0, 0));
		topPanel.setLayout(new GridLayout(0, 1, 0, -76));
		topPanel.setPreferredSize(new java.awt.Dimension(374, 89));
		topPanel.setMinimumSize(new java.awt.Dimension(374, 89));
		topPanel.setMaximumSize(new java.awt.Dimension(374, 89));
		topPanel.setBackground(backColor);
		topPanel.add(labelTitle);
		topPanel.add(labelTitleShadow);

		JPanel middlePanel = new JPanel();    // Panel with the main buttons
		middlePanel.setBorder(BorderFactory.createEmptyBorder(23, 30, 0, 30));
		middlePanel.setLayout(new GridLayout(0, 1, 0, 16));
		middlePanel.setPreferredSize(new java.awt.Dimension(374, 174));
		middlePanel.setMinimumSize(new java.awt.Dimension(374, 174));
		middlePanel.setMaximumSize(new java.awt.Dimension(374, 174));
		middlePanel.setBackground(backColor);
		middlePanel.add(button1);
		middlePanel.add(button2);
		middlePanel.add(button3);

		JPanel bottomPanel = new JPanel();    // Panel with About and Exit buttons
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 0, 10));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);
		bottomPanel.add(buttonAbout);
		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonExit);

		JPanel mainPanel = new JPanel();    // Main panel
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 30, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(bottomPanel);
		mainPanel.setPreferredSize(new java.awt.Dimension(394, 369));
		mainPanel.setMinimumSize(new java.awt.Dimension(394, 369));
		mainPanel.setMaximumSize(new java.awt.Dimension(394, 369));


		// Main window
		JFrame frame = new JFrame();
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Joker stats");
		frame.pack();
		frame.setLocationRelativeTo(null);   // Appear in the center of screen
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new WindowMain();
	}
}
