/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuInvert {

	public JMenu createMenu(GUI gui) {

		JMenu invert = new JMenu("Invert");

		JMenuItem invertColors = new JMenuItem("Invert Colors");
		invertColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().invert();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		invert.add(invertColors);

		JMenuItem invertBrightness = new JMenuItem("Invert Brightness (Keeping Colors, Approach 1)");
		invertBrightness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().invertBrightness1();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		invert.add(invertBrightness);

		JMenuItem invertBrightness2 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 2)");
		invertBrightness2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().invertBrightness2();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		invert.add(invertBrightness2);

		JMenuItem invertBrightness3 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 3)");
		invertBrightness3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().invertBrightness3();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		invert.add(invertBrightness3);

		JMenuItem invertBrightness4 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 4)");
		invertBrightness4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().invertBrightness4();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		invert.add(invertBrightness4);

		return invert;
	}
}
