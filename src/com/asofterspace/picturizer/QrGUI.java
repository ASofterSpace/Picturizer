/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.barcodes.QrCode;
import com.asofterspace.toolbox.barcodes.QrCodeFactory;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.Utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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


public class QrGUI {

	private GUI gui;

	private JDialog qrDialog;

	private ImageIcon qrImageViewer;

	private Image qrPicture;


	public QrGUI(GUI gui) {

		this.gui = gui;

		this.qrDialog = createGUI();
	}

	private JDialog createGUI() {

		// Create the window
		final JDialog qrDialog = new JDialog(gui.getMainFrame(), "Create QR Code", true);
		GridLayout qrDialogLayout = new GridLayout(4, 1);
		qrDialogLayout.setVgap(8);
		qrDialog.setLayout(qrDialogLayout);
		qrDialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Populate the window
		JLabel explanationLabel = new JLabel();
		explanationLabel.setText("Enter the text content of the QR code here:");
		qrDialog.add(explanationLabel);

		final JTextField qrTextContent = new JTextField();
		qrDialog.add(qrTextContent);

		qrTextContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				QrCode qrCode = QrCodeFactory.createFromString(qrTextContent.getText());
				qrPicture = qrCode.toImage();
				qrImageViewer.setImage(qrPicture.getAwtImage());
				qrDialog.repaint();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		qrImageViewer = new ImageIcon();
		JLabel qrImageViewerLabel = new JLabel(qrImageViewer);
		qrDialog.add(qrImageViewerLabel);

		JPanel buttonRow = new JPanel();
		GridLayout buttonRowLayout = new GridLayout(1, 2);
		buttonRowLayout.setHgap(8);
		buttonRow.setLayout(buttonRowLayout);
		qrDialog.add(buttonRow);

		JButton okButton = new JButton("OK, take this");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setPicture(qrPicture);
			}
		});
		buttonRow.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qrDialog.dispose();
			}
		});
		buttonRow.add(cancelButton);

		// Set the preferred size of the dialog
		int width = 600;
		int height = 300;
		qrDialog.setSize(width, height);
		qrDialog.setPreferredSize(new Dimension(width, height));

		return qrDialog;
	}

	public void show() {
		GuiUtils.centerAndShowWindow(qrDialog);
	}

}
