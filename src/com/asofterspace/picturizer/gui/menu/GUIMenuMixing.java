/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.toolbox.images.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuMixing {

	public JMenu createMenu(GUI gui) {

		JMenu mixing = new JMenu("Mixing");

		JMenuItem curMenuItem;

		curMenuItem = new JMenuItem("Mix in Previous Image 90:10 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.1f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 75:25 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.25f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 66:33 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.3333f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 60:40 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.4f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 50:50 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.5f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 40:60 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.6f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 33:66 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.6666f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 25:75 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.75f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 10:90 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImage(otherPic, 0.9f);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mix in Previous Image from Left (old) to Right (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageLeftToRight(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Right (old) to Left (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageRightToLeft(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Top (old) to Bottom (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageTopToBottom(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Bottom (old) to Top (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageBottomToTop(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mix in Previous Image (applying min)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageMin(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image (applying max)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().intermixImageMax(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mask out Previous Image (every same pixel becomes BG color)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().maskOutImage(otherPic, gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Interlace Previous Image 50:50 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = gui.getUndoablePicture().bake();
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().interlaceImage(otherPic);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		mixing.add(curMenuItem);

		return mixing;
	}
}
