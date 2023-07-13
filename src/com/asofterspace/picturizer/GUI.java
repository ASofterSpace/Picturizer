/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.gui.MainWindow;
import com.asofterspace.toolbox.gui.MenuItemForMainMenu;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageFile;
import com.asofterspace.toolbox.images.ImageFileCtrl;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.pdf.PdfImageHandler;
import com.asofterspace.toolbox.Utils;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
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
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


public class GUI extends MainWindow {

	private final static String CONFIG_KEY_WIDTH = "mainFrameWidth";
	private final static String CONFIG_KEY_HEIGHT = "mainFrameHeight";
	private final static String CONFIG_KEY_LEFT = "mainFrameLeft";
	private final static String CONFIG_KEY_TOP = "mainFrameTop";
	private final static String CONFIG_KEY_LAST_DIRECTORY = "lastDirectory";

	private String lastPicturePath;

	private JPanel mainPanel;
	private JScrollPane mainPanelLeft;
	private JScrollPane mainPanelRight;

	private JMenuItem close;

	private ConfigFile configuration;

	private Image mainPanelLeftImg;
	private ImageIcon mainPanelLeftViewer;
	private JLabel mainPanelLeftViewerLabel;

	private ImageIcon imageViewer;
	private JLabel imageViewerLabel;
	private Image picture;
	private Image undoablePicture3;
	private Image undoablePicture2;
	private Image undoablePicture1;
	private Image undoablePicture;
	private Image redoablePicture;

	private QrGUI qrGUI = null;
	private ChannelChangeGUI channelChangeGUI = null;
	private CreateGridGUI createGridGUI = null;
	private ColorPickerGUI colorPickerGUI = null;
	private boolean colorToBePickedIsForeground = true;

	private ImageFileCtrl imageFileCtrl;
	private Image colorpickerImg;

	private ColorRGBA foregroundColor = ColorRGBA.BLACK;
	private ColorRGBA backgroundColor = ColorRGBA.WHITE;
	private ColorRGBA windowBackgroundColor = new ColorRGBA(Color.gray);

	private final static Directory TEMP_DIR = new Directory("temp");


