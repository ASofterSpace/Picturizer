/**
 * Unlicensed code created by A Softer Space, 2024
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.utils.StrUtils;
import com.asofterspace.toolbox.Utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ResizeGUI {

	private GUI gui;

	private JDialog dialog;

	private Image baseImage;

	private boolean resample;

	private JTextField inputFieldWidth;
	private JTextField inputFieldHeight;


	public ResizeGUI(GUI gui) {

		this.gui = gui;

		createGUI();
	}

	private JDialog createGUI() {

		// Create the window
		this.dialog = new JDialog(gui.getMainFrame(), "", true);
		GridBagLayout dialogLayout = new GridBagLayout();
		dialog.setLayout(dialogLayout);
		dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		JLabel explanationLabel = new JLabel();
		explanationLabel.setText("Expand / Shrink to width:");
		dialog.add(explanationLabel, new Arrangement(0, 0, 1.0, 0.0));

		inputFieldWidth = new JTextField();
		inputFieldWidth.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldWidth, new Arrangement(0, 1, 1.0, 0.0));

		explanationLabel = new JLabel();
		explanationLabel.setText("Expand / Shrink to height:");
		dialog.add(explanationLabel, new Arrangement(0, 2, 1.0, 0.0));

		inputFieldHeight = new JTextField();
		inputFieldHeight.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldHeight, new Arrangement(0, 3, 1.0, 0.0));

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 3);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		dialog.add(buttonRow, new Arrangement(0, 4, 1.0, 0.0));

		JButton okButton = new JButton("OK, make adjustments");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newWidth = StrUtils.strToInt(inputFieldWidth.getText(), 0);
				int newHeight = StrUtils.strToInt(inputFieldHeight.getText(), 0);

				Image newPic = baseImage.copy();
				if (resample) {
					newPic.resample(newWidth, newHeight);
				} else {
					newPic.resize(newWidth, newHeight);
				}
				gui.setPicture(newPic);
				dialog.dispose();
			}
		});
		buttonRow.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		buttonRow.add(cancelButton);

		// Set the preferred size of the dialog
		int width = 600;
		int height = 500;
		dialog.setSize(width, height);
		dialog.setPreferredSize(new Dimension(width, height));

		return dialog;
	}

	public void show(Image currentImg, boolean resample) {
		this.baseImage = currentImg;
		this.resample = resample;

		GuiUtils.centerAndShowWindow(dialog);

		// use existing image width and height
		inputFieldWidth.setText("" + currentImg.getWidth());
		inputFieldHeight.setText("" + currentImg.getHeight());
	}
}
