/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

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


	JMenu createMenu(GUI gui) {

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

				gui.getPicture().bake().copy(top, right, bottom, left).copyToClipboard();
			}
		});
		edit.add(copyClickedArea);

		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setPicture(Image.createFromClipboard());
			}
		});
		edit.add(paste);

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

		JMenuItem turnLeft = new JMenuItem("Turn Left");
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

		JMenuItem turnRight = new JMenuItem("Turn Right");
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

		JMenuItem reflectHorizontally = new JMenuItem("Reflect Horizontally");
		reflectHorizontally.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().reflectHorizontally();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(reflectHorizontally);

		JMenuItem reflectVertically = new JMenuItem("Reflect Vertically");
		reflectVertically.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().reflectVertically();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		edit.add(reflectVertically);

		return edit;
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
