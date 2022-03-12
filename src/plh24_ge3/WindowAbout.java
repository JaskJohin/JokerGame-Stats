package plh24_ge3;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class WindowAbout
{
	// Variables declaration
	private final List<String> authorList;
	private final JPanel middlePanelCards;
	private final JLabel labelAuthor1;
	private final JLabel labelAuthor2;
	private final JLabel labelAuthor3;
	private final JLabel labelAuthor4;
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
			Collections.shuffle(authorList);
			labelAuthor1.setText("    " + authorList.get(0));
			labelAuthor2.setText("    " + authorList.get(1));
			labelAuthor3.setText("    " + authorList.get(2));
			labelAuthor4.setText("    " + authorList.get(3));
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
		authorList = new ArrayList<>();
		authorList.add("Αθανάσιος Θεοδωρόπουλος");
		authorList.add("Αλέξανδρος Δημητρακόπουλος");
		authorList.add("Οδυσσέας Ραυτόπουλος");
		authorList.add("Χριστόφορος Αμπελάς");
		Collections.shuffle(authorList);

		// License text
		String licenceText = "Copyright 2022, Athanasios Theodoropoulos, Alexandros Dimitrakopoulos, Odysseas Raftopoulos, Xristoforos Ampelas\n" +
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

		// Background color
		Color backColor = new java.awt.Color(244, 244, 250);

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

		// Font
		Font fontRobotoRegular = null;
		Font fontRobotoBold = null;
		try
		{
			fontRobotoRegular = Font.createFont(Font.PLAIN, getClass().getResourceAsStream("/resources/Roboto-Regular.ttf"));
			fontRobotoBold = Font.createFont(Font.PLAIN, getClass().getResourceAsStream("/resources/Roboto-Bold.ttf"));
		}
		catch (FontFormatException | IOException ex)
		{
			System.err.println(ex);
			fontRobotoRegular = new Font(null, 0, 22);  // Fallback, not suppose to happen
			fontRobotoBold = new Font(null, 1, 22);  // Fallback, not suppose to happen
		}


		// Top panel
		JPanel topPanel = new JPanel();    // Panel with the window title label
		topPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 0));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(new java.awt.Color(78, 116, 221));

			// Labels
			JLabel labelIcon = new JLabel();
			labelIcon.setIcon(new ImageIcon(icons.get(6)));
			labelIcon.setAlignmentY(Component.BOTTOM_ALIGNMENT);

			JLabel labelName = new JLabel("Joker stats");
			labelName.setFont(new Font("Arial", 1, 32));
			labelName.setForeground(Color.WHITE);
			labelName.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
			labelName.setAlignmentY(Component.BOTTOM_ALIGNMENT);

			JLabel labelVer = new JLabel("v1.0");
			labelVer.setFont(new Font("Arial", 0, 16));
			labelVer.setForeground(Color.WHITE);
			labelVer.setBorder(BorderFactory.createEmptyBorder(0, 10, 4, 0));
			labelVer.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		topPanel.add(labelIcon);
		topPanel.add(labelName);
		topPanel.add(labelVer);
		topPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);


		// CardLayout: Info / License panel
		middlePanelCards = new JPanel();    
		middlePanelCards.setLayout(new CardLayout());

			// Middle panel with discription, authors, license
			JPanel middlePanelInfo = new JPanel();
			middlePanelInfo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			middlePanelInfo.setLayout(new BoxLayout(middlePanelInfo, BoxLayout.Y_AXIS));
			middlePanelInfo.setBackground(backColor);

				// Description label
				JPanel labelDescriptionPanel = new JPanel(new FlowLayout(0, 0, 0));
				labelDescriptionPanel.setBackground(backColor);
					JLabel labelDescription = new JLabel("Αναζήτηση και προβολή κληρώσεων και στατιστικών του παιχνιδιού Τζόκερ.");
					labelDescription.setFont(fontRobotoBold.deriveFont(1, 12));
				labelDescriptionPanel.add(labelDescription);
				labelDescriptionPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, labelDescriptionPanel.getMinimumSize().height));


				// Authors labels
				JPanel labelAuthorsPanel = new JPanel(new FlowLayout(0, 0, 0));
				labelAuthorsPanel.setBackground(backColor);
					JLabel labelAuthors = new JLabel("Δημιουργοί:");
					labelAuthors.setFont(fontRobotoBold.deriveFont(1, 12));
				labelAuthorsPanel.add(labelAuthors);
				labelAuthorsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, labelAuthorsPanel.getMinimumSize().height + 1));

				JPanel author1Panel = new JPanel(new FlowLayout(0, 0, 0));
				author1Panel.setBackground(backColor);
					labelAuthor1 = new JLabel("    " + authorList.get(0));
					labelAuthor1.setFont(fontRobotoRegular.deriveFont(0, 12));
				author1Panel.add(labelAuthor1);
				author1Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, author1Panel.getMinimumSize().height + 1));

				JPanel author2Panel = new JPanel(new FlowLayout(0, 0, 0));
				author2Panel.setBackground(backColor);
					labelAuthor2 = new JLabel("    " + authorList.get(1));
					labelAuthor2.setFont(fontRobotoRegular.deriveFont(0, 12));
				author2Panel.add(labelAuthor2);
				author2Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, author2Panel.getMinimumSize().height + 1));

				JPanel author3Panel = new JPanel(new FlowLayout(0, 0, 0));
				author3Panel.setBackground(backColor);
					labelAuthor3 = new JLabel("    " + authorList.get(2));
					labelAuthor3.setFont(fontRobotoRegular.deriveFont(0, 12));
				author3Panel.add(labelAuthor3);
				author3Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, author3Panel.getMinimumSize().height + 1));

				JPanel author4Panel = new JPanel(new FlowLayout(0, 0, 0));
				author4Panel.setBackground(backColor);
					labelAuthor4 = new JLabel("    " + authorList.get(3));
					labelAuthor4.setFont(fontRobotoRegular.deriveFont(0, 12));
				author4Panel.add(labelAuthor4);
				author4Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, author4Panel.getMinimumSize().height + 1));


				// Libraries labels
				JPanel labelLibrariesPanel = new JPanel(new FlowLayout(0, 0, 0));
				labelLibrariesPanel.setBackground(backColor);
					JLabel labelLibraries = new JLabel("Χρησιμοποιούνται οι βιβλιοθήκες:");
					labelLibraries.setFont(fontRobotoBold.deriveFont(1, 12));
				labelLibrariesPanel.add(labelLibraries);
				labelLibrariesPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, labelLibrariesPanel.getMinimumSize().height + 1));

				JPanel library1Panel = new JPanel(new FlowLayout(0, 0, 0));
				library1Panel.setBackground(backColor);
					JLabel labelLibrary1 = new JLabel("    " + "EclipseLink       (Eclipse Public License v1.0)");
					labelLibrary1.setFont(fontRobotoRegular.deriveFont(0, 12));
				library1Panel.add(labelLibrary1);
				library1Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library1Panel.getMinimumSize().height + 1));

				JPanel library2Panel = new JPanel(new FlowLayout(0, 0, 0));
				library2Panel.setBackground(backColor);
					JLabel labelLibrary2 = new JLabel("    " + "Apache Derby  (Apache License v2.0)");
					labelLibrary2.setFont(fontRobotoRegular.deriveFont(0, 12));
				library2Panel.add(labelLibrary2);
				library2Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library2Panel.getMinimumSize().height + 1));

				JPanel library3Panel = new JPanel(new FlowLayout(0, 0, 0));
				library3Panel.setBackground(backColor);
					JLabel labelLibrary3 = new JLabel("    " + "Gson                  (Apache License v2.0)");
					labelLibrary3.setFont(fontRobotoRegular.deriveFont(0, 12));
				library3Panel.add(labelLibrary3);
				library3Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library3Panel.getMinimumSize().height + 1));

				JPanel library4Panel = new JPanel(new FlowLayout(0, 0, 0));
				library4Panel.setBackground(backColor);
					JLabel labelLibrary4 = new JLabel("    " + "OkHttp               (Apache License v2.0)");
					labelLibrary4.setFont(fontRobotoRegular.deriveFont(0, 12));
				library4Panel.add(labelLibrary4);
				library4Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library4Panel.getMinimumSize().height + 1));

				JPanel library5Panel = new JPanel(new FlowLayout(0, 0, 0));
				library5Panel.setBackground(backColor);
					JLabel labelLibrary5 = new JLabel("    " + "Okio                    (Apache License v2.0)");
					labelLibrary5.setFont(fontRobotoRegular.deriveFont(0, 12));
				library5Panel.add(labelLibrary5);
				library5Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library5Panel.getMinimumSize().height + 1));

				JPanel library6Panel = new JPanel(new FlowLayout(0, 0, 0));
				library6Panel.setBackground(backColor);
					JLabel labelLibrary6 = new JLabel("    " + "Kotlin stdlib      (Apache License v2.0)");
					labelLibrary6.setFont(fontRobotoRegular.deriveFont(0, 12));
				library6Panel.add(labelLibrary6);
				library6Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library6Panel.getMinimumSize().height + 1));

				JPanel library7Panel = new JPanel(new FlowLayout(0, 0, 0));
				library7Panel.setBackground(backColor);
					JLabel labelLibrary7 = new JLabel("    " + "JFreeChart        (GNU Lesser General Public Licence v2.1)");
					labelLibrary7.setFont(fontRobotoRegular.deriveFont(0, 12));
				library7Panel.add(labelLibrary7);
				library7Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library7Panel.getMinimumSize().height + 1));

				JPanel library8Panel = new JPanel(new FlowLayout(0, 0, 0));
				library8Panel.setBackground(backColor);
					JLabel labelLibrary8 = new JLabel("    " + "iTextPDF 7        (Affero General Public License v3)");
					labelLibrary8.setFont(fontRobotoRegular.deriveFont(0, 12));
				library8Panel.add(labelLibrary8);
				library8Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library8Panel.getMinimumSize().height + 1));

				JPanel library9Panel = new JPanel(new FlowLayout(0, 0, 0));
				library9Panel.setBackground(backColor);
					JLabel labelLibrary9 = new JLabel("    " + "SLF4J                (MIT license)");
					labelLibrary9.setFont(fontRobotoRegular.deriveFont(0, 12));
				library9Panel.add(labelLibrary9);
				library9Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, library9Panel.getMinimumSize().height + 1));


				// Fonts labels
				JPanel labelFontsPanel = new JPanel(new FlowLayout(0, 0, 0));
				labelFontsPanel.setBackground(backColor);
					JLabel labelFonts = new JLabel("και οι γραμματοσειρές:");
					labelFonts.setFont(fontRobotoBold.deriveFont(1, 12));
				labelFontsPanel.add(labelFonts);
				labelFontsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, labelFontsPanel.getMinimumSize().height + 1));

				JPanel font1Panel = new JPanel(new FlowLayout(0, 0, 0));
				font1Panel.setBackground(backColor);
					JLabel labelFont1 = new JLabel("    " + "NotoSans         (SIL Open Font License v1.1)");
					labelFont1.setFont(fontRobotoRegular.deriveFont(0, 12));
				font1Panel.add(labelFont1);
				font1Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, font1Panel.getMinimumSize().height + 1));

				JPanel font2Panel = new JPanel(new FlowLayout(0, 0, 0));
				font2Panel.setBackground(backColor);
					JLabel labelFont2 = new JLabel("    " + "Roboto              (Apache License v2.0)");
					labelFont2.setFont(fontRobotoRegular.deriveFont(0, 12));
				font2Panel.add(labelFont2);
				font2Panel.setMaximumSize(new Dimension(Short.MAX_VALUE, font2Panel.getMinimumSize().height + 1));


				// License label
				JPanel labelLicensePanel = new JPanel(new FlowLayout(0, 0, 0));
				labelLicensePanel.setBackground(backColor);
					JLabel labelLicense = new JLabel("Αυτό το πρόγραμμα διατείθεται υπό την άδεια BSD License 2.0.");
					labelLicense.setFont(fontRobotoBold.deriveFont(1, 12));
				labelLicensePanel.add(labelLicense);
				labelLicensePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, labelLicensePanel.getMinimumSize().height));

			middlePanelInfo.add(Box.createRigidArea(new Dimension(0, 18)));
			middlePanelInfo.add(labelDescriptionPanel);
			middlePanelInfo.add(Box.createRigidArea(new Dimension(0, 16)));
			middlePanelInfo.add(labelAuthorsPanel);
			middlePanelInfo.add(author1Panel);
			middlePanelInfo.add(author2Panel);
			middlePanelInfo.add(author3Panel);
			middlePanelInfo.add(author4Panel);
			middlePanelInfo.add(Box.createRigidArea(new Dimension(0, 16)));
			middlePanelInfo.add(labelLibrariesPanel);
			middlePanelInfo.add(library1Panel);
			middlePanelInfo.add(library2Panel);
			middlePanelInfo.add(library3Panel);
			middlePanelInfo.add(library4Panel);
			middlePanelInfo.add(library5Panel);
			middlePanelInfo.add(library6Panel);
			middlePanelInfo.add(library7Panel);
			middlePanelInfo.add(library8Panel);
			middlePanelInfo.add(library9Panel);
			middlePanelInfo.add(Box.createRigidArea(new Dimension(0, 2)));
			middlePanelInfo.add(labelFontsPanel);
			middlePanelInfo.add(font1Panel);
			middlePanelInfo.add(font2Panel);
			middlePanelInfo.add(Box.createRigidArea(new Dimension(0, 16)));
			middlePanelInfo.add(labelLicensePanel);


			// License text panel
			JPanel middlePanelLicense = new JPanel();    // Panel with the license text
			middlePanelLicense.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
			middlePanelLicense.setLayout(new GridLayout(0, 1, 0, 16));
			middlePanelLicense.setBackground(backColor);

				// Text area with the license
				JTextArea textAreaLicense = new JTextArea();
				textAreaLicense.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
				textAreaLicense.setLineWrap(true);
				textAreaLicense.setWrapStyleWord(true);
				textAreaLicense.setEditable(false);

				textAreaLicense.setText(licenceText);
				textAreaLicense.setCaretPosition(0);

				// Scroll pane to enclose the textAreaLicense
				JScrollPane scrollPane = new JScrollPane(textAreaLicense);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			middlePanelLicense.add(scrollPane);

		middlePanelCards.add(middlePanelInfo, "Info");
		middlePanelCards.add(middlePanelLicense, "License");


		// Bottom panel with License and Close buttons
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.setBackground(backColor);

			buttonLicense = new JButton("Άδεια");
			buttonLicense.setPreferredSize(new java.awt.Dimension(126, 26));
			buttonLicense.addActionListener(this::buttonLicenseActionPerformed);

			JButton buttonClose = new JButton("Κλείσιμο");
			buttonClose.setPreferredSize(new java.awt.Dimension(126, 26));
			buttonClose.addActionListener(this::buttonCloseActionPerformed);

		bottomPanel.add(buttonLicense);
		bottomPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		bottomPanel.add(buttonClose);


		// Main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new java.awt.Dimension(596, 520));
		mainPanel.setBackground(backColor);
		mainPanel.add(topPanel);
		mainPanel.add(middlePanelCards);
		mainPanel.add(bottomPanel);

		// Main window
		dialog = new JDialog();
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("About");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);   // Appear in the center of screen
		dialog.setResizable(false);
		dialog.setIconImages(icons);
		dialog.setVisible(true);
	}
}
