/**
 * Unlicensed code created by A Softer Space, 2023
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.ColorRGBA;
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


public class CreateGridGUI {

	private GUI gui;

	private JDialog dialog;

	private ColorRGBA foregroundColor;
	private ColorRGBA backgroundColor;

	private JTextField inputFieldAmountHorz;
	private JTextField inputFieldAmountVert;
	private JTextField inputFieldWidth;
	private JTextField inputFieldHeight;
	private JTextField inputFieldLineThickness;


	public CreateGridGUI(GUI gui) {

		this.gui = gui;

		this.inputFieldAmountHorz = new JTextField();
		this.inputFieldAmountVert = new JTextField();
		this.inputFieldWidth = new JTextField();
		this.inputFieldHeight = new JTextField();
		this.inputFieldLineThickness = new JTextField();

		this.dialog = createGUI();
	}

	private JDialog createGUI() {

		// Create the window
		final JDialog dialog = new JDialog(gui.getMainFrame(), "Create an Empty Grid to Align Images", true);
		GridBagLayout dialogLayout = new GridBagLayout();
		dialog.setLayout(dialogLayout);
		dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Populate the window
		JLabel explanationLabelHV1 = new JLabel();
		explanationLabelHV1.setText("Enter the amount of images horizontally:");
		dialog.add(explanationLabelHV1, new Arrangement(0, 0, 1.0, 0.0));

		inputFieldAmountHorz.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldAmountHorz, new Arrangement(0, 1, 1.0, 0.0));

		JLabel explanationLabelHV2 = new JLabel();
		explanationLabelHV2.setText("Enter the amount of images vertically:");
		dialog.add(explanationLabelHV2, new Arrangement(0, 2, 1.0, 0.0));

		inputFieldAmountVert.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldAmountVert, new Arrangement(0, 3, 1.0, 0.0));

		JLabel explanationLabel1 = new JLabel();
		explanationLabel1.setText("Enter the width of the individual pictures in px:");
		dialog.add(explanationLabel1, new Arrangement(0, 4, 1.0, 0.0));

		inputFieldWidth.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldWidth, new Arrangement(0, 5, 1.0, 0.0));

		JLabel explanationLabel2 = new JLabel();
		explanationLabel2.setText("Enter the height of the individual pictures in px:");
		dialog.add(explanationLabel2, new Arrangement(0, 6, 1.0, 0.0));

		inputFieldHeight.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldHeight, new Arrangement(0, 7, 1.0, 0.0));

		JLabel explanationLabel3 = new JLabel();
		explanationLabel3.setText("Enter the width of gridlines between pictures in px:");
		dialog.add(explanationLabel3, new Arrangement(0, 8, 1.0, 0.0));

		inputFieldLineThickness.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldLineThickness, new Arrangement(0, 9, 1.0, 0.0));

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 3);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		dialog.add(buttonRow, new Arrangement(0, 10, 1.0, 0.0));

		JButton okButton = new JButton("OK, create grid");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer horz = StrUtils.strToInt(inputFieldAmountHorz.getText());
				Integer vert = StrUtils.strToInt(inputFieldAmountVert.getText());
				Integer width = StrUtils.strToInt(inputFieldWidth.getText());
				Integer height = StrUtils.strToInt(inputFieldHeight.getText());
				Integer lineThickness = StrUtils.strToInt(inputFieldLineThickness.getText());
				if ((horz != null) && (vert != null) &&
					(width != null) && (height != null) && (lineThickness != null)) {
					int fullWidth = (horz * width) + ((horz + 1) * lineThickness);
					int fullHeight = (vert * height) + ((vert + 1) * lineThickness);
					Image gridPic = new Image(fullWidth, fullHeight);
					gridPic.drawRectangle(0, 0, fullWidth - 1, fullHeight - 1, backgroundColor);
					for (int x = 0; x < horz; x++) {
						for (int y = 0; y < vert; y++) {
							int offsetX = lineThickness + (x * (width + lineThickness));
							int offsetY = lineThickness + (y * (height + lineThickness));
							gridPic.drawRectangle(offsetX, offsetY,
								offsetX + width - 1, offsetY + height - 1, foregroundColor);
						}
					}
					gui.setPicture(gridPic);
				}
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

	public void show(ColorRGBA foregroundColor, ColorRGBA backgroundColor) {
		GuiUtils.centerAndShowWindow(dialog);
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}

}
