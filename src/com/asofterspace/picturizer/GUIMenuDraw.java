/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.GuiUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuDraw {

	private final static String TOOL_TEXTS_PEN_FG = "Draw Using Pen with Foreground Color, Size: ";
	private final static String TOOL_TEXTS_LINES_FG = "Draw Using Lines with Foreground Color, Size: ";
	private final static String TOOL_TEXTS_FILL_FG = "Fill Connected Area (Same Color) with Foreground Color";
	private final static String TOOL_TEXTS_FILL_ROUGHLY_FG = "Fill Connected Area (Roughly Same Color) with Foreground Color";
	private final static String TOOL_TEXTS_RECTANGLE_FG = "Draw Rectangle with Foreground Color";
	private final static String TOOL_TEXTS_RECTANGLE_BG = "Draw Rectangle with Background Color";
	private final static String TOOL_TEXTS_AREA_FG = "Draw Area with Foreground Color";
	private final static String TOOL_TEXTS_AREA_BG = "Draw Area with Background Color";

	private JMenuItem drawPenFG1;
	private JMenuItem drawPenFG2;
	private JMenuItem drawPenFG4;
	private JMenuItem drawPenFG8;
	private JMenuItem drawLinesFG1;
	private JMenuItem drawLinesFG2;
	private JMenuItem drawLinesFG4;
	private JMenuItem drawLinesFG8;
	private JMenuItem fill;
	private JMenuItem fillRoughly;
	private JMenuItem drawRectangleFG;
	private JMenuItem drawRectangleBG;
	private JMenuItem drawAreaFG;
	private JMenuItem drawAreaBG;


	JMenu createMenu(GUI gui) {

		JMenu draw = new JMenu("Draw");

		drawPenFG1 = new JMenuItem(TOOL_TEXTS_PEN_FG+"1");
		drawPenFG1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_PEN_FG, 1);
			}
		});
		draw.add(drawPenFG1);

		drawPenFG2 = new JMenuItem(TOOL_TEXTS_PEN_FG+"2");
		drawPenFG2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_PEN_FG, 2);
			}
		});
		draw.add(drawPenFG2);

		drawPenFG4 = new JMenuItem(TOOL_TEXTS_PEN_FG+"4");
		drawPenFG4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_PEN_FG, 4);
			}
		});
		draw.add(drawPenFG4);

		drawPenFG8 = new JMenuItem(TOOL_TEXTS_PEN_FG+"8");
		drawPenFG8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiUtils.complain("Not yet implemented!");
			}
		});
		draw.add(drawPenFG8);

		draw.addSeparator();

		drawLinesFG1 = new JMenuItem(TOOL_TEXTS_LINES_FG+"1");
		drawLinesFG1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_LINES_FG, 1);
			}
		});
		draw.add(drawLinesFG1);

		drawLinesFG2 = new JMenuItem(TOOL_TEXTS_LINES_FG+"2");
		drawLinesFG2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_LINES_FG, 2);
			}
		});
		draw.add(drawLinesFG2);

		drawLinesFG4 = new JMenuItem(TOOL_TEXTS_LINES_FG+"4");
		drawLinesFG4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_LINES_FG, 4);
			}
		});
		draw.add(drawLinesFG4);

		drawLinesFG8 = new JMenuItem(TOOL_TEXTS_LINES_FG+"8");
		drawLinesFG8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiUtils.complain("Not yet implemented!");
			}
		});
		draw.add(drawLinesFG8);

		draw.addSeparator();

		fill = new JMenuItem(TOOL_TEXTS_FILL_FG);
		fill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.FILL_FG);
			}
		});
		draw.add(fill);

		fillRoughly = new JMenuItem(TOOL_TEXTS_FILL_ROUGHLY_FG);
		fillRoughly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.FILL_ROUGHLY_FG);
			}
		});
		draw.add(fillRoughly);

		draw.addSeparator();

		drawRectangleFG = new JMenuItem(TOOL_TEXTS_RECTANGLE_FG);
		drawRectangleFG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_RECTANGLE_FG);
			}
		});
		draw.add(drawRectangleFG);

		drawRectangleBG = new JMenuItem(TOOL_TEXTS_RECTANGLE_BG);
		drawRectangleBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_RECTANGLE_BG);
			}
		});
		draw.add(drawRectangleBG);

		drawAreaFG = new JMenuItem(TOOL_TEXTS_AREA_FG);
		drawAreaFG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_AREA_FG);
			}
		});
		draw.add(drawAreaFG);

		drawAreaBG = new JMenuItem(TOOL_TEXTS_AREA_BG);
		drawAreaBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(Tool.DRAW_AREA_BG);
			}
		});
		draw.add(drawAreaBG);

		return draw;
	}

	void refreshTools(Tool activeTool, int activeToolSize) {
		GUIMenuTools.adjustToolTitle(drawPenFG1, TOOL_TEXTS_PEN_FG + "1", (activeTool == Tool.DRAW_PEN_FG) && (activeToolSize == 1));
		GUIMenuTools.adjustToolTitle(drawPenFG2, TOOL_TEXTS_PEN_FG + "2", (activeTool == Tool.DRAW_PEN_FG) && (activeToolSize == 2));
		GUIMenuTools.adjustToolTitle(drawPenFG4, TOOL_TEXTS_PEN_FG + "4", (activeTool == Tool.DRAW_PEN_FG) && (activeToolSize == 4));
		GUIMenuTools.adjustToolTitle(drawPenFG8, TOOL_TEXTS_PEN_FG + "8", (activeTool == Tool.DRAW_PEN_FG) && (activeToolSize == 8));
		GUIMenuTools.adjustToolTitle(drawLinesFG1, TOOL_TEXTS_LINES_FG + "1", (activeTool == Tool.DRAW_LINES_FG) && (activeToolSize == 1));
		GUIMenuTools.adjustToolTitle(drawLinesFG2, TOOL_TEXTS_LINES_FG + "2", (activeTool == Tool.DRAW_LINES_FG) && (activeToolSize == 2));
		GUIMenuTools.adjustToolTitle(drawLinesFG4, TOOL_TEXTS_LINES_FG + "4", (activeTool == Tool.DRAW_LINES_FG) && (activeToolSize == 4));
		GUIMenuTools.adjustToolTitle(drawLinesFG8, TOOL_TEXTS_LINES_FG + "8", (activeTool == Tool.DRAW_LINES_FG) && (activeToolSize == 8));
		GUIMenuTools.adjustToolTitle(fill, TOOL_TEXTS_FILL_FG, activeTool == Tool.FILL_FG);
		GUIMenuTools.adjustToolTitle(fillRoughly, TOOL_TEXTS_FILL_ROUGHLY_FG, activeTool == Tool.FILL_ROUGHLY_FG);
		GUIMenuTools.adjustToolTitle(drawRectangleFG, TOOL_TEXTS_RECTANGLE_FG, activeTool == Tool.DRAW_RECTANGLE_FG);
		GUIMenuTools.adjustToolTitle(drawRectangleBG, TOOL_TEXTS_RECTANGLE_BG, activeTool == Tool.DRAW_RECTANGLE_BG);
		GUIMenuTools.adjustToolTitle(drawAreaFG, TOOL_TEXTS_AREA_FG, activeTool == Tool.DRAW_AREA_FG);
		GUIMenuTools.adjustToolTitle(drawAreaBG, TOOL_TEXTS_AREA_BG, activeTool == Tool.DRAW_AREA_BG);
	}
}
