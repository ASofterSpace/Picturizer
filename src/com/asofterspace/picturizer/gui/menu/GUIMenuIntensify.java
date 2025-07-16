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


public class GUIMenuIntensify {

	public JMenu createMenu(GUI gui) {

		JMenu intensify = new JMenu("Intensify");

		JMenuItem curMenuItem;

		/*
		Farben intensivieren:
		p^[1] := max255((p^[1] * p^[1]) div 128);
		p^[2] := max255((p^[2] * p^[2]) div 128);
		p^[3] := max255((p^[3] * p^[3]) div 128);
		*/
		curMenuItem = new JMenuItem("Intensify");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intensify();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		intensify.add(curMenuItem);

		/*
		Farben leicht intensivieren:
		p^[1] := (max255((p^[1] * p^[1]) div 128) + p^[1]) div 2;
		p^[2] := (max255((p^[2] * p^[2]) div 128) + p^[2]) div 2;
		p^[3] := (max255((p^[3] * p^[3]) div 128) + p^[3]) div 2;
		*/
		curMenuItem = new JMenuItem("Intensify Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intensifySlightly();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		intensify.add(curMenuItem);

		// intensifies colors, and the ones that achieve black or white are set to that,
		// but all others are kept as before, so if it was somewhere in the middle before,
		// it just stays exactly there
		curMenuItem = new JMenuItem("Intensify Extremes, but Keep Non-Intense Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intensifyExtremes();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		intensify.add(curMenuItem);

		intensify.addSeparator();

		curMenuItem = new JMenuItem("Create Map of Extremes and Non-Intense Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().createMapOfExtremes(gui.getForegroundColor(), gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		intensify.add(curMenuItem);

		GUIMenuPixels.addPixelLevelDiffMapButtons(gui, intensify);

		return intensify;
	}
}
