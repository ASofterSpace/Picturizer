/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.ChannelChangeGUI;
import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.toolbox.gui.GuiUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuChannels {

	public final static String ADJUST_CHANNEL_GUI_STR = "Adjust Channels Manually in Detail...";

	private ChannelChangeGUI channelChangeGUI = null;


	public JMenu createMenu(GUI gui) {

		JMenu adjustColors = new JMenu("Channels");

		JMenuItem removeAbsoluteColors = new JMenuItem("Remove Colors by Absolute Brightness (to Grayscale)");
		removeAbsoluteColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().removeColors();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(removeAbsoluteColors);

		JMenuItem removePerceivedColors = new JMenuItem("Remove Colors by Perceived Brightness (to Grayscale)");
		removePerceivedColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().removePerceivedColors();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(removePerceivedColors);

		JMenuItem removeAbsoluteColorsBW = new JMenuItem("Remove Colors by Absolute Brightness (to Black/White)");
		removeAbsoluteColorsBW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().removeColors();
				gui.getCurrentImageLayer().getImage().makeBlackAndWhite();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(removeAbsoluteColorsBW);

		JMenuItem removePerceivedColorsBW = new JMenuItem("Remove Colors by Perceived Brightness (to Black/White)");
		removePerceivedColorsBW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().removePerceivedColors();
				gui.getCurrentImageLayer().getImage().makeBlackAndWhite();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(removePerceivedColorsBW);

		adjustColors.addSeparator();

		JMenuItem headline1 = new JMenuItem("RGB to RGBA:");
		adjustColors.add(headline1);

		JMenuItem extractBlackToAlpha = new JMenuItem("Extract Black to Alpha");
		extractBlackToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().extractBlackToAlpha();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(extractBlackToAlpha);

		JMenuItem extractWhiteToAlpha = new JMenuItem("Extract White to Alpha");
		extractWhiteToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().extractWhiteToAlpha();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(extractWhiteToAlpha);

		JMenuItem extractBgColToAlpha = new JMenuItem("Extract Background Color to Alpha");
		extractBgColToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().extractBackgroundColorToAlpha();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(extractBgColToAlpha);

		adjustColors.addSeparator();

		JMenuItem headline2 = new JMenuItem("RGBA to RGB:");
		adjustColors.add(headline2);

		JMenuItem removeAlpha = new JMenuItem("Just Remove Alpha Channel");
		removeAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().removeAlpha();
				gui.setPictureUndoTakenCareOf(gui.getPicture());
			}
		});
		adjustColors.add(removeAlpha);

		JMenuItem bakeCurrentView = new JMenuItem("Remove Alpha Channel by Baking In Current Background View");
		bakeCurrentView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				gui.saveCurPicForUndo();
				gui.getCurrentImageLayer().getImage().bakeAlpha(new ColorRGBA(imageViewerLabel.getBackground()));
				gui.setPictureUndoTakenCareOf(gui.getPicture());
				*/
				GuiUtils.complain("TODO: this needs more logic now with layers - instead of baking in bg color, we should get all layers up to this one, and bake the layers, and extract? but still on that bg color below it all? arghs!");
			}
		});
		adjustColors.add(bakeCurrentView);

		adjustColors.addSeparator();

		JMenuItem channelAdjust = new JMenuItem(ADJUST_CHANNEL_GUI_STR);
		channelAdjust.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (channelChangeGUI == null) {
					channelChangeGUI = new ChannelChangeGUI(gui);
				}
				channelChangeGUI.show();
			}
		});
		adjustColors.add(channelAdjust);

		return adjustColors;
	}
}
