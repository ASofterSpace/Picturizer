/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuDarkenBrighten {

	JMenu createMenu(GUI gui) {

		JMenu darkenBrighten = new JMenu("Darken / Brighten");

		JMenuItem curMenuItem;

		curMenuItem = new JMenuItem("0.25 - Darken Strongly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().editChannels("R", 0.25, "G", 0.25, "B", 0.25);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("0.75 - Darken Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().editChannels("R", 0.75, "G", 0.75, "B", 0.75);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.25 - Brighten Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().editChannels("R", 1.25, "G", 1.25, "B", 1.25);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.75 - Brighten Strongly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().editChannels("R", 1.75, "G", 1.75, "B", 1.75);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.75 - Brighten Strongly Above Brightness Cutoff");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				int cutoff = 256+256;
				gui.getCurrentImageLayer().getImage().editChannelsAboveCutoff(
					"R", 1.75, "G", 1.75, "B", 1.75, ColorRGBA.DEFAULT_ALLOW_OVERFLOW, cutoff);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		darkenBrighten.add(curMenuItem);

		return darkenBrighten;
	}
}
