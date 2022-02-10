package plh24_ge3;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowShowStats
{
	// Variables declaration
	private final JDialog dialog;




	// Button actions
	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}





	// Constructor
	public WindowShowStats()
	{
		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


		// Labels
		JLabel label = new JLabel("Προβολή στατιστικών");
		label.setFont(new Font("Arial", 3, 42));
		label.setForeground(Color.ORANGE);

		JLabel labelShadow = new JLabel("Προβολή στατιστικών");
		labelShadow.setFont(new Font("Arial", 3, 42));
		labelShadow.setForeground(Color.BLUE);



		// Buttons
		JButton buttonClose = new JButton("Κλείσιμο");
		buttonClose.setPreferredSize(new java.awt.Dimension(116, 26));
        buttonClose.addActionListener(this::buttonCloseActionPerformed);



		// Panels
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(6, 30, 0, 0));
		topPanel.setLayout(new GridLayout(0, 1, 0, -76));
		topPanel.setPreferredSize(new java.awt.Dimension(500, 89));
		topPanel.setMinimumSize(new java.awt.Dimension(500, 89));
		topPanel.setMaximumSize(new java.awt.Dimension(500, 89));
		topPanel.setBackground(backColor);
		topPanel.add(label);
		topPanel.add(labelShadow);





		JPanel bottomPanel = new JPanel();    // Panel with Close button
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);
		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonClose);

		JPanel mainPanel = new JPanel();    // Main panel
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new java.awt.Dimension(596, 362));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);


		// Main window
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Show stats");
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setResizable(false);
		dialog.setVisible(true);
	}
}