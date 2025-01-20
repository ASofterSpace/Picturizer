/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuDampen {

	JMenu createMenu(GUI gui) {

		JMenu dampen = new JMenu("Dampen");

		JMenuItem undampenStrongly = new JMenuItem("0.25 - Undampen Strongly");
		undampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(0.25f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(undampenStrongly);

		JMenuItem undampenWeakly = new JMenuItem("0.75 - Undampen Slightly");
		undampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(0.75f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(undampenWeakly);

		JMenuItem dampenWeakly1 = new JMenuItem("1.1 - Dampen Extremely Slightly");
		dampenWeakly1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(1.1f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(dampenWeakly1);

		JMenuItem dampenWeakly25 = new JMenuItem("1.25 - Dampen Very Slightly");
		dampenWeakly25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(1.25f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(dampenWeakly25);

		JMenuItem dampenWeakly = new JMenuItem("1.5 - Dampen Slightly");
		dampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(1.5f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(dampenWeakly);

		JMenuItem dampenStrongly = new JMenuItem("2.0 - Dampen Strongly");
		dampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().dampen(2);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		dampen.add(dampenStrongly);

		return dampen;
	}
}
