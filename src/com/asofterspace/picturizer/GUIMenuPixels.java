/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuPixels {

	JMenu createMenu(GUI gui) {

		JMenu adjustPixels = new JMenu("Pixels");

		JMenuItem replaceEvWithForeground = new JMenuItem("Replace Everything with Foreground Color");
		replaceEvWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().clear(gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceEvWithForeground);

		JMenuItem replaceEvWithBackground = new JMenuItem("Replace Everything with Background Color");
		replaceEvWithBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().clear(gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceEvWithBackground);

		adjustPixels.addSeparator();

		JMenuItem replaceBackgroundForeground = new JMenuItem("Replace Background Color with Foreground Color");
		replaceBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(gui.getBackgroundColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceBackgroundForeground);

		JMenuItem replaceVCBackgroundForeground = new JMenuItem("Replace Very-Close-to-Background Color with Foreground Color");
		replaceVCBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(gui.getBackgroundColor(), gui.getForegroundColor(), 24);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceVCBackgroundForeground);

		JMenuItem replaceCBackgroundForeground = new JMenuItem("Replace Close-to-Background Color with Foreground Color");
		replaceCBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(gui.getBackgroundColor(), gui.getForegroundColor(), 64);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceCBackgroundForeground);

		JMenuItem replaceSBackgroundForeground = new JMenuItem("Replace Similar-ish-to-Background Color with Foreground Color");
		replaceSBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(gui.getBackgroundColor(), gui.getForegroundColor(), 128);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceSBackgroundForeground);

		JMenuItem replaceVSBackgroundForeground = new JMenuItem("Replace Vaguely-Similar-ish-to-Background Color with Foreground Color");
		replaceVSBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(gui.getBackgroundColor(), gui.getForegroundColor(), 255);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceVSBackgroundForeground);

		adjustPixels.addSeparator();

		JMenuItem replaceAnythingButFgWithBg = new JMenuItem("Replace Anything but Foreground Color with Background Color");
		replaceAnythingButFgWithBg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColorsExcept(gui.getForegroundColor(), gui.getBackgroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceAnythingButFgWithBg);

		JMenuItem replaceAnythingButMcWithFg = new JMenuItem("Replace Anything but Most Common Color with Foreground Color");
		replaceAnythingButMcWithFg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColorsExcept(
					gui.getCurrentImageLayer().getImage().getMostCommonColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceAnythingButMcWithFg);

		adjustPixels.addSeparator();

		JMenuItem replaceMostCommonForeground = new JMenuItem("Replace Most Common Color with Foreground Color");
		replaceMostCommonForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(
					gui.getCurrentImageLayer().getImage().getMostCommonColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceMostCommonForeground);

		JMenuItem replaceMostCommonSurroundingForeground = new JMenuItem("Replace Most Common Surrounding Color with Foreground Color");
		replaceMostCommonSurroundingForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceColors(
					gui.getCurrentImageLayer().getImage().getMostCommonSurroundingColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceMostCommonSurroundingForeground);

		adjustPixels.addSeparator();

		JMenuItem replaceStragglersWithForeground = new JMenuItem("Replace Stragglers (Single Pixels) based on Background Color with Foreground Color");
		replaceStragglersWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceStragglersWith(gui.getBackgroundColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceStragglersWithForeground);

		JMenuItem replaceStragglersIshWithForeground = new JMenuItem("Replace Stragglers-ish (Single-ish Pixels) based on Background Color with Foreground Color");
		replaceStragglersIshWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().replaceStragglersIshWith(gui.getBackgroundColor(), gui.getForegroundColor());
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustPixels.add(replaceStragglersIshWithForeground);

		adjustPixels.addSeparator();

		addPixelLevelDiffMapButtons(gui, adjustPixels);

		adjustPixels.addSeparator();

		addPixelateButton(gui, adjustPixels, 2);
		addPixelateButton(gui, adjustPixels, 4);
		addPixelateButton(gui, adjustPixels, 8);
		addPixelateButton(gui, adjustPixels, 16);
		addPixelateButton(gui, adjustPixels, 32);
		addPixelateButton(gui, adjustPixels, 64);

		return adjustPixels;
	}

	void addPixelLevelDiffMapButtons(GUI gui, JMenuItem parentItem) {

		JMenuItem curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().createMapOfDifferences();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		parentItem.add(curMenuItem);

		curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences (Black/White)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().createMapOfDifferencesBW();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		parentItem.add(curMenuItem);
	}

	private void addPixelateButton(GUI gui, JMenuItem parentItem, int size) {

		JMenuItem curMenuItem = new JMenuItem("Pixelate (to "+size+"px x "+size+"px)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().pixelate(size);
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		parentItem.add(curMenuItem);
	}
}
