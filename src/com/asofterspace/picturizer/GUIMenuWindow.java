/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.images.ColorRGBA;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuWindow {

	JMenu createMenu(GUI gui) {

		JMenu view = new JMenu("Window");

		JMenuItem bgForeground = new JMenuItem("Set Window BG to Foreground Color");
		bgForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(gui.getForegroundColor().toColor());
			}
		});
		view.add(bgForeground);

		JMenuItem bgBackground = new JMenuItem("Set Window BG to Background Color");
		bgBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(gui.getBackgroundColor().toColor());
			}
		});
		view.add(bgBackground);

		JMenuItem bgBlack = new JMenuItem("Set Window BG to Black");
		bgBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.black);
			}
		});
		view.add(bgBlack);

		JMenuItem bgGray = new JMenuItem("Set Window BG to Gray");
		bgGray.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.gray);
			}
		});
		view.add(bgGray);

		JMenuItem bgWhite = new JMenuItem("Set Window BG to White");
		bgWhite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.white);
			}
		});
		view.add(bgWhite);

		JMenuItem bgRed = new JMenuItem("Set Window BG to Red");
		bgRed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.red);
			}
		});
		view.add(bgRed);

		JMenuItem bgGreen = new JMenuItem("Set Window BG to Green");
		bgGreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.green);
			}
		});
		view.add(bgGreen);

		JMenuItem bgBlue = new JMenuItem("Set Window BG to Blue");
		bgBlue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor(Color.blue);
			}
		});
		view.add(bgBlue);

		JMenuItem bgPurple = new JMenuItem("Set Window BG to Purple");
		bgPurple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setWindowBackgroundColor((new ColorRGBA(80, 0, 110)).toColor());
			}
		});
		view.add(bgPurple);

		return view;
	}
}