	public GUI(ConfigFile configFile) {
		this.configuration = configFile;

		// we want to handle as many image formats as we can...
		// even opening images from PDF files, if possible :)
		this.imageFileCtrl = new ImageFileCtrl();
		this.imageFileCtrl.addHandler(new PdfImageHandler());

		colorpickerImg = imageFileCtrl.loadImageFromFile(new File(
			 System.getProperty("java.class.path") + "/../res/colorpicker.png"));
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

				refreshLeftView();

				refreshMainView();
			}
		});

		createNewEmptyFile();
	}

	private JMenuBar createMenu(JFrame parent) {

		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		menu.add(file);

		JMenu newFile = new JMenu("New");
		menu.add(newFile);

		JMenuItem emptyFile = new JMenuItem("Empty");
		emptyFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewEmptyFile();
			}
		});
		newFile.add(emptyFile);

		JMenuItem screenshotFile = new JMenuItem("Screenshot");
		screenshotFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createScreenshot();
			}
		});
		newFile.add(screenshotFile);

		Map<String, Object> screenshotBackgrounds =
			configuration.getAllContents().getObjectMap("screenshotBackgrounds");

		for (Map.Entry<String, Object> entry : screenshotBackgrounds.entrySet()) {
			String colorName = entry.getKey();
			Object value = entry.getValue();
			if (value != null) {
				if (value instanceof String) {
					JMenuItem picFromScreenshot = new JMenuItem("Picture from Screenshot (" + colorName + ")");
					picFromScreenshot.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							createPicFromScreenshot(ColorRGBA.fromString((String) value));
						}
					});
					newFile.add(picFromScreenshot);
				}
			}
		}

		JMenuItem picFromPDFexplanation = new JMenuItem("Take screenshot, click PDF 1, take second screenshot with tiny overlap, click PDF 2:");
		newFile.add(picFromPDFexplanation);

		JMenuItem picFromPDF1 = new JMenuItem("Picture from PDF 1");
		picFromPDF1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createPicFromPDF1();
			}
		});
		newFile.add(picFromPDF1);

		JMenuItem picFromPDF2 = new JMenuItem("Picture from PDF 2");
		picFromPDF2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createPicFromPDF2();
			}
		});
		newFile.add(picFromPDF2);

		JMenuItem qrCodeCore = new JMenuItem("QR Code Core");
		qrCodeCore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (qrGUI == null) {
					qrGUI = new QrGUI(GUI.this);
				}
				qrGUI.setUseFrame(false);
				qrGUI.setBackgroundColor(backgroundColor);
				qrGUI.setForegroundColor(foregroundColor);
				qrGUI.show();
			}
		});
		newFile.add(qrCodeCore);

		JMenuItem qrCodeFramed = new JMenuItem("QR Code Framed");
		qrCodeFramed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (qrGUI == null) {
					qrGUI = new QrGUI(GUI.this);
				}
				qrGUI.setUseFrame(true);
				qrGUI.setBackgroundColor(backgroundColor);
				qrGUI.setForegroundColor(foregroundColor);
				qrGUI.show();
			}
		});
		newFile.add(qrCodeFramed);

		JMenuItem newGrid = new JMenuItem("Empty Grid to Align Images");
		newGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (createGridGUI == null) {
					createGridGUI = new CreateGridGUI(GUI.this);
				}
				createGridGUI.show(foregroundColor, backgroundColor);
			}
		});
		newFile.add(newGrid);


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


		JMenu edit = new JMenu("Edit");
		menu.add(edit);

		JMenuItem undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		edit.add(undo);

		JMenuItem redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
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
				refreshMainView();
			}
		});
		edit.add(clear);

		edit.addSeparator();

		JMenuItem copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.copyToClipboard();
			}
		});
		edit.add(copy);

		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPicture(Image.createFromClipboard());
			}
		});
		edit.add(paste);

		MenuItemForMainMenu editChannelsManually = new MenuItemForMainMenu("Edit Channels");
		editChannelsManually.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (channelChangeGUI == null) {
					channelChangeGUI = new ChannelChangeGUI(GUI.this);
				}
				channelChangeGUI.show();
			}
		});
		menu.add(editChannelsManually);

		JMenu colors = new JMenu("Colors");
		menu.add(colors);

		JMenuItem switchForeAndBack = new JMenuItem("Switch Foreground and Background Colors");
		switchForeAndBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchForeAndBackColor();
			}
		});
		colors.add(switchForeAndBack);

		JMenuItem setForegroundToColorPickerGUI = new JMenuItem("Set Foreground to a Particular Color");
		setForegroundToColorPickerGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorToBePickedIsForeground = true;
				if (colorPickerGUI == null) {
					colorPickerGUI = new ColorPickerGUI(GUI.this);
				}
				colorPickerGUI.show(foregroundColor);
			}
		});
		colors.add(setForegroundToColorPickerGUI);

		JMenuItem setBackgroundToColorPickerGUI = new JMenuItem("Set Background to a Particular Color");
		setBackgroundToColorPickerGUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorToBePickedIsForeground = false;
				if (colorPickerGUI == null) {
					colorPickerGUI = new ColorPickerGUI(GUI.this);
				}
				colorPickerGUI.show(backgroundColor);
			}
		});
		colors.add(setBackgroundToColorPickerGUI);

		JMenuItem setForegroundToMostCommon = new JMenuItem("Set Foreground to Most Common Color");
		setForegroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setForegroundColor(picture.getMostCommonColor());
			}
		});
		colors.add(setForegroundToMostCommon);

		JMenuItem setBackgroundToMostCommon = new JMenuItem("Set Background to Most Common Color");
		setBackgroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBackgroundColor(picture.getMostCommonColor());
			}
		});
		colors.add(setBackgroundToMostCommon);

		JMenuItem setForegroundToMostCommonSur = new JMenuItem("Set Foreground to Most Common Surrounding Color");
		setForegroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setForegroundColor(picture.getMostCommonSurroundingColor());
			}
		});
		colors.add(setForegroundToMostCommonSur);

		JMenuItem setBackgroundToMostCommonSur = new JMenuItem("Set Background to Most Common Surrounding Color");
		setBackgroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBackgroundColor(picture.getMostCommonSurroundingColor());
			}
		});
		colors.add(setBackgroundToMostCommonSur);

		JMenu adjustPixels = new JMenu("Pixel-Level");
		menu.add(adjustPixels);

		JMenuItem replaceBackgroundForeground = new JMenuItem("Replace Background Color with Foreground Color");
		replaceBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.replaceColors(backgroundColor, foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceBackgroundForeground);

		JMenuItem replaceAnythingButFgWithBg = new JMenuItem("Replace Anything but Foreground Color with Background Color");
		replaceAnythingButFgWithBg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.replaceColorsExcept(foregroundColor, backgroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceAnythingButFgWithBg);

		JMenuItem replaceAnythingButMcWithFg = new JMenuItem("Replace Anything but Most Common Color with Foreground Color");
		replaceAnythingButMcWithFg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.replaceColorsExcept(picture.getMostCommonColor(), foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceAnythingButMcWithFg);

		JMenuItem replaceMostCommonForeground = new JMenuItem("Replace Most Common Color with Foreground Color");
		replaceMostCommonForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				ColorRGBA mostCommonCol = picture.getMostCommonColor();
				picture.replaceColors(mostCommonCol, foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceMostCommonForeground);

		JMenuItem replaceMostCommonSurroundingForeground = new JMenuItem("Replace Most Common Surrounding Color with Foreground Color");
		replaceMostCommonSurroundingForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				ColorRGBA mostCommonSurroundCol = picture.getMostCommonSurroundingColor();
				picture.replaceColors(mostCommonSurroundCol, foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceMostCommonSurroundingForeground);

		JMenuItem replaceStragglersWithForeground = new JMenuItem("Replace Stragglers (Single Pixels) based on Background Color with Foreground Color");
		replaceStragglersWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.replaceStragglersWith(backgroundColor, foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceStragglersWithForeground);

		JMenuItem replaceStragglersIshWithForeground = new JMenuItem("Replace Stragglers-ish (Single-ish Pixels) based on Background Color with Foreground Color");
		replaceStragglersIshWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				ColorRGBA mostCommonSurroundCol = picture.getMostCommonSurroundingColor();
				picture.replaceStragglersIshWith(backgroundColor, foregroundColor);
				refreshMainView();
			}
		});
		adjustPixels.add(replaceStragglersIshWithForeground);

		addPixelLevelDiffMapButtons(adjustPixels);

		JMenu adjustColors = new JMenu("Adjust Colors");
		menu.add(adjustColors);

		JMenuItem removeAbsoluteColors = new JMenuItem("Remove Colors by Absolute Brightness");
		removeAbsoluteColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.removeColors();
				refreshMainView();
			}
		});
		adjustColors.add(removeAbsoluteColors);

		JMenuItem removePerceivedColors = new JMenuItem("Remove Colors by Perceived Brightness");
		removePerceivedColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.removePerceivedColors();
				refreshMainView();
			}
		});
		adjustColors.add(removePerceivedColors);

		adjustColors.addSeparator();

		JMenuItem headline1 = new JMenuItem("RGB to RGBA:");
		adjustColors.add(headline1);

		JMenuItem extractBlackToAlpha = new JMenuItem("Extract Black to Alpha");
		extractBlackToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.extractBlackToAlpha();
				refreshMainView();
			}
		});
		adjustColors.add(extractBlackToAlpha);

		JMenuItem extractBgColToAlpha = new JMenuItem("Extract Background Color to Alpha");
		extractBgColToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.extractBackgroundColorToAlpha();
				refreshMainView();
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
				saveCurPicForUndo();
				picture = picture.copy();
				picture.removeAlpha();
				refreshMainView();
			}
		});
		adjustColors.add(removeAlpha);

		JMenuItem bakeCurrentView = new JMenuItem("Remove Alpha Channel by Baking In Current Background View");
		bakeCurrentView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.bakeAlpha(new ColorRGBA(imageViewerLabel.getBackground()));
				refreshMainView();
			}
		});
		adjustColors.add(bakeCurrentView);


		JMenu invert = new JMenu("Invert");
		menu.add(invert);

		JMenuItem invertColors = new JMenuItem("Invert Colors");
		invertColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.invert();
				refreshMainView();
			}
		});
		invert.add(invertColors);

		JMenuItem invertBrightness = new JMenuItem("Invert Brightness (Keeping Colors, Approach 1)");
		invertBrightness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.invertBrightness1();
				refreshMainView();
			}
		});
		invert.add(invertBrightness);

		JMenuItem invertBrightness2 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 2)");
		invertBrightness2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.invertBrightness2();
				refreshMainView();
			}
		});
		invert.add(invertBrightness2);

		JMenu dampen = new JMenu("Dampen");
		menu.add(dampen);

		JMenuItem undampenStrongly = new JMenuItem("0.25 - Undampen Strongly");
		undampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(0.25f);
				refreshMainView();
			}
		});
		dampen.add(undampenStrongly);

		JMenuItem undampenWeakly = new JMenuItem("0.75 - Undampen Slightly");
		undampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(0.75f);
				refreshMainView();
			}
		});
		dampen.add(undampenWeakly);

		JMenuItem dampenWeakly1 = new JMenuItem("1.1 - Dampen Extremely Slightly");
		dampenWeakly1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(1.1f);
				refreshMainView();
			}
		});
		dampen.add(dampenWeakly1);

		JMenuItem dampenWeakly25 = new JMenuItem("1.25 - Dampen Very Slightly");
		dampenWeakly25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(1.25f);
				refreshMainView();
			}
		});
		dampen.add(dampenWeakly25);

		JMenuItem dampenWeakly = new JMenuItem("1.5 - Dampen Slightly");
		dampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(1.5f);
				refreshMainView();
			}
		});
		dampen.add(dampenWeakly);

		JMenuItem dampenStrongly = new JMenuItem("2.0 - Dampen Strongly");
		dampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(2);
				refreshMainView();
			}
		});
		dampen.add(dampenStrongly);

		JMenu darkenBrighten = new JMenu("Darken / Brighten");
		menu.add(darkenBrighten);

		JMenuItem curMenuItem;

		curMenuItem = new JMenuItem("0.25 - Darken Strongly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.editChannels("R", 0.25, "G", 0.25, "B", 0.25);
				refreshMainView();
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("0.75 - Darken Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.editChannels("R", 0.75, "G", 0.75, "B", 0.75);
				refreshMainView();
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.25 - Brighten Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.editChannels("R", 1.25, "G", 1.25, "B", 1.25);
				refreshMainView();
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.75 - Brighten Strongly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.editChannels("R", 1.75, "G", 1.75, "B", 1.75);
				refreshMainView();
			}
		});
		darkenBrighten.add(curMenuItem);

		JMenu intensify = new JMenu("Intensify");
		menu.add(intensify);

		/*
		Farben intensivieren:
		p^[1] := max255((p^[1] * p^[1]) div 128);
		p^[2] := max255((p^[2] * p^[2]) div 128);
		p^[3] := max255((p^[3] * p^[3]) div 128);
		*/
		curMenuItem = new JMenuItem("Intensify");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intensify();
				refreshMainView();
			}
		});
		intensify.add(curMenuItem);

		/*
		Farben leicht intensivieren:
		p^[1] := (max255((p^[1] * p^[1]) div 128) + p^[1]) div 2;
		p^[2] := (max255((p^[2] * p^[2]) div 128) + p^[2]) div 2;
		p^[3] := (max255((p^[3] * p^[3]) div 128) + p^[3]) div 2;
		*/
		curMenuItem = new JMenuItem("Intensify Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intensifySlightly();
				refreshMainView();
			}
		});
		intensify.add(curMenuItem);

		// intensifies colors, and the ones that achieve black or white are set to that,
		// but all others are kept as before, so if it was somewhere in the middle before,
		// it just stays exactly there
		curMenuItem = new JMenuItem("Intensify Extremes, but Keep Non-Intense Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intensifyExtremes();
				refreshMainView();
			}
		});
		intensify.add(curMenuItem);

		curMenuItem = new JMenuItem("Create Map of Extremes and Non-Intense Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.createMapOfExtremes(foregroundColor, backgroundColor);
				refreshMainView();
			}
		});
		intensify.add(curMenuItem);

		addPixelLevelDiffMapButtons(intensify);

		JMenu mixing = new JMenu("Mixing");
		menu.add(mixing);

		curMenuItem = new JMenuItem("Mix in Previous Image 90:10 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.1f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 75:25 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.25f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 66:33 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.3333f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 60:40 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.4f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 50:50 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.5f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 40:60 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.6f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 33:66 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.6666f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 25:75 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.75f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 10:90 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImage(otherPic, 0.9f);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Left (old) to Right (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageLeftToRight(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Right (old) to Left (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageRightToLeft(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Top (old) to Bottom (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageTopToBottom(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Bottom (old) to Top (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageBottomToTop(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image (applying min)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageMin(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image (applying max)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.intermixImageMax(otherPic);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mask out Previous Image (every same pixel becomes BG color)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture;
				saveCurPicForUndo();
				picture = picture.copy();
				picture.maskOutImage(otherPic, backgroundColor);
				refreshMainView();
			}
		});
		mixing.add(curMenuItem);

		/*
		TODO:
		add commandline options to be able to just automatically apply this or that editing directly... well... from the commandline :D
		(also much more efficient then, as no AWT nonsense needed in between!)

		add floor() and ceil() functions for channels (maybe on the channel editing gui?) where you can say e.g. R should be ceil(R, 128), or somesuch

		add slots on the left hand side, such that pictures can be pushed into these slots for later re-combination
		(however, this will use up even more memory... wäääh!)

		kopieren

		einfügen

		Farben entfernen:
		p^[1] := Trunc((p^[1] * 0.11) + (p^[2] * 0.59) + (p^[3] * 0.3));
		p^[2] := p^[1];
		p^[3] := p^[2];
		Inc(p);

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


		JMenu view = new JMenu("Window");
		menu.add(view);

		JMenuItem bgForeground = new JMenuItem("Set Window BG to Foreground Color");
		bgForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(foregroundColor.toColor());
			}
		});
		view.add(bgForeground);

		JMenuItem bgBackground = new JMenuItem("Set Window BG to Background Color");
		bgBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(backgroundColor.toColor());
			}
		});
		view.add(bgBackground);

		JMenuItem bgBlack = new JMenuItem("Set Window BG to Black");
		bgBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.black);
			}
		});
		view.add(bgBlack);

		JMenuItem bgGray = new JMenuItem("Set Window BG to Gray");
		bgGray.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.gray);
			}
		});
		view.add(bgGray);

		JMenuItem bgWhite = new JMenuItem("Set Window BG to White");
		bgWhite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.white);
			}
		});
		view.add(bgWhite);

		JMenuItem bgRed = new JMenuItem("Set Window BG to Red");
		bgRed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.red);
			}
		});
		view.add(bgRed);

		JMenuItem bgGreen = new JMenuItem("Set Window BG to Green");
		bgGreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.green);
			}
		});
		view.add(bgGreen);

		JMenuItem bgBlue = new JMenuItem("Set Window BG to Blue");
		bgBlue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor(Color.blue);
			}
		});
		view.add(bgBlue);


		JMenu huh = new JMenu("?");

		JMenuItem showFGColorCode = new JMenuItem("Show Foreground Color Code");
		showFGColorCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Hex Code: " + foregroundColor.toHexString() + "\n" +
					"rgba Code: " + foregroundColor.toString();
				JOptionPane.showMessageDialog(mainFrame, msg, "Color", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(showFGColorCode);

		JMenuItem showBGColorCode = new JMenuItem("Show Background Color Code");
		showBGColorCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "Hex Code: " + backgroundColor.toHexString() + "\n" +
					"rgba Code: " + backgroundColor.toString();
				JOptionPane.showMessageDialog(mainFrame, msg, "Color", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		huh.add(showBGColorCode);

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

	private void addPixelLevelDiffMapButtons(JMenuItem parentItem) {

		JMenuItem curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.createMapOfDifferences();
				refreshMainView();
			}
		});
		parentItem.add(curMenuItem);

		curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences (Black/White)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.createMapOfDifferencesBW();
				refreshMainView();
			}
		});
		parentItem.add(curMenuItem);
	}

	// magic numbers within this function correspond to mouse listener in the createMainPanel() function below
	private void refreshLeftView() {

		// background overall
		mainPanelLeftImg.drawRectangle(0, 0, 79, 199, windowBackgroundColor);

		// background color
		int left = 12;
		int top = 12;
		int right = left + 20;
		int bottom = top + 20;
		mainPanelLeftImg.drawRectangle(left, top, right, bottom, backgroundColor);

		// foreground color
		left = 5;
		top = 5;
		right = left + 20;
		bottom = top + 20;
		mainPanelLeftImg.drawRectangle(left, top, right, bottom, foregroundColor);

		// color switcher
		left = 52;
		top = 8;
		right = left + 20;
		bottom = top + 20;
		mainPanelLeftImg.drawRectangle(left, top, right, bottom, backgroundColor);
		for (int x = left; x <= right; x++) {
			for (int y = 1 + top + right - x; y <= bottom; y++) {
				mainPanelLeftImg.setPixel(x, y, foregroundColor);
			}
		}

		// colorpicker
		mainPanelLeftImg.draw(colorpickerImg, 0, 40);

		mainPanelLeftViewer.setImage(mainPanelLeftImg.getAwtImage());

		mainPanelLeft.revalidate();
		mainPanelLeft.repaint();
	}

	public void setPickedColor(ColorRGBA newColor) {
		if (colorToBePickedIsForeground) {
			setForegroundColor(newColor);
		} else {
			setBackgroundColor(newColor);
		}
	}

	private JPanel createMainPanel(JFrame parent) {

		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(800, 500));
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanel.setLayout(mainPanelLayout);

		mainPanelLeftImg = new Image(80, 200, ColorRGBA.WHITE);
		mainPanelLeftViewer = new ImageIcon();
		mainPanelLeftViewer.setImage(mainPanelLeftImg.getAwtImage());
		mainPanelLeftViewerLabel = new JLabel(mainPanelLeftViewer);
		mainPanelLeftViewerLabel.setBackground(windowBackgroundColor.toColor());
		mainPanelLeftViewerLabel.setOpaque(true);

		// magic numbers within the mouse listener correspond to refreshLeftView() function above
		mainPanelLeftViewerLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY() - ((mainPanelLeftViewerLabel.getHeight() - mainPanelLeftImg.getHeight()) / 2);
				if ((x >= 52) && (x < 72) && (y >= 8) && (y < 28)) {
					switchForeAndBackColor();
				}

				if ((x >= 0) && (x < 80) && (y >= 40) && (y < 168)) {
					System.out.println(e.getModifiersEx() + "");
					if (e.getModifiersEx() == 0) {
						setForegroundColor(colorpickerImg.getPixelSafely(x, y - 40));
					} else {
						setBackgroundColor(colorpickerImg.getPixelSafely(x, y - 40));
					}
				}
			}
		});

		mainPanelLeft = new JScrollPane(mainPanelLeftViewerLabel);
		mainPanelLeft.setPreferredSize(new Dimension(80, 1));
		mainPanelLeft.setBorder(BorderFactory.createEmptyBorder());

		mainPanel.add(mainPanelLeft, new Arrangement(0, 0, 0.0, 1.0));

		imageViewer = new ImageIcon();
		imageViewerLabel = new JLabel(imageViewer);
		imageViewerLabel.setBackground(Color.gray);
		imageViewerLabel.setOpaque(true);

		mainPanelRight = new JScrollPane(imageViewerLabel);
		mainPanelRight.setBorder(BorderFactory.createEmptyBorder());

		mainPanel.add(mainPanelRight, new Arrangement(1, 0, 1.0, 1.0));

		parent.add(mainPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private void refreshTitleBar() {
		mainFrame.setTitle(Main.PROGRAM_TITLE + " - " + lastPicturePath);
	}

	private void createNewEmptyFile() {
		setPicture(new Image(100, 100));
	}

	private Image getPdfPic() {

		Image pic = Image.createFromClipboard();

		ColorRGBA background = new ColorRGBA(255, 255, 255);
		int longestRunOverallLength = 0;
		int leftmostLongestRunTop = 0;
		int leftmostLongestRunBottom = 0;
		int leftmostLongestRunLeft = 0;
		int rightmostLongestRunRight = 0;

		// go from left to right across all columns
		for (int x = 0; x < pic.getWidth(); x++) {

			// check for each how long the longest run of white color is
			int curLongestRunStart = 0;
			int curLongestRunLength = 0;
			int curStart = 0;
			boolean curInARun = false;
			for (int y = 0; y < pic.getHeight(); y++) {
				if (pic.getPixel(x, y).equals(background)) {
					if (!curInARun) {
						curInARun = true;
						curStart = y;
					}
				} else {
					if (curInARun) {
						curInARun = false;
						int curLength = y - curStart;
						if (curLength > curLongestRunLength) {
							curLongestRunLength = curLength;
							curLongestRunStart = curStart;
						}
					}
				}
			}
			if (curInARun) {
				int curLength = pic.getHeight() - curStart;
				if (curLength > curLongestRunLength) {
					curLongestRunLength = curLength;
					curLongestRunStart = curStart;
				}
			}
			if (curLongestRunLength > longestRunOverallLength) {
				longestRunOverallLength = curLongestRunLength;
				leftmostLongestRunTop = curLongestRunStart;
				leftmostLongestRunBottom = curLongestRunStart + curLongestRunLength - 1;
				leftmostLongestRunLeft = x;
			}
			if (curLongestRunLength == longestRunOverallLength) {
				rightmostLongestRunRight = x;
			}
		}

		int picLeft = leftmostLongestRunLeft;
		int picTop = leftmostLongestRunTop;
		int picRight = rightmostLongestRunRight;
		int picBottom = leftmostLongestRunBottom;

		pic = pic.copy(picTop, picRight, picBottom, picLeft);

		return pic;
	}

	private void createPicFromPDF1() {

		Image pdfPic = getPdfPic();

		setPicture(pdfPic);
	}

	private void createPicFromPDF2() {

		// take the first picture (from the top of the page)
		Image origPic = picture.copy();

		int origPicHeight = origPic.getHeight();

		// and the second picture (from the bottom of the same page)
		Image pdfPic = getPdfPic();

		int overlapRows = 0;
		int potentialOverlapStart = 0;
		if (pdfPic.getHeight() < origPicHeight) {
			potentialOverlapStart = origPicHeight - pdfPic.getHeight();
		}

		// figure out how large the overlap is
		for (; potentialOverlapStart < origPicHeight; potentialOverlapStart++) {
			boolean isOverlapping = true;
			trynext:
			for (int y = potentialOverlapStart; (y < origPic.getHeight()) && (y - potentialOverlapStart < pdfPic.getHeight()); y++) {
				for (int x = 0; (x < origPic.getWidth()) && (x < pdfPic.getWidth()); x++) {
					if (!origPic.getPixel(x, y).fastVaguelySimilar(pdfPic.getPixel(x, y - potentialOverlapStart))) {
						isOverlapping = false;
						break trynext;
					}
				}
			}
			if (isOverlapping) {
				overlapRows = origPicHeight - potentialOverlapStart;
				break;
			}
		}

		if (overlapRows < 1) {
			GuiUtils.complain("Could not find any overlapping area; probably fonts shifted by a pixel up or down.\n" +
				"Try including a smaller (but existing) overlap.");
			return;
		}

		// and draw the second picture on top of the first picture, but leave the non-overlapping area untouched
		origPic.expandBottomBy(pdfPic.getHeight() - overlapRows, new ColorRGBA(255, 255, 255));

		origPic.draw(pdfPic, 0, origPicHeight - overlapRows);

		setPicture(origPic);

		// save the screenshot immediately automagically
		TEMP_DIR.create();
		File tempFile = null;
		int fileNum = 1;
		while (true) {
			tempFile = new File(TEMP_DIR, "pdf_" + fileNum + ".png");
			if (tempFile.exists()) {
				fileNum++;
			} else {
				break;
			}
		}
		saveImageToFile(origPic, tempFile);
	}

	private void createPicFromScreenshot(ColorRGBA background) {

		Image pic = Image.createFromClipboard();

		int startX = 88;
		int endX = 0;
		int startY = 0;
		int picLeft = 0;
		int picTop = 0;
		int picRight = 0;
		int picBottom = 0;

		// move from the top down until we find the first background pixel
		for (int y = 0; y < pic.getHeight(); y++) {
			if (pic.getPixel(startX, y).equals(background)) {
				startY = y;
				// move right until we find the last background pixel
				for (int x = startX; x < pic.getWidth(); x++) {
					if (!pic.getPixel(x, y).equals(background)) {
						endX = x;
						break;
					}
				}
				break;
			}
		}

		// move down until we find the first row in which the background is not a continuous row
		for (int y = startY; y < pic.getHeight(); y++) {
			for (int x = startX; x < endX; x++) {
				if (!pic.getPixel(x, y).equals(background)) {
					// we have found our upper left corner!
					picLeft = x;
					picTop = y;
					break;
				}
			}
			if (picLeft > 0) {
				break;
			}
		}

		// move right from the upper left corner until we find the upper right corner
		for (int x = picLeft; x < endX; x++) {
			if (pic.getPixel(x, picTop).equals(background)) {
				picRight = x - 1;
				break;
			}
		}

		// move down from the upper left corner until we find a whole row containing the same color;
		// this is one below the bottom
		for (int y = picTop; y < pic.getHeight(); y++) {
			ColorRGBA firstPixel = pic.getPixel(picLeft, y);
			boolean allPixelsSame = true;
			for (int x = picLeft; x <= picRight; x++) {
				if (!pic.getPixel(x, y).equals(firstPixel)) {
					allPixelsSame = false;
					break;
				}
			}
			if (allPixelsSame) {
				picBottom = y - 1;
				break;
			}
		}

		pic = pic.copy(picTop, picRight, picBottom, picLeft);

		setPicture(pic);
	}

	private void createScreenshot() {

		try {
			Robot robot = new Robot();

			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

			BufferedImage screenshotBufImg = robot.createScreenCapture(new Rectangle(screenDimension));

			Image screenshotImg = Image.createFromAwtImage(screenshotBufImg);

			setPicture(screenshotImg);

		} catch (AWTException e) {
			// whoops! guess not...
		}
	}

	public Image getPicture() {
		return picture.copy();
	}

	public void setPicture(Image newPicture) {

		saveCurPicForUndo();

		picture = newPicture;

		refreshMainView();

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
				picture = imageFileCtrl.loadImageFromFile(selectedFile);
				refreshMainView();

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
				saveImageToFile(picture, selectedFile);

				break;

			case JFileChooser.CANCEL_OPTION:
				// cancel was pressed... do nothing for now
				break;
		}
	}

	private void saveImageToFile(Image picture, File selectedFile) {
		imageFileCtrl.saveImageToFile(picture, selectedFile);
		lastPicturePath = selectedFile.getCanonicalFilename();
		refreshTitleBar();
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

	private void refreshMainView() {
		imageViewer.setImage(picture.getAwtImage());
		// imageViewerLabel.repaint();
		mainPanelRight.revalidate();
		mainPanelRight.repaint();
	}

	private void saveCurPicForUndo() {
		undoablePicture3 = undoablePicture2;
		undoablePicture2 = undoablePicture1;
		undoablePicture1 = undoablePicture;
		undoablePicture = picture;
		redoablePicture = null;
	}

	private void switchForeAndBackColor() {
		ColorRGBA prevBackgroundColor = backgroundColor;
		backgroundColor = foregroundColor;
		foregroundColor = prevBackgroundColor;
		refreshLeftView();
	}

	private void setForegroundColor(ColorRGBA col) {
		foregroundColor = col;
		refreshLeftView();
	}

	private void setBackgroundColor(ColorRGBA col) {
		backgroundColor = col;
		refreshLeftView();
	}

	private void setWindowBackgroundColor(Color newBgColor) {
		windowBackgroundColor = new ColorRGBA(newBgColor);
		imageViewerLabel.setBackground(newBgColor);
		mainPanelLeftViewerLabel.setBackground(newBgColor);
		refreshLeftView();
	}

	private void undo() {
		if (undoablePicture == null) {
			return;
		}
		redoablePicture = picture;
		picture = undoablePicture;
		undoablePicture = undoablePicture1;
		undoablePicture1 = undoablePicture2;
		undoablePicture2 = undoablePicture3;
		refreshMainView();
	}

	private void redo() {
		if (redoablePicture == null) {
			return;
		}
		undoablePicture3 = undoablePicture2;
		undoablePicture2 = undoablePicture1;
		undoablePicture1 = undoablePicture;
		undoablePicture = picture;
		picture = redoablePicture;
		redoablePicture = null;
		refreshMainView();
	}

}
