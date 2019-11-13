/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.gui.MainWindow;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.ImageFile;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.Utils;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


public class GUI extends MainWindow {

	private final static String CONFIG_KEY_WIDTH = "mainFrameWidth";
	private final static String CONFIG_KEY_HEIGHT = "mainFrameHeight";
	private final static String CONFIG_KEY_LEFT = "mainFrameLeft";
	private final static String CONFIG_KEY_TOP = "mainFrameTop";
	private final static String CONFIG_KEY_LAST_DIRECTORY = "lastDirectory";

	private String lastPicturePath;

	private JPanel mainPanelRight;

	private JMenuItem close;

	private ConfigFile configuration;

	private ImageIcon imageViewer;
	private JLabel imageViewerLabel;
	private Image picture;
	private Image undoablePicture;
	private Image redoablePicture;

	private QrGUI qrGUI;
	private ChannelChangeGUI channelChangeGUI;


	public GUI(ConfigFile configFile) {
		this.configuration = configFile;
	}

	@Override
	public void run() {

		super.create();

		createMenu(mainFrame);

		createMainPanel(mainFrame);

		// do not call super.show, as we are doing things a little bit
		// differently around here (including restoring from previous
		// position...)
		// super.show();

		final Integer lastWidth = configuration.getInteger(CONFIG_KEY_WIDTH, -1);
		final Integer lastHeight = configuration.getInteger(CONFIG_KEY_HEIGHT, -1);
		final Integer lastLeft = configuration.getInteger(CONFIG_KEY_LEFT, -1);
		final Integer lastTop = configuration.getInteger(CONFIG_KEY_TOP, -1);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Stage everything to be shown
				mainFrame.pack();

				// Actually display the whole jazz
				mainFrame.setVisible(true);

				if ((lastWidth < 1) || (lastHeight < 1)) {
					GuiUtils.maximizeWindow(mainFrame);
				} else {
					mainFrame.setSize(lastWidth, lastHeight);

					mainFrame.setPreferredSize(new Dimension(lastWidth, lastHeight));

					mainFrame.setLocation(new Point(lastLeft, lastTop));
				}

				mainFrame.addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent componentEvent) {
						configuration.set(CONFIG_KEY_WIDTH, mainFrame.getWidth());
						configuration.set(CONFIG_KEY_HEIGHT, mainFrame.getHeight());
					}

