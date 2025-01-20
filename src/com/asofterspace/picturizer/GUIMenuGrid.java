/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuGrid {

	private CreateGridGUI createGridGUI = null;


	JMenu createMenu(GUI gui) {

		JMenu grid = new JMenu("Grid");

		JMenuItem newGrid2 = new JMenuItem("Create Empty Grid to Align Images");
		newGrid2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = true;
				showGridGUI(gui, createNew);
			}
		});
		grid.add(newGrid2);

		JMenuItem copyToGrid = new JMenuItem("Copy Current Image Multiple Times into Grid");
		copyToGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = false;
				showGridGUI(gui, createNew);
			}
		});
		grid.add(copyToGrid);

		return grid;
	}

	void showGridGUI(GUI gui, boolean createNew) {

		if (createGridGUI == null) {
			createGridGUI = new CreateGridGUI(gui);
		}

		createGridGUI.show(createNew, gui.getForegroundColor(), gui.getBackgroundColor(), gui.getPicture().bake());
	}

}
