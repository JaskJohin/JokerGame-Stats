package plh24_ge3;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowAbout
{
	// Variables declaration
	private final JPanel middlePanelCards;
	private final JButton buttonLicense;
	private final JDialog dialog;


	// Button actions
	private void buttonLicenseActionPerformed(java.awt.event.ActionEvent evt)
	{
		CardLayout cl = (CardLayout)(middlePanelCards.getLayout());
		if (buttonLicense.getText().equals("Άδεια"))
		{
			cl.show(middlePanelCards, "License");
			buttonLicense.setText("Πληροφορίες");
		}
		else if (buttonLicense.getText().equals("Πληροφορίες"))
		{
			cl.show(middlePanelCards, "Info");
			buttonLicense.setText("Άδεια");
		}
	}

	private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt)
	{
		dialog.dispose();
	}


	// Constructor
	public WindowAbout()
	{
		// Author list
		List<String> authorList = new ArrayList<>();
		authorList.add("Αθανάσιος Θεοδωρόπουλος");
		authorList.add("Αλέξανδρος Δημητρακόπουλος");
		authorList.add("Οδυσσέας Ραυτόπουλος");
		authorList.add("Χριστόφορος Αμπελάς");
		Collections.shuffle(authorList);

	
		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);


		// Labels
		JLabel labelName = new JLabel("Joker stats");
		labelName.setFont(new Font("Arial", 1, 32));
		labelName.setForeground(Color.WHITE);
		labelName.setPreferredSize(new java.awt.Dimension(190, 40));
		labelName.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		JLabel labelVer = new JLabel("v1.0");
		labelVer.setFont(new Font("Arial", 0, 16));
		labelVer.setForeground(Color.WHITE);
		labelVer.setPreferredSize(new java.awt.Dimension(40, 40));
		labelVer.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		labelVer.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		JLabel labelDescription = new JLabel("Αναζήτηση και προβολή κληρώσεων και στατιστικών του παιχνιδιού Τζόκερ.");
		labelDescription.setFont(new Font("Arial", 0, 12));

		JLabel labelAuthors = new JLabel("Δημιουργοί:  " + authorList.get(0));
		labelAuthors.setFont(new Font("Arial", 0, 12));
		JLabel labelAuthor1 = new JLabel("                       " + authorList.get(1));
		labelAuthor1.setFont(new Font("Arial", 0, 12));
		JLabel labelAuthor2 = new JLabel("                       " + authorList.get(2));
		labelAuthor2.setFont(new Font("Arial", 0, 12));
		JLabel labelAuthor3 = new JLabel("                       " + authorList.get(3));
		labelAuthor3.setFont(new Font("Arial", 0, 12));

		JLabel labelLicense = new JLabel("Αυτό το πρόγραμμα διατείθεται υπό την άδεια FreeBSD.");
		labelLicense.setFont(new Font("Arial", 0, 12));


		// Text area with the license
		JTextArea textAreaLicense = new JTextArea();
		textAreaLicense.setLineWrap(true);
		textAreaLicense.setWrapStyleWord(true);
		textAreaLicense.setEditable(false);
		String lisenceText = "Copyright 2022, Athanasios Theodoropoulos, Aleksandros Dimitrakopoulos, Odysseas Raftopoulos, Xristoforos Ampelas\n" +
		"All rights reserved.\n" +
		"\n" +
		"Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n" +
		"\n" +
		"1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n" +
		"\n" +
		"2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n" +
		"\n" +
		"3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.\n" +
		"\n" +
		"THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
		textAreaLicense.setText(lisenceText);
		textAreaLicense.setCaretPosition(0);


		// Scroll pane to enclose the textAreaLicense
		JScrollPane scrollPane = new JScrollPane(textAreaLicense);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


		// Buttons
		JButton buttonClose = new JButton("Κλείσιμο");
		buttonClose.setPreferredSize(new java.awt.Dimension(116, 26));
        buttonClose.addActionListener(this::buttonCloseActionPerformed);

		buttonLicense = new JButton("Άδεια");
		buttonLicense.setPreferredSize(new java.awt.Dimension(116, 26));
		buttonLicense.addActionListener(this::buttonLicenseActionPerformed);


		// Panels
		JPanel topPanel = new JPanel();    // Panel with the window title label
		topPanel.setBorder(BorderFactory.createEmptyBorder(6, 20, 4, 0));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(new java.awt.Color(78, 116, 221));
		topPanel.add(labelName);
		topPanel.add(labelVer);
		topPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);

		JPanel authorsPanel = new JPanel();    // Just the authors
		authorsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		authorsPanel.setBackground(backColor);
		authorsPanel.add(labelAuthors);
		authorsPanel.add(labelAuthor1);
		authorsPanel.add(labelAuthor2);
		authorsPanel.add(labelAuthor3);

		JPanel middlePanelInfo = new JPanel();    // Panel: discription, authors, license
		middlePanelInfo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		middlePanelInfo.setLayout(new GridLayout(0, 1, 0, 16));
		middlePanelInfo.setPreferredSize(new java.awt.Dimension(596, 213));
		middlePanelInfo.setBackground(backColor);
		middlePanelInfo.add(labelDescription);
		middlePanelInfo.add(authorsPanel);
		middlePanelInfo.add(labelLicense);

		JPanel middlePanelLicense = new JPanel();    // Panel with the license text
		middlePanelLicense.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		middlePanelLicense.setLayout(new GridLayout(0, 1, 0, 16));
		middlePanelLicense.setPreferredSize(new java.awt.Dimension(596, 213));
		middlePanelLicense.setBackground(backColor);
		middlePanelLicense.add(scrollPane);

		middlePanelCards = new JPanel();    // CardLayout: Info / License
		middlePanelCards.setLayout(new CardLayout());
		middlePanelCards.add(middlePanelInfo, "Info");
		middlePanelCards.add(middlePanelLicense, "License");

		JPanel bottomPanel = new JPanel();    // Panel with License and Close buttons
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);
		bottomPanel.add(buttonLicense);
		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonClose);

		JPanel mainPanel = new JPanel();    // Main panel
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new java.awt.Dimension(596, 362));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanelCards);
		mainPanel.add(bottomPanel);


		// Main window
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("About");
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setResizable(false);
		dialog.setVisible(true);
	}
}