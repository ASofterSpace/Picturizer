/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.Utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ChannelChangeGUI {

	private GUI gui;

	private JDialog ccDialog;

	private ImageIcon ccImageViewer;
	private JLabel ccImageViewerLabel;

	private Image ccPicture;

	private JTextField[] basicFields;
	private JTextField[] multiplierFields;


	public ChannelChangeGUI(GUI gui) {

		this.gui = gui;

		this.basicFields = new JTextField[3];
		this.multiplierFields = new JTextField[3];

		this.ccDialog = createGUI();
	}

	private JDialog createGUI() {

		// Create the window
		final JDialog ccDialog = new JDialog(gui.getMainFrame(), "Edit Channels Manually", true);
		GridBagLayout ccDialogLayout = new GridBagLayout();
		ccDialog.setLayout(ccDialogLayout);
		ccDialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Populate the window
		createChannelWidget('R', 0, ccDialog);
		createChannelWidget('G', 1, ccDialog);
		createChannelWidget('B', 2, ccDialog);

		ccImageViewer = new ImageIcon();
		ccImageViewerLabel = new JLabel(ccImageViewer);
		ccDialog.add(ccImageViewerLabel, new Arrangement(0, 3, 1.0, 1.0));

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 3);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		ccDialog.add(buttonRow, new Arrangement(0, 4, 1.0, 0.0));

		JButton applyPreview = new JButton("Apply to Preview");
		applyPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyPreview();
			}
		});
		buttonRow.add(applyPreview);

		JButton okButton = new JButton("OK, take this");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setPicture(ccPicture);
				ccDialog.dispose();
			}
		});
		buttonRow.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ccDialog.dispose();
			}
		});
		buttonRow.add(cancelButton);

		// Set the preferred size of the dialog
		int width = 600;
		int height = 500;
		ccDialog.setSize(width, height);
		ccDialog.setPreferredSize(new Dimension(width, height));

		return ccDialog;
	}

	private void createChannelWidget(char which, int pos, JDialog parent) {

		JPanel innerParent = new JPanel();

		JLabel explanationLabel = new JLabel();
		explanationLabel.setText("Channel " + which + ": Base on ");
		innerParent.add(explanationLabel);

		// TODO :: replace by dropdown containing
		// 0, R, G, B, R+G, R+B, G+B, R+G+B, 1
		final JTextField basicField = new JTextField();
		basicField.setText("" + which);
		basicField.setPreferredSize(new Dimension(75, 20));
		innerParent.add(basicField);
		basicFields[pos] = basicField;

		JLabel explanationLabel2 = new JLabel();
		explanationLabel2.setText(", then multiply by ");
		innerParent.add(explanationLabel2);

		final JTextField multiplierField = new JTextField();
		multiplierField.setText("1.0");
		multiplierField.setPreferredSize(new Dimension(75, 20));
		innerParent.add(multiplierField);
		multiplierFields[pos] = multiplierField;

		parent.add(innerParent, new Arrangement(0, pos, 1.0, 0.0));
	}

	private void applyPreview() {
		ccPicture = gui.getPicture();
		String basicR = basicFields[0].getText();
		String basicG = basicFields[1].getText();
		String basicB = basicFields[2].getText();
		double modR = Double.parseDouble(multiplierFields[0].getText());
		double modG = Double.parseDouble(multiplierFields[1].getText());
		double modB = Double.parseDouble(multiplierFields[2].getText());
		ccPicture.editChannels(basicR, modR, basicG, modG, basicB, modB);
		ccImageViewer.setImage(ccPicture.getAwtImage());
		ccImageViewerLabel.repaint();
	}

	public void show() {
		GuiUtils.centerAndShowWindow(ccDialog);

		applyPreview();
	}

}
