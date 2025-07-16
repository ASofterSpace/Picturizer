/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.ExpandShrinkGUI;
import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.picturizer.gui.ResizeGUI;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageMultiLayered;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class GUIMenuEdit {

	private ExpandShrinkGUI expandShrinkGUI = null;
	private ResizeGUI resizeGUI = null;


	public JMenu createMenu(GUI gui) {

		JMenu edit = new JMenu("Edit");

		JMenuItem undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.undo();
			}
		});
		edit.add(undo);

		JMenuItem redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.redo();
			}
		});
		edit.add(redo);

		edit.addSeparator();

		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getPicture().clear();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(clear);

		edit.addSeparator();

		JMenuItem copy = new JMenuItem("Copy All");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getPicture().bake().copyToClipboard();
			}
		});
		edit.add(copy);

		JMenuItem copyClickedArea = new JMenuItem("Copy Area Bounded by Last Two Clicks");
		copyClickedArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int prevClickX = gui.getPrevClickX();
				int prevClickY = gui.getPrevClickY();
				int lastClickX = gui.getLastClickX();
				int lastClickY = gui.getLastClickY();
				int top = Math.min(prevClickY, lastClickY);
				int right = Math.max(prevClickX, lastClickX);
				int bottom = Math.max(prevClickY, lastClickY);
				int left = Math.min(prevClickX, lastClickX);

				if ((right > left) && (bottom > top)) {
					gui.getPicture().bake().copy(top, right, bottom, left).copyToClipboard();
				} else {
					GuiUtils.complain("Cannot copy area of with zero width or height!");
				}
			}
		});
		edit.add(copyClickedArea);

		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image img = Image.createFromClipboard();
				if (img != null) {
					gui.setPicture(img);
				} else {
					complainAboutClipboard();
				}
			}
		});
		edit.add(paste);

		JMenuItem extractClickedArea = new JMenuItem("Extract Area Bounded by Last Two Clicks (Copy+Paste)");
		extractClickedArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int prevClickX = gui.getPrevClickX();
				int prevClickY = gui.getPrevClickY();
				int lastClickX = gui.getLastClickX();
				int lastClickY = gui.getLastClickY();
				int top = Math.min(prevClickY, lastClickY);
				int right = Math.max(prevClickX, lastClickX);
				int bottom = Math.max(prevClickY, lastClickY);
				int left = Math.min(prevClickX, lastClickX);

				if ((right > left) && (bottom > top)) {
					gui.setPicture(gui.getPicture().bake().copy(top, right, bottom, left));
				} else {
					GuiUtils.complain("Cannot copy area of with zero width or height!");
				}
			}
		});
		edit.add(extractClickedArea);

		edit.addSeparator();

		JMenuItem expandShrinkImgArea = new JMenuItem("Expand / Shrink Image Area");
		expandShrinkImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showExpandShrinkGUI(gui);
			}
		});
		edit.add(expandShrinkImgArea);

		JMenuItem resizeImgArea = new JMenuItem("Resize Image Area");
		resizeImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResizeGUI(gui, false);
			}
		});
		edit.add(resizeImgArea);

		JMenuItem resampleImgArea = new JMenuItem("Resample Image Area");
		resampleImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResizeGUI(gui, true);
			}
		});
		edit.add(resampleImgArea);

		edit.addSeparator();

		JMenuItem turnLeft22 = new JMenuItem("Turn Left 22.5° (all)");
		turnLeft22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.rotateLeft22(gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnLeft22);

		JMenuItem turnLeft45 = new JMenuItem("Turn Left 45° (all)");
		turnLeft45.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.rotateLeft45(gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnLeft45);

		JMenuItem turnLeft = new JMenuItem("Turn Left 90° (all)");
		turnLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.rotateLeft();
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnLeft);

		JMenuItem turnRight = new JMenuItem("Turn Right 90° (all)");
		turnRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.rotateRight();
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnRight);

		JMenuItem turnLeftCL22 = new JMenuItem("Turn Left 22.5° (current layer)");
		turnLeftCL22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().rotateLeft22(gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(turnLeftCL22);

		JMenuItem turnLeftCL45 = new JMenuItem("Turn Left 45° (current layer)");
		turnLeftCL45.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().rotateLeft45(gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(turnLeftCL45);

		JMenuItem turnLeftCL = new JMenuItem("Turn Left 90° (current layer)");
		turnLeftCL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().rotateLeft();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(turnLeftCL);

		JMenuItem turnRightCL = new JMenuItem("Turn Right 90° (current layer)");
		turnRightCL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().rotateRight();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(turnRightCL);

		edit.addSeparator();

		JMenuItem reflectHorizontally = new JMenuItem("Reflect Horizontally (all)");
		reflectHorizontally.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().reflectHorizontally();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(reflectHorizontally);

		JMenuItem reflectVertically = new JMenuItem("Reflect Vertically (all)");
		reflectVertically.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().reflectVertically();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(reflectVertically);

		JMenuItem reflectHorizontallyCL = new JMenuItem("Reflect Horizontally (current layer)");
		reflectHorizontallyCL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.reflectHorizontally();
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(reflectHorizontallyCL);

		JMenuItem reflectVerticallyCL = new JMenuItem("Reflect Vertically (current layer)");
		reflectVerticallyCL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				Image img = gui.getPicture().bake();
				img.reflectVertically();
				gui.setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(reflectVerticallyCL);

		return edit;
	}

	static void complainAboutClipboard() {
		GuiUtils.complain("Clipboard contents cannot be parsed as image!\n" +
			"(Clipboard is either empty or filled with horrors beyond our comprehension...)");
	}

	private void showExpandShrinkGUI(GUI gui) {

		if (expandShrinkGUI == null) {
			expandShrinkGUI = new ExpandShrinkGUI(gui);
		}

		expandShrinkGUI.show(gui.getForegroundColor(), gui.getBackgroundColor(), gui.getPicture().bake());
	}

	private void showResizeGUI(GUI gui, boolean resample) {

		if (resizeGUI == null) {
			resizeGUI = new ResizeGUI(gui);
		}

		resizeGUI.show(gui.getPicture().bake(), resample);
	}

}