					public void componentMoved(ComponentEvent componentEvent) {
						configuration.set(CONFIG_KEY_LEFT, mainFrame.getLocation().x);
						configuration.set(CONFIG_KEY_TOP, mainFrame.getLocation().y);
					}
				});
			}
		});

		createNewEmptyFile();
	}

	private JMenuBar createMenu(JFrame parent) {

		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		menu.add(file);

		JMenu newFile = new JMenu("New");
		file.add(newFile);

		JMenuItem emptyFile = new JMenuItem("Empty");
		emptyFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewEmptyFile();
			}
		});
		newFile.add(emptyFile);

		JMenuItem qrCodeFile = new JMenuItem("QR Code");
		qrCodeFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (qrGUI == null) {
					qrGUI = new QrGUI(GUI.this);
				}
				qrGUI.show();
			}
		});
		newFile.add(qrCodeFile);

		JMenuItem openFile = new JMenuItem("Open");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		file.add(openFile);

		JMenuItem saveFileAs = new JMenuItem("Save As...");
		saveFileAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});
		file.add(saveFileAs);

		// file.addSeparator();

		close = new JMenuItem("Exit");
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(close);

		JMenu edit = new JMenu("Edit");
		menu.add(edit);

		JMenuItem undo = new JMenuItem("Undo");
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		edit.add(undo);

		JMenuItem redo = new JMenuItem("Redo");
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		edit.add(redo);

		edit.addSeparator();

		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.clear();
				refreshView();
			}
		});
		edit.add(clear);

		JMenuItem editChannelsManually = new JMenuItem("Edit Channels Manually");
		editChannelsManually.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (channelChangeGUI == null) {
					channelChangeGUI = new ChannelChangeGUI(GUI.this);
				}
				channelChangeGUI.show();
			}
		});
		edit.add(editChannelsManually);

		JMenuItem dampenWeakly = new JMenuItem("Dampen Weakly");
		dampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(1.5f);
				refreshView();
			}
		});
		edit.add(dampenWeakly);

		JMenuItem dampenStrongly = new JMenuItem("Dampen Strongly");
		dampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(2);
				refreshView();
			}
		});
		edit.add(dampenStrongly);

		/*
		TODO:
		stark dämpfen:
		p^[1] := max(255 - ((255 - p^[1]) * 2), 0);
		p^[2] := max(255 - ((255 - p^[2]) * 2), 0);
		p^[3] := max(255 - ((255 - p^[3]) * 2), 0)

		leicht dämpfen:
		p^[1] := max(255 - round((255 - p^[1]) * 1.5), 0);
		p^[2] := max(255 - round((255 - p^[2]) * 1.5), 0);
		p^[3] := max(255 - round((255 - p^[3]) * 1.5), 0);


		kopieren

		einfügen

		Differenzanalyse:
		varc := ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx, vary]);
		varcc := ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx + 1, vary]);
		varccc := ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx, vary + 1]);
		varcccc := ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx + 1, vary + 1]);
		varbr := max255(Abs(GetRValue(varc) - GetRValue(varcc)) + Abs(GetRValue(varccc) - GetRValue(varcccc)));
		varbg := max255(Abs(GetGValue(varc) - GetGValue(varcc)) + Abs(GetGValue(varccc) - GetGValue(varcccc)));
		varbb := max255(Abs(GetBValue(varc) - GetBValue(varcc)) + Abs(GetBValue(varccc) - GetBValue(varcccc)));
		PicOrgImage.Canvas.Pixels[varx, vary] := RGB(varbr, varbg, varbb);

		second layer:
		PicOrgImage.Canvas.Pixels[varx, vary] := RGB(HextoInt(InttoHex(GetRValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[2] + InttoHex(GetRValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[1]), HextoInt(InttoHex(GetGValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[2] + InttoHex(GetGValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[1]), HextoInt(InttoHex(GetBValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[2] + InttoHex(GetBValue(PicOrgImage.Canvas.Pixels[varx, vary]), 2)[1]));

		Differenzanalyse grau:
		varbr := max255(Abs(GetRValue(ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx, vary])) - GetRValue(ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx + 1, vary]))) + Abs(GetRValue(ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx, vary + 1])) - GetRValue(ColorToRGB(PicOrgSicherung.Canvas.Pixels[varx + 1, vary + 1]))));
		PicOrgImage.Canvas.Pixels[varx, vary] := RGB(varbr, varbr, varbr);

		Bild einfärben:
		p^[1] := Div0((p^[1] * 255), GetBValue(PicOrgFC.Color));
		p^[2] := Div0((p^[2] * 255), GetGValue(PicOrgFC.Color));
		p^[3] := Div0((p^[3] * 255), GetRValue(PicOrgFC.Color));
		Farben entfernen:
		p^[1] := Trunc((p^[1] * 0.11) + (p^[2] * 0.59) + (p^[3] * 0.3));
		p^[2] := p^[1];
		p^[3] := p^[2];
		Inc(p);

		Bild stark verdunkeln:
		p^[1] := round(p^[1] * 0.25);
		p^[2] := round(p^[2] * 0.25);
		p^[3] := round(p^[3] * 0.25);

		Bild leicht verdunkeln:
		p^[1] := round(p^[1] * 0.75);
		p^[2] := round(p^[2] * 0.75);
		p^[3] := round(p^[3] * 0.75);

		Bild leicht aufhellen:
		p^[1] := max255(round(p^[1] * 1.25));
		p^[2] := max255(round(p^[2] * 1.25));
		p^[3] := max255(round(p^[3] * 1.25));

		Bild stark aufhellen:
		p^[1] := max255(round(p^[1] * 1.75));
		p^[2] := max255(round(p^[2] * 1.75));
		p^[3] := max255(round(p^[3] * 1.75));

		Farben intensivieren:
		p^[1] := max255((p^[1] * p^[1]) div 128);
		p^[2] := max255((p^[2] * p^[2]) div 128);
		p^[3] := max255((p^[3] * p^[3]) div 128);

		Farben leicht intensivieren:
		p^[1] := (max255((p^[1] * p^[1]) div 128) * p^[1]) div 2;
		p^[2] := (max255((p^[2] * p^[2]) div 128) * p^[2]) div 2;
		p^[3] := (max255((p^[3] * p^[3]) div 128) * p^[3]) div 2;
		Farben reduziern:
		varbr := Trunc((p^[1] * 0.11) + (p^[2] * 0.59) + (p^[3] * 0.3));
		p^[1] := max255((p^[1] * varbr) div 128);
		p^[2] := max255((p^[2] * varbr) div 128);
		p^[3] := max255((p^[3] * varbr) div 128);

		Farben dereduzieren:
		varbr := Trunc((p^[1] * 0.11) + (p^[2] * 0.59) + (p^[3] * 0.3));
		if varbr = 0 then
		begin
		p^[1] := 255;
		p^[2] := 255;
		p^[3] := 255;
		end
		else
		begin
		p^[1] := max255((p^[1] * 255) div varbr);
		p^[2] := max255((p^[2] * 255) div varbr);
		p^[3] := max255((p^[3] * 255) div varbr);
		end;
		Farben verwischen:
		varc := ColorToRGB(Pixels[varx, vary]);
		varwr := round(sqrt(GetRValue(varc) * 128));
		varwg := round(sqrt(GetGValue(varc) * 128));
		varwb := round(sqrt(GetBValue(varc) * 128));
		if varwr > 255 then
		varwr := 255;
		if varwg > 255 then
		varwg := 255;
		if varwb > 255 then
		varwb := 255;
		Pixels[varx, vary] := RGB(varwr, varwg, varwb);

		Farbrichtig invertieren:
		varc := ColorToRGB(Pixels[varx, vary]);
		varbb := GetBValue(varc);
		varbr := GetRValue(varc);
		varbg := GetGValue(varc);
		varbb := Trunc((varbb * 0.11) + (varbg * 0.59) + (varbr * 0.3));
		varbr := (GetRValue(varc) * (256 - varbb)) div 255;
		varbg := (GetGValue(varc) * (256 - varbb)) div 255;
		varbb := (GetBValue(varc) * (256 - varbb)) div 255;
		Pixels[varx, vary] := RGB(varbr, varbg, varbb);
		Farben invertieren:
		drawShape(pmNot)

		Farbe einmischen:
		varc := ColorToRGB(Pixels[varx, vary]);
		varbr := (GetRValue(varc) + GetRValue(varcc)) div 2;
		varbg := (GetGValue(varc) + GetGValue(varcc)) div 2;
		varbb := (GetBValue(varc) + GetBValue(varcc)) div 2;
		Pixels[varx, vary] := RGB(varbr, varbg, varbb);

		Farbe extrahieren:
		varc := ColorToRGB(Pixels[varx, vary]);
		{ varwr := (posor0((GetRValue(varc)) - (GetRValue(varcc)) div 2)) * 2;
		varwg := (posor0((GetGValue(varc)) - (GetGValue(varcc)) div 2)) * 2;
		varwb := (posor0((GetBValue(varc)) - (GetBValue(varcc)) div 2)) * 2;}
		varwr := (GetRValue(varc) * 2) - GetRValue(varcc);
		varwg := (GetGValue(varc) * 2) - GetGValue(varcc);
		varwb := (GetBValue(varc) * 2) - GetBValue(varcc);
		if varwr > 255 then
		varwr := 255;
		if varwg > 255 then
		varwg := 255;
		if varwb > 255 then
		varwb := 255;
		Pixels[varx, vary] := RGB(varwr, varwg, varwb);
		*/

		JMenu huh = new JMenu("?");

		JMenuItem openConfigPath = new JMenuItem("Open Config Path");
		openConfigPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(configuration.getParentDirectory().getJavaFile());
				} catch (IOException ex) {
					// do nothing
				}
			}
		});
		huh.add(openConfigPath);

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String aboutMessage = "This is the " + Main.PROGRAM_TITLE + ".\n" +
					"Version: " + Main.VERSION_NUMBER + " (" + Main.VERSION_DATE + ")\n" +
					"Brought to you by: A Softer Space";
				JOptionPane.showMessageDialog(mainFrame, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(about);
		menu.add(huh);

		parent.setJMenuBar(menu);

		return menu;
	}

	private JPanel createMainPanel(JFrame parent) {

		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(800, 500));
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanel.setLayout(mainPanelLayout);

		JPanel mainPanelRightOuter = new JPanel();
		GridBagLayout mainPanelRightOuterLayout = new GridBagLayout();
		mainPanelRightOuter.setLayout(mainPanelRightOuterLayout);

		mainPanelRight = new JPanel();
		mainPanelRight.setLayout(new CardLayout());
		mainPanelRight.setPreferredSize(new Dimension(8, 8));

		imageViewer = new ImageIcon();
		imageViewerLabel = new JLabel(imageViewer);
		mainPanelRight.add(imageViewerLabel);

		mainPanelRightOuter.add(mainPanelRight, new Arrangement(0, 0, 1.0, 1.0));

		mainPanel.add(mainPanelRightOuter, new Arrangement(3, 0, 1.0, 1.0));

		parent.add(mainPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private void refreshTitleBar() {
		mainFrame.setTitle(Main.PROGRAM_TITLE + " - " + lastPicturePath);
	}

	private void createNewEmptyFile() {

		setPicture(new Image(100, 100));
	}

	public Image getPicture() {
		return picture.copy();
	}

	public void setPicture(Image newPicture) {

		saveCurPicForUndo();

		picture = newPicture;

		refreshView();

		lastPicturePath = "Unsaved Picture";
		refreshTitleBar();
	}

	private void openFile() {

		// TODO :: de-localize the JFileChooser (by default it seems localized, which is inconsistent when the rest of the program is in English...)
		// (while you're at it, make Öffnen into Save for the save dialog, but keep it as Open for the open dialog... ^^)
		// TODO :: actually, write our own file chooser
		JFileChooser augFilePicker;

		// if we find nothing better, use the last-used directory
		String lastDirectory = configuration.getValue(CONFIG_KEY_LAST_DIRECTORY);

		if ((lastDirectory != null) && !"".equals(lastDirectory)) {
			augFilePicker = new JFileChooser(new java.io.File(lastDirectory));
		} else {
			augFilePicker = new JFileChooser();
		}

		augFilePicker.setDialogTitle("Open a Picture File to Edit");
		augFilePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
		augFilePicker.setMultiSelectionEnabled(false);

		addOpenFileFilters(augFilePicker);

		int result = augFilePicker.showOpenDialog(mainFrame);

		switch (result) {

			case JFileChooser.APPROVE_OPTION:

				// load the files
				configuration.set(CONFIG_KEY_LAST_DIRECTORY, augFilePicker.getCurrentDirectory().getAbsolutePath());
				configuration.create();

				File selectedFile = new File(augFilePicker.getSelectedFile());
				saveCurPicForUndo();
				picture = ImageFile.readImageFromFile(selectedFile);
				refreshView();

				lastPicturePath = selectedFile.getCanonicalFilename();
				refreshTitleBar();

				break;

			case JFileChooser.CANCEL_OPTION:
				// cancel was pressed... do nothing for now
				break;
		}
	}

	private void saveFileAs() {

		JFileChooser augFilePicker;

		// if we find nothing better, use the last-used directory
		String lastDirectory = configuration.getValue(CONFIG_KEY_LAST_DIRECTORY);

		if ((lastDirectory != null) && !"".equals(lastDirectory)) {
			augFilePicker = new JFileChooser(new java.io.File(lastDirectory));
		} else {
			augFilePicker = new JFileChooser();
		}

		augFilePicker.setDialogTitle("Save as Picture File");
		augFilePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
		augFilePicker.setMultiSelectionEnabled(false);

		addSaveFileFilters(augFilePicker);

		int result = augFilePicker.showSaveDialog(mainFrame);

		switch (result) {

			case JFileChooser.APPROVE_OPTION:

				// save the files
				configuration.set(CONFIG_KEY_LAST_DIRECTORY, augFilePicker.getCurrentDirectory().getAbsolutePath());
				configuration.create();

				File selectedFile = new File(augFilePicker.getSelectedFile());
				String selectedExtension = null;
				FileFilter genericFilter = augFilePicker.getFileFilter();
				if (genericFilter != null) {
					if (genericFilter instanceof FileNameExtensionFilter) {
						FileNameExtensionFilter filter = (FileNameExtensionFilter) genericFilter;
						if (filter.getExtensions() != null) {
							if (filter.getExtensions().length > 0) {
								selectedExtension = filter.getExtensions()[0];
								if (selectedExtension != null) {
									selectedExtension = selectedExtension.toLowerCase();
									if (!selectedFile.getFilename().toLowerCase().endsWith("." + selectedExtension)) {
										selectedFile = new File(selectedFile.getFilename() + "." + selectedExtension);
									}
								}
							}
						}
					}
				}
				ImageFile.saveImageToFile(picture, selectedFile);

				lastPicturePath = selectedFile.getCanonicalFilename();
				refreshTitleBar();

				break;

			case JFileChooser.CANCEL_OPTION:
				// cancel was pressed... do nothing for now
				break;
		}
	}

	private void addOpenFileFilters(JFileChooser fileChooser) {
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG files (*.jpg, *.jpeg)", "jpg", "jpeg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap files (*.bmp)", "bmp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", "png"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Bitmap (*.pbm)", "pbm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Gray Map (*.pgm)", "pgm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Pixel Map (*.ppm)", "ppm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Document Format (*.pdf)", "pdf"));
	}

	private void addSaveFileFilters(JFileChooser fileChooser) {
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG files (*.jpg, *.jpeg)", "jpg", "jpeg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap files (*.bmp)", "bmp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", "png"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Bitmap (*.pbm)", "pbm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Gray Map (*.pgm)", "pgm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Pixel Map (*.ppm)", "ppm"));
	}

	private void refreshView() {
		imageViewer.setImage(picture.getAwtImage());
		imageViewerLabel.repaint();
	}

	private void saveCurPicForUndo() {
		undoablePicture = picture;
		redoablePicture = null;
	}

	private void undo() {
		if (undoablePicture == null) {
			return;
		}
		redoablePicture = picture;
		picture = undoablePicture;
		undoablePicture = null;
		refreshView();
	}

	private void redo() {
		if (redoablePicture == null) {
			return;
		}
		undoablePicture = picture;
		picture = redoablePicture;
		redoablePicture = null;
		refreshView();
	}

}
