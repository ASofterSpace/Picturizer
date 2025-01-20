/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.io.Directory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class GUIMenuFile {

	final static Directory TEMP_DIR = new Directory("temp");

	private JMenuItem saveAgain;
	private JMenuItem exportAgain;
	private JMenuItem close;


	JMenu createMenu(GUI gui) {

		JMenu file = new JMenu("File");

		JMenuItem openFile = new JMenuItem("Open / Import...");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean returnImage = false;
				gui.openFile(returnImage);
			}
		});
		file.add(openFile);

		JMenuItem saveFileAs = new JMenuItem("Save As...");
		saveFileAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean exporting = false;
				gui.saveOrExportFile(exporting);
			}
		});
		file.add(saveFileAs);

		saveAgain = new JMenuItem("");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveFileAgain();
			}
		});
		saveAgain.setEnabled(false);
		file.add(saveAgain);

		JMenuItem exportFileTo = new JMenuItem("Export To...");
		exportFileTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean exporting = true;
				gui.saveOrExportFile(exporting);
			}
		});
		file.add(exportFileTo);

		exportAgain = new JMenuItem("");
		exportAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.exportFileAgain();
			}
		});
		exportAgain.setEnabled(false);
		file.add(exportAgain);

		file.addSeparator();

		JMenuItem openTempDir = new JMenuItem("Open Temp Directory");
		openTempDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TEMP_DIR.create();
				GuiUtils.openFolder(TEMP_DIR);
			}
		});
		file.add(openTempDir);

		file.addSeparator();

		close = new JMenuItem("Exit");
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(close);

		return file;
	}

	public JMenuItem getSaveAgain() {
		return saveAgain;
	}

	public JMenuItem getExportAgain() {
		return exportAgain;
	}

}
