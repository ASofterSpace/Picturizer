/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuTools {

	private final static String TOOL_SELECTED_START_STR = ">> ";
	private final static String TOOL_SELECTED_END_STR = " <<";


	public JMenu createMenu(GUI gui) {

		JMenu tools = new JMenu("Tools");

		JMenuItem unsetTool = new JMenuItem("Unset Currently Used Tool");
		unsetTool.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setActiveTool(null);
			}
		});
		tools.add(unsetTool);

		return tools;
	}

	static void adjustToolTitle(JMenuItem item, String itemText, boolean isActive) {
		if (isActive) {
			item.setText(TOOL_SELECTED_START_STR + itemText + TOOL_SELECTED_END_STR);
		} else {
			item.setText(itemText);
		}
	}
}
