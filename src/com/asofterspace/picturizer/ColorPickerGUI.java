/**
 * Unlicensed code created by A Softer Space, 2023
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.ColorRGBA;
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


public class ColorPickerGUI {

	private GUI gui;

	private JDialog ccDialog;

	private JTextField inputField;


	public ColorPickerGUI(GUI gui) {

		this.gui = gui;

		this.inputField = new JTextField();

		this.ccDialog = createGUI();
	}

	private JDialog createGUI() {

		// Create the window
		final JDialog ccDialog = new JDialog(gui.getMainFrame(), "Pick a Color", true);
		GridBagLayout ccDialogLayout = new GridBagLayout();
		ccDialog.setLayout(ccDialogLayout);
		ccDialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Populate the window
		JLabel explanationLabel = new JLabel();
		explanationLabel.setText("Enter #RGB, #RRGGBB, #RRGGBBAA, rgb(r,g,b) or rgba(r,g,b,a):");
		ccDialog.add(explanationLabel, new Arrangement(0, 0, 1.0, 0.0));

		inputField.setPreferredSize(new Dimension(75, 20));
		ccDialog.add(inputField, new Arrangement(0, 1, 1.0, 0.0));

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 3);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		ccDialog.add(buttonRow, new Arrangement(0, 2, 1.0, 0.0));

		JButton okButton = new JButton("OK, take this");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.guiMenuColors.setPickedColor(gui, getColor());
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

	private ColorRGBA getColor() {
		String colorStr = inputField.getText();
		return ColorRGBA.fromString(colorStr);
	}

	public void show(ColorRGBA color) {
		GuiUtils.centerAndShowWindow(ccDialog);
		if (color != null) {
			inputField.setText(color.toString());
		}
	}

}
