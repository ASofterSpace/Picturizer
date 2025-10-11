/**
 * Unlicensed code created by A Softer Space, 2024
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui;

import com.asofterspace.picturizer.Picturizer;
import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.utils.StrUtils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ExpandShrinkGUI {

	private GUI gui;

	private JDialog dialog;

	private ColorRGBA foregroundColor;
	private ColorRGBA backgroundColor;

	private Image baseImage;

	private JTextField inputFieldWidth;
	private JTextField inputFieldHeight;
	private JTextField inputFieldTop;
	private JTextField inputFieldLeft;
	private JTextField inputFieldRight;
	private JTextField inputFieldBottom;

	// tracks when the last self-created input was made, so that we only react to human inputs
	// (when the difference of this to now is high), but not to listeners to our own changes
	// (when the difference is low)
	private long lastSelfMadeInputTime = 0;
	private final static int OFFSET_TO_PREVENT_SELF_CALLING_MS = 64;


	public ExpandShrinkGUI(GUI gui) {

		this.gui = gui;

		createGUI();
	}

	private JDialog createGUI() {

		DocumentListener refreshTopLeftRightBottomListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshTopLeftRightBottom();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshTopLeftRightBottom();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshTopLeftRightBottom();
			}
		};

		DocumentListener refreshWidthHeightListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshWidthHeight();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshWidthHeight();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshWidthHeight();
			}
		};

		// Create the window
		this.dialog = new JDialog(gui.getMainFrame(), Picturizer.PROGRAM_TITLE + " - Expand/Shrink", true);
		GridBagLayout dialogLayout = new GridBagLayout();
		dialog.setLayout(dialogLayout);
		dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Populate the window like this:

		// Expand / Shrink to width:
		// [          ]
		// Expand / Shrink to height:
		// [          ]
		// Expand / Shrink by:
		//    [ ]
		// [ ]   [ ]
		//    [ ]

		JLabel explanationLabel = new JLabel();
		explanationLabel.setText("Expand / Shrink to width:");
		dialog.add(explanationLabel, new Arrangement(0, 0, 1.0, 0.0));

		inputFieldWidth = new JTextField();
		inputFieldWidth.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldWidth, new Arrangement(0, 1, 1.0, 0.0));
		inputFieldWidth.getDocument().addDocumentListener(refreshTopLeftRightBottomListener);

		explanationLabel = new JLabel();
		explanationLabel.setText("Expand / Shrink to height:");
		dialog.add(explanationLabel, new Arrangement(0, 2, 1.0, 0.0));

		inputFieldHeight = new JTextField();
		inputFieldHeight.setPreferredSize(new Dimension(75, 20));
		dialog.add(inputFieldHeight, new Arrangement(0, 3, 1.0, 0.0));
		inputFieldHeight.getDocument().addDocumentListener(refreshTopLeftRightBottomListener);

		explanationLabel = new JLabel();
		explanationLabel.setText("Expand / Shrink by:");
		dialog.add(explanationLabel, new Arrangement(0, 4, 1.0, 0.0));

		JPanel expShrPanel = new JPanel();
		GridLayout expShrPanelLayout = new GridLayout(3, 3);
		expShrPanelLayout.setHgap(8);
		expShrPanelLayout.setVgap(8);
		expShrPanel.setLayout(expShrPanelLayout);
		dialog.add(expShrPanel, new Arrangement(0, 5, 1.0, 0.0));

		expShrPanel.add(new JPanel());

		inputFieldTop = new JTextField();
		inputFieldTop.setPreferredSize(new Dimension(75, 20));
		expShrPanel.add(inputFieldTop);
		inputFieldTop.getDocument().addDocumentListener(refreshWidthHeightListener);

		expShrPanel.add(new JPanel());

		inputFieldLeft = new JTextField();
		inputFieldLeft.setPreferredSize(new Dimension(75, 20));
		expShrPanel.add(inputFieldLeft);
		inputFieldLeft.getDocument().addDocumentListener(refreshWidthHeightListener);

		expShrPanel.add(new JPanel());

		inputFieldRight = new JTextField();
		inputFieldRight.setPreferredSize(new Dimension(75, 20));
		expShrPanel.add(inputFieldRight);
		inputFieldRight.getDocument().addDocumentListener(refreshWidthHeightListener);

		expShrPanel.add(new JPanel());

		inputFieldBottom = new JTextField();
		inputFieldBottom.setPreferredSize(new Dimension(75, 20));
		expShrPanel.add(inputFieldBottom);
		inputFieldBottom.getDocument().addDocumentListener(refreshWidthHeightListener);

		expShrPanel.add(new JPanel());

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 3);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		dialog.add(buttonRow, new Arrangement(0, 6, 1.0, 0.0));

		JButton okButton = new JButton("OK, make adjustments");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer top = StrUtils.strToInt(inputFieldTop.getText(), 0);
				Integer left = StrUtils.strToInt(inputFieldLeft.getText(), 0);
				Integer right = StrUtils.strToInt(inputFieldRight.getText(), 0);
				Integer bottom = StrUtils.strToInt(inputFieldBottom.getText(), 0);

				if ((top != null) && (left != null) &&
					(right != null) && (bottom != null)) {
					int newWidth = baseImage.getWidth() + left + right;
					int newHeight = baseImage.getHeight() + top + bottom;
					Image gridPic = new Image(newWidth, newHeight, backgroundColor);

					gridPic.draw(baseImage, left, top);

					gui.setPicture(gridPic);
					dialog.dispose();
				}
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

	public void show(ColorRGBA foregroundColor, ColorRGBA backgroundColor, Image currentImg) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.baseImage = currentImg;

		GuiUtils.centerAndShowWindow(dialog);

		// use existing image width and height
		inputFieldWidth.setText("" + currentImg.getWidth());
		inputFieldHeight.setText("" + currentImg.getHeight());
		inputFieldTop.setText("0");
		inputFieldLeft.setText("0");
		inputFieldRight.setText("0");
		inputFieldBottom.setText("0");
	}

	private void refreshTopLeftRightBottom() {

		long curTimeMillis = System.currentTimeMillis();
		if (curTimeMillis - lastSelfMadeInputTime < OFFSET_TO_PREVENT_SELF_CALLING_MS) {
			return;
		}
		lastSelfMadeInputTime = curTimeMillis;

		Integer left = StrUtils.strToInt(inputFieldLeft.getText(), 0);
		Integer width = StrUtils.strToInt(inputFieldWidth.getText(), 0);
		if ((left != null) && (width != null)) {
			int right = width - baseImage.getWidth() - left;
			inputFieldRight.setText("" + right);
		}

		Integer top = StrUtils.strToInt(inputFieldTop.getText(), 0);
		Integer height = StrUtils.strToInt(inputFieldHeight.getText(), 0);
		if ((top != null) && (height != null)) {
			int bottom = height - baseImage.getHeight() - top;
			inputFieldBottom.setText("" + bottom);
		}
	}

	private void refreshWidthHeight() {

		long curTimeMillis = System.currentTimeMillis();
		if (curTimeMillis - lastSelfMadeInputTime < OFFSET_TO_PREVENT_SELF_CALLING_MS) {
			return;
		}
		lastSelfMadeInputTime = curTimeMillis;

		Integer left = StrUtils.strToInt(inputFieldLeft.getText(), 0);
		Integer right = StrUtils.strToInt(inputFieldRight.getText(), 0);
		if ((left != null) && (right != null)) {
			int newWidth = baseImage.getWidth() + left + right;
			inputFieldWidth.setText("" + newWidth);
		}

		Integer top = StrUtils.strToInt(inputFieldTop.getText(), 0);
		Integer bottom = StrUtils.strToInt(inputFieldBottom.getText(), 0);
		if ((top != null) && (bottom != null)) {
			int newHeight = baseImage.getHeight() + top + bottom;
			inputFieldHeight.setText("" + newHeight);
		}
	}
}
