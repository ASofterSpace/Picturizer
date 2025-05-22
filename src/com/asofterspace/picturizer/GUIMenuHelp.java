/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class GUIMenuHelp {

	JMenu createMenu(GUI gui) {

		JMenu huh = new JMenu("?");

		JMenuItem showFGColorCode = new JMenuItem("Show Foreground Color Code");
		showFGColorCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Hex Code: " + gui.getForegroundColor().toHexString() + "\n" +
					"rgba Code: " + gui.getForegroundColor().toString();
				JOptionPane.showMessageDialog(null, msg, "Color", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(showFGColorCode);

		JMenuItem showBGColorCode = new JMenuItem("Show Background Color Code");
		showBGColorCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Hex Code: " + gui.getBackgroundColor().toHexString() + "\n" +
					"rgba Code: " + gui.getBackgroundColor().toString();
				JOptionPane.showMessageDialog(null, msg, "Color", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(showBGColorCode);

		JMenuItem showSize = new JMenuItem("Show Picture Size");
		showSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Picture Area Width: " + gui.getPicture().getWidth() + " px\n" +
					"Picture Area Height: " + gui.getPicture().getHeight() + " px\n" +
					"Current Layer Width: " + gui.getPicture().getLayer(gui.getCurrentLayerIndex()).getWidth() + " px\n" +
					"Current Layer Height: " + gui.getPicture().getLayer(gui.getCurrentLayerIndex()).getHeight() + " px";
				JOptionPane.showMessageDialog(null, msg, "Size", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(showSize);

		huh.addSeparator();

		JMenuItem openConfigPath = new JMenuItem("Open Config Path");
		openConfigPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(gui.getConfiguration().getParentDirectory().getJavaFile());
				} catch (IOException ex) {
					// do nothing
				}
			}
		});
		huh.add(openConfigPath);

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String aboutMessage = "This is the " + Picturizer.PROGRAM_TITLE + ".\n" +
					"Version: " + Picturizer.VERSION_NUMBER + " (" + Picturizer.VERSION_DATE + ")\n" +
					"Brought to you by: A Softer Space";
				JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(about);

		return huh;
	}
}
