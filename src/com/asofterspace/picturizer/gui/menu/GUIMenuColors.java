/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.ColorPickerGUI;
import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.picturizer.gui.Tool;
import com.asofterspace.toolbox.images.ColorRGBA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuColors {

	private final static String TOOL_TEXTS_PIPETTE_FG = "Set Foreground to a Particular Color (Pipette Tool)";
	private final static String TOOL_TEXTS_PIPETTE_BG = "Set Background to a Particular Color (Pipette Tool)";

	private ColorPickerGUI colorPickerGUI = null;

	private static boolean colorToBePickedIsForeground = true;

	private JMenuItem setForegroundToPipette;
	private JMenuItem setForegroundToPipette4;
	private JMenuItem setForegroundToPipette12;
	private JMenuItem setBackgroundToPipette;


	public JMenu createMenu(GUI gui) {

		JMenu colors = new JMenu("Colors");

		JMenuItem switchForeAndBack = new JMenuItem("Switch Foreground and Background Colors");
		switchForeAndBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.switchForeAndBackColor();
			}
		});
		colors.add(switchForeAndBack);

		colors.addSeparator();

		setForegroundToPipette = new JMenuItem(TOOL_TEXTS_PIPETTE_FG);
		setForegroundToPipette.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.PIPETTE_FG, 1);
			}
		});
		colors.add(setForegroundToPipette);

		setForegroundToPipette4 = new JMenuItem(TOOL_TEXTS_PIPETTE_FG + " + 4 Pixels Around");
		setForegroundToPipette4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.PIPETTE_FG, 5);
			}
		});
		colors.add(setForegroundToPipette4);

		setForegroundToPipette12 = new JMenuItem(TOOL_TEXTS_PIPETTE_FG + " + 12 Pixels Around");
		setForegroundToPipette12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.PIPETTE_FG, 13);
			}
		});
		colors.add(setForegroundToPipette12);

		setBackgroundToPipette = new JMenuItem(TOOL_TEXTS_PIPETTE_BG);
		setBackgroundToPipette.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.PIPETTE_BG, 1);
			}
		});
		colors.add(setBackgroundToPipette);

		JMenuItem setForegroundToColorPickerGUI = new JMenuItem("Set Foreground to a Particular Color (GUI Menu)");
		setForegroundToColorPickerGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorToBePickedIsForeground = true;
				if (colorPickerGUI == null) {
					colorPickerGUI = new ColorPickerGUI(gui);
				}
				colorPickerGUI.show(gui.getForegroundColor());
			}
		});
		colors.add(setForegroundToColorPickerGUI);

		JMenuItem setBackgroundToColorPickerGUI = new JMenuItem("Set Background to a Particular Color (GUI Menu)");
		setBackgroundToColorPickerGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorToBePickedIsForeground = false;
				if (colorPickerGUI == null) {
					colorPickerGUI = new ColorPickerGUI(gui);
				}
				colorPickerGUI.show(gui.getBackgroundColor());
			}
		});
		colors.add(setBackgroundToColorPickerGUI);

		colors.addSeparator();

		JMenuItem setForegroundToMostCommon = new JMenuItem("Set Foreground to Most Common Color");
		setForegroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setForegroundColor(gui.getPicture().bake().getMostCommonColor());
			}
		});
		colors.add(setForegroundToMostCommon);

		JMenuItem setBackgroundToMostCommon = new JMenuItem("Set Background to Most Common Color");
		setBackgroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setBackgroundColor(gui.getPicture().bake().getMostCommonColor());
			}
		});
		colors.add(setBackgroundToMostCommon);

		JMenuItem setForegroundToMostCommonSur = new JMenuItem("Set Foreground to Most Common Surrounding Color");
		setForegroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setForegroundColor(gui.getPicture().bake().getMostCommonSurroundingColor());
			}
		});
		colors.add(setForegroundToMostCommonSur);

		JMenuItem setBackgroundToMostCommonSur = new JMenuItem("Set Background to Most Common Surrounding Color");
		setBackgroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setBackgroundColor(gui.getPicture().bake().getMostCommonSurroundingColor());
			}
		});
		colors.add(setBackgroundToMostCommonSur);

		return colors;
	}

	public static void setPickedColor(GUI gui, ColorRGBA newColor) {
		if (colorToBePickedIsForeground) {
			gui.setForegroundColor(newColor);
		} else {
			gui.setBackgroundColor(newColor);
		}
	}

	public void refreshTools(Tool activeTool, int activeToolSize) {
		GUIMenuTools.adjustToolTitle(setForegroundToPipette, TOOL_TEXTS_PIPETTE_FG, (activeTool == Tool.PIPETTE_FG) && (activeToolSize == 1));
		GUIMenuTools.adjustToolTitle(setForegroundToPipette4, TOOL_TEXTS_PIPETTE_FG + " + 4 Pixels Around", (activeTool == Tool.PIPETTE_FG) && (activeToolSize == 5));
		GUIMenuTools.adjustToolTitle(setForegroundToPipette12, TOOL_TEXTS_PIPETTE_FG + " + 12 Pixels Around", (activeTool == Tool.PIPETTE_FG) && (activeToolSize == 13));
		GUIMenuTools.adjustToolTitle(setBackgroundToPipette, TOOL_TEXTS_PIPETTE_BG, activeTool == Tool.PIPETTE_BG);
	}
}
