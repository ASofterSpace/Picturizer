/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui;

import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.ImageLayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUIButtonHolder {

	private Color DEFAULT_COLOR = (new ColorRGBA(200, 100, 255, 255)).toColor();
	private Color FOCUS_COLOR = (new ColorRGBA(170, 50, 200, 255)).toColor();

	private JButton pipFG;
	private JButton pipBG;
	private JButton penBtn;
	private JButton lineBtn;
	private JButton fillBtn;
	private JButton roughFillBtn;
	private JButton rectBtn;
	private JButton quadsBtn;
	private JButton ellipseBtn;
	private JButton areaBtn;

	private List<JButton> zoomButtons = new ArrayList<>();


	void createButtons(GUI gui, JPanel parent) {

		GridBagLayout parentLayout = new GridBagLayout();
		parent.setLayout(parentLayout);
		parent.setBorder(BorderFactory.createEmptyBorder());

		int row = 0;

		JButton unsetBtn = new JButton("X");
		unsetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(null);
			}
		});
		addBtn(unsetBtn, 0, row, parent);

		row++;

		pipFG = new JButton("FgCo");
		pipFG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.PIPETTE_FG);
			}
		});
		addBtn(pipFG, 0, row, parent);

		pipBG = new JButton("BgCo");
		pipBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.PIPETTE_BG);
			}
		});
		addBtn(pipBG, 1, row, parent);

		row++;

		penBtn = new JButton("Pen");
		penBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_PEN_FG);
			}
		});
		addBtn(penBtn, 0, row, parent);

		lineBtn = new JButton("Line");
		lineBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_LINES_FG);
			}
		});
		addBtn(lineBtn, 1, row, parent);

		row++;

		fillBtn = new JButton("Fill");
		fillBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.FILL_FG);
			}
		});
		addBtn(fillBtn, 0, row, parent);

		roughFillBtn = new JButton("~Fill");
		roughFillBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.FILL_ROUGHLY_FG);
			}
		});
		addBtn(roughFillBtn, 1, row, parent);

		row++;

		rectBtn = new JButton("Rect");
		rectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_RECTANGLE_FG);
			}
		});
		addBtn(rectBtn, 0, row, parent);

		quadsBtn = new JButton("Quads");
		quadsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_QUADS_FG);
			}
		});
		addBtn(quadsBtn, 1, row, parent);

		row++;

		ellipseBtn = new JButton("Elps");
		ellipseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_ELLIPSE_FG);
			}
		});
		addBtn(ellipseBtn, 0, row, parent);

		areaBtn = new JButton("Area");
		areaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.forceActiveTool(Tool.DRAW_AREA_FG);
			}
		});
		addBtn(areaBtn, 1, row, parent);

		row++;

		parent.add(new JLabel(" "), new Arrangement(0, row, 1.0, 0.0));

		row++;

		JButton curBtn;

		curBtn = createLayerMoveBtn(gui, "<<", -64, 0);
		addBtn(curBtn, 0, row, parent);

		curBtn = createLayerMoveBtn(gui, ">>", 64, 0);
		addBtn(curBtn, 1, row, parent);

		row++;

		curBtn = createLayerMoveBtn(gui, "//\\\\", 0, -64);
		addBtn(curBtn, 0, row, parent);

		curBtn = createLayerMoveBtn(gui, "\\\\//", 0, 64);
		addBtn(curBtn, 1, row, parent);

		row++;

		curBtn = createLayerMoveBtn(gui, "<", -8, 0);
		addBtn(curBtn, 0, row, parent);

		curBtn = createLayerMoveBtn(gui, ">", 8, 0);
		addBtn(curBtn, 1, row, parent);

		row++;

		curBtn = createLayerMoveBtn(gui, "/\\", 0, -8);
		addBtn(curBtn, 0, row, parent);

		curBtn = createLayerMoveBtn(gui, "\\/", 0, 8);
		addBtn(curBtn, 1, row, parent);

		row++;

		parent.add(new JLabel(" "), new Arrangement(0, row, 1.0, 0.0));

		row++;

		curBtn = createZoomBtn(gui, "1x", 1.0);
		addBtn(curBtn, 0, row, parent);
		curBtn.setBackground(FOCUS_COLOR);

		curBtn = createZoomBtn(gui, "2x", 2.0);
		addBtn(curBtn, 1, row, parent);

		row++;

		curBtn = createZoomBtn(gui, "4x", 4.0);
		addBtn(curBtn, 0, row, parent);

		curBtn = createZoomBtn(gui, "8x", 8.0);
		addBtn(curBtn, 1, row, parent);

		row++;

		curBtn = createZoomBtn(gui, "/2", 0.5);
		addBtn(curBtn, 0, row, parent);

		curBtn = createZoomBtn(gui, "/4", 0.25);
		addBtn(curBtn, 1, row, parent);

		row++;

		parent.setPreferredSize(new Dimension(1, 30*row));
	}

	void refreshTools(Tool activeTool) {
		pipFG.setBackground((activeTool == Tool.PIPETTE_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		pipBG.setBackground((activeTool == Tool.PIPETTE_BG) ? FOCUS_COLOR : DEFAULT_COLOR);
		penBtn.setBackground((activeTool == Tool.DRAW_PEN_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		lineBtn.setBackground((activeTool == Tool.DRAW_LINES_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		fillBtn.setBackground((activeTool == Tool.FILL_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		roughFillBtn.setBackground((activeTool == Tool.FILL_ROUGHLY_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		rectBtn.setBackground((activeTool == Tool.DRAW_RECTANGLE_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		quadsBtn.setBackground((activeTool == Tool.DRAW_QUADS_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		ellipseBtn.setBackground((activeTool == Tool.DRAW_ELLIPSE_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
		areaBtn.setBackground((activeTool == Tool.DRAW_AREA_FG) ? FOCUS_COLOR : DEFAULT_COLOR);
	}

	private void addBtn(JButton btn, int x, int y, JPanel parent) {
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setBackground(DEFAULT_COLOR);
		parent.add(btn, new Arrangement(x, y, 1.0, 0.0));
	}

	private JButton createLayerMoveBtn(GUI gui, String caption, int moveX, int moveY) {
		JButton curBtn = new JButton(caption);
		curBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer imgLayer = gui.getCurrentLayer();
				imgLayer.setOffsetX(imgLayer.getOffsetX() + moveX);
				imgLayer.setOffsetY(imgLayer.getOffsetY() + moveY);
				gui.refreshMainView();
				gui.refreshLayerView();
			}
		});
		return curBtn;
	}

	private JButton createZoomBtn(GUI gui, String caption, double zoomFactor) {
		JButton curBtn = new JButton(caption);
		curBtn.setBackground(DEFAULT_COLOR);
		curBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setZoomFactor(zoomFactor);
				for (JButton zoomBtn : zoomButtons) {
					zoomBtn.setBackground(DEFAULT_COLOR);
				}
				curBtn.setBackground(FOCUS_COLOR);
			}
		});
		zoomButtons.add(curBtn);
		return curBtn;
	}
}
