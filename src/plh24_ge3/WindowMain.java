package plh24_ge3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowMain
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
		topPanel.setPreferredSize(new Dimension(374, 89));
		topPanel.setMinimumSize(new Dimension(374, 89));
		topPanel.setMaximumSize(new Dimension(374, 89));
		topPanel.setBackground(backColor);

			// Labels with the title
			JLabel labelTitle = new JLabel("Τζόκερ stats");
			labelTitle.setFont(new Font("Arial", 3, 53));
			labelTitle.setForeground(Color.ORANGE);

			JLabel labelTitleShadow = new JLabel("Τζόκερ stats");
			labelTitleShadow.setFont(new Font("Arial", 3, 53));
			labelTitleShadow.setForeground(Color.BLUE);

		topPanel.add(labelTitle);
		topPanel.add(labelTitleShadow);

		/*
		 * Middle panel with the main buttons
		 */
		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(BorderFactory.createEmptyBorder(23, 30, 0, 30));
		middlePanel.setLayout(new GridLayout(0, 1, 0, 16));
		middlePanel.setPreferredSize(new Dimension(374, 174));
		middlePanel.setMinimumSize(new Dimension(374, 174));
		middlePanel.setMaximumSize(new Dimension(374, 174));
		middlePanel.setBackground(backColor);

			// Manage data button
			JButton button1 = new JButton("Διαχείριση δεδομένων");
			button1.addActionListener(this::button1ActionPerformed);

			// Show data button
			JButton button2 = new JButton("Προβολή δεδομένων");
			button2.addActionListener(this::button2ActionPerformed);

			// Show stats button
			JButton button3 = new JButton("Προβολή στατιστικών δεδομένων");
			button3.addActionListener(this::button3ActionPerformed);

		middlePanel.add(button1);
		middlePanel.add(button2);
		middlePanel.add(button3);

		/*
		 * Bottom panel with the with About and Exit buttons
		 */
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 0, 10));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);

			// About button
			JButton buttonAbout = new JButton("Σχετικά");
			buttonAbout.setPreferredSize(new Dimension(90, 26));
			buttonAbout.addActionListener(this::buttonAboutActionPerformed);

			// Exit button
			JButton buttonExit = new JButton("Έξοδος");
			buttonExit.setPreferredSize(new Dimension(90, 26));
			buttonExit.addActionListener(this::buttonExitActionPerformed);

		bottomPanel.add(buttonAbout);
		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonExit);

		/*
		 * Main panel
		 */
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 30, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(bottomPanel);
		mainPanel.setPreferredSize(new Dimension(394, 369));
		mainPanel.setMinimumSize(new Dimension(394, 369));
		mainPanel.setMaximumSize(new Dimension(394, 369));

		/*
		 * Main window
		 */
		JFrame frame = new JFrame();
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Joker stats");
		frame.pack();
		frame.setLocationRelativeTo(null);   // Appear in the center of screen
		frame.setResizable(false);
		frame.setIconImages(icons);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new WindowMain();
		model.DBTablesManager.createDatabaseTables();
	}
}
