/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.gui.Arrangement;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.gui.MainWindow;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageFile;
import com.asofterspace.toolbox.images.ImageFileCtrl;
import com.asofterspace.toolbox.images.ImageLayer;
import com.asofterspace.toolbox.images.ImageLayerBasedOnImage;
import com.asofterspace.toolbox.images.ImageLayerBasedOnText;
import com.asofterspace.toolbox.images.ImageMultiLayered;
import com.asofterspace.toolbox.images.PicFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.pdf.PdfImageHandler;
import com.asofterspace.toolbox.utils.Pair;
import com.asofterspace.toolbox.utils.StrUtils;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


public class GUI extends MainWindow {

	private final static String CONFIG_KEY_WIDTH = "mainFrameWidth";
	private final static String CONFIG_KEY_HEIGHT = "mainFrameHeight";
	private final static String CONFIG_KEY_LEFT = "mainFrameLeft";
	private final static String CONFIG_KEY_TOP = "mainFrameTop";
	private final static String CONFIG_KEY_LAST_DIRECTORY = "lastDirectory";

	final static String ADJUST_CHANNEL_GUI_STR = "Adjust Channels Manually in Detail...";

	private final static String TOOL_SELECTED_START_STR = ">> ";
	private final static String TOOL_SELECTED_END_STR = " <<";
	private final static String TOOL_TEXTS_PIPETTE_FG = "Set Foreground to a Particular Color (Pipette Tool)";
	private final static String TOOL_TEXTS_PIPETTE_BG = "Set Background to a Particular Color (Pipette Tool)";
	private final static String TOOL_TEXTS_RECTANGLE_FG = "Draw Rectangle with Foreground Color";
	private final static String TOOL_TEXTS_RECTANGLE_BG = "Draw Rectangle with Background Color";
	private final static String TOOL_TEXTS_AREA_FG = "Draw Area with Foreground Color";
	private final static String TOOL_TEXTS_AREA_BG = "Draw Area with Background Color";

	private String lastSavePath;
	private String lastExportPath;

	private JPanel mainPanel;
	private JPanel mainPanelLeft;
	private JList<String> layerList;
	private JScrollPane mainPanelRight;
	private JPanel imgLayerPanel;
	private JLabel imgLayerLabel;
	private JTextField imgLayerOffsetXInput;
	private JTextField imgLayerOffsetYInput;
	private JPanel textLayerPanel;
	private JLabel textLayerLabel;
	private JTextField textLayerOffsetXInput;
	private JTextField textLayerOffsetYInput;
	private JTextField textLayerFontNameInput;
	private JTextField textLayerFontSizeInput;
	private JTextField textLayerColorInput;
	private JTextField textLayerTextInput;
	private JPanel mainLowerPanelLeft;
	private JLabel curPosXLabel;
	private JLabel curPosYLabel;
	private JLabel curPosWLabel;
	private JLabel curPosHLabel;

	private JMenuItem saveAgain;
	private JMenuItem exportAgain;
	private JMenuItem close;
	private JMenuItem setForegroundToPipette;
	private JMenuItem setForegroundToPipette4;
	private JMenuItem setForegroundToPipette12;
	private JMenuItem setBackgroundToPipette;
	private JMenuItem drawRectangleFG;
	private JMenuItem drawRectangleBG;
	private JMenuItem drawAreaFG;
	private JMenuItem drawAreaBG;

	private ConfigFile configuration;
	private String fileToOpenAfterStartup;

	private Image mainPanelLeftImg;
	private ImageIcon mainPanelLeftViewer;
	private JLabel mainPanelLeftViewerLabel;

	private ImageIcon imageViewer;
	private JLabel imageViewerLabel;
	private ImageMultiLayered picture;
	private ImageMultiLayered undoablePicture3;
	private ImageMultiLayered undoablePicture2;
	private ImageMultiLayered undoablePicture1;
	private ImageMultiLayered undoablePicture;
	private ImageMultiLayered redoablePicture;

	private QrGUI qrGUI = null;
	private ChannelChangeGUI channelChangeGUI = null;
	private CreateGridGUI createGridGUI = null;
	private ExpandShrinkGUI expandShrinkGUI = null;
	private ResizeGUI resizeGUI = null;
	private ColorPickerGUI colorPickerGUI = null;
	private boolean colorToBePickedIsForeground = true;

	private ImageFileCtrl imageFileCtrl;
	private Image colorpickerImg;

	private Tool activeTool = null;
	private ColorRGBA foregroundColor = ColorRGBA.BLACK;
	private ColorRGBA backgroundColor = ColorRGBA.WHITE;
	private ColorRGBA windowBackgroundColor = new ColorRGBA(Color.gray);

	private int currentLayerIndex = 0;
	private int pipetteSize = 1;
	private List<Pair<Integer, Integer>> lastDrawPoints = new ArrayList<>();
	private Image pictureBeforePointDrawing;
	private int prevClickX = 0;
	private int prevClickY = 0;
	private int lastClickX = 0;
	private int lastClickY = 0;

	private boolean savedSinceLastChange = true;

	private final static Directory TEMP_DIR = new Directory("temp");


	public GUI(ConfigFile configFile, String fileToOpen) {
		this.configuration = configFile;
		this.fileToOpenAfterStartup = fileToOpen;

		// we want to handle as many image formats as we can...
		// even opening images from PDF files, if possible :)
		this.imageFileCtrl = new ImageFileCtrl();
		this.imageFileCtrl.addHandler(new PdfImageHandler());

		colorpickerImg = imageFileCtrl.loadImageFromFile(new File(
			 System.getProperty("java.class.path") + "/../res/colorpicker.png"));

		// enable anti-aliasing for swing
		System.setProperty("swing.aatext", "true");
		// enable anti-aliasing for awt
		System.setProperty("awt.useSystemAAFontSettings", "on");
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

				if (fileToOpenAfterStartup != null) {
					boolean returnImage = false;
					openImageFile(new File(fileToOpenAfterStartup), returnImage);
				}
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

		JMenuItem newGrid1 = new JMenuItem("Empty Grid to Align Images");
		newGrid1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = true;
				showGridGUI(createNew);
			}
		});
		newFile.add(newGrid1);


		JMenuItem openFile = new JMenuItem("Open / Import...");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean returnImage = false;
				openFile(returnImage);
			}
		});
		file.add(openFile);

		JMenuItem saveFileAs = new JMenuItem("Save As...");
		saveFileAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean exporting = false;
				saveOrExportFile(exporting);
			}
		});
		file.add(saveFileAs);

		saveAgain = new JMenuItem("");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAgain();
			}
		});
		saveAgain.setEnabled(false);
		file.add(saveAgain);

		JMenuItem exportFileTo = new JMenuItem("Export To...");
		exportFileTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean exporting = true;
				saveOrExportFile(exporting);
			}
		});
		file.add(exportFileTo);

		exportAgain = new JMenuItem("");
		exportAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportFileAgain();
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
				picture.clear();
				setPictureUndoTakenCareOf(picture);
			}
		});
		edit.add(clear);

		edit.addSeparator();

		JMenuItem copy = new JMenuItem("Copy All");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.bake().copyToClipboard();
			}
		});
		edit.add(copy);

		JMenuItem copyClickedArea = new JMenuItem("Copy Area Bounded by Last Two Clicks");
		copyClickedArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int top = Math.min(prevClickY, lastClickY);
				int right = Math.max(prevClickX, lastClickX);
				int bottom = Math.max(prevClickY, lastClickY);
				int left = Math.min(prevClickX, lastClickX);

				picture.bake().copy(top, right, bottom, left).copyToClipboard();
			}
		});
		edit.add(copyClickedArea);

		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPicture(Image.createFromClipboard());
			}
		});
		edit.add(paste);

		edit.addSeparator();

		JMenuItem expandShrinkImgArea = new JMenuItem("Expand / Shrink Image Area");
		expandShrinkImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showExpandShrinkGUI();
			}
		});
		edit.add(expandShrinkImgArea);

		JMenuItem resizeImgArea = new JMenuItem("Resize Image Area");
		resizeImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResizeGUI(false);
			}
		});
		edit.add(resizeImgArea);

		JMenuItem resampleImgArea = new JMenuItem("Resample Image Area");
		resampleImgArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResizeGUI(true);
			}
		});
		edit.add(resampleImgArea);

		edit.addSeparator();

		JMenuItem turnLeft = new JMenuItem("Turn Left");
		turnLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				Image img = picture.bake();
				img.rotateLeft();
				setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnLeft);

		JMenuItem turnRight = new JMenuItem("Turn Right");
		turnRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				Image img = picture.bake();
				img.rotateRight();
				setPictureUndoTakenCareOf(new ImageMultiLayered(img));
			}
		});
		edit.add(turnRight);

		JMenuItem reflectHorizontally = new JMenuItem("Reflect Horizontally");
		reflectHorizontally.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().reflectHorizontally();
				setPictureUndoTakenCareOf(picture);
			}
		});
		edit.add(reflectHorizontally);

		JMenuItem reflectVertically = new JMenuItem("Reflect Vertically");
		reflectVertically.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().reflectVertically();
				setPictureUndoTakenCareOf(picture);
			}
		});
		edit.add(reflectVertically);


		JMenu layers = new JMenu("Layers");
		menu.add(layers);

		JMenuItem delLayer = new JMenuItem("Delete Current Layer");
		delLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture.deleteLayer(currentLayerIndex);
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(delLayer);

		JMenuItem addImgLayer = new JMenuItem("Add Image Layer");
		addImgLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0,
					new Image(picture.getWidth(), picture.getHeight(), backgroundColor));
				picture.addLayer(layer);
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(addImgLayer);

		JMenuItem addImgLayerFile = new JMenuItem("Add Image Layer (Opening Existing File)");
		addImgLayerFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				boolean returnImage = true;
				Image img = openFile(returnImage);
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0, img);
				picture.addLayer(layer);
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(addImgLayerFile);

		JMenuItem addImgLayerClipbrd = new JMenuItem("Add Image Layer (Pasting from Clipboard)");
		addImgLayerClipbrd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(0, 0, Image.createFromClipboard());
				picture.addLayer(layer);
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(addImgLayerClipbrd);

		JMenuItem addImgLayerClickedArea = new JMenuItem("Add Image Layer (Based on Area Bounded by Last Two Clicks)");
		addImgLayerClickedArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int top = Math.min(prevClickY, lastClickY);
				int right = Math.max(prevClickX, lastClickX);
				int bottom = Math.max(prevClickY, lastClickY);
				int left = Math.min(prevClickX, lastClickX);

				saveCurPicForUndo();
				ImageLayerBasedOnImage layer = new ImageLayerBasedOnImage(left, top, picture.bake().copy(top, right, bottom, left));
				picture.addLayer(layer);
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(addImgLayerClickedArea);

		JMenuItem addTextLayer = new JMenuItem("Add Text Layer");
		addTextLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				ImageLayerBasedOnText layer = new ImageLayerBasedOnText(0, 0, "Hello!", "Calibri", 32, foregroundColor);
				picture.addLayer(layer);
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(addTextLayer);

		JMenuItem duplicateLayer = new JMenuItem("Duplicate Current Layer");
		duplicateLayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture.addLayer(getCurrentLayer());
				currentLayerIndex = picture.getLayerAmount() - 1;
				setPictureUndoTakenCareOf(picture);
				refreshLayerView();
			}
		});
		layers.add(duplicateLayer);

		layers.addSeparator();

		JMenuItem selLayerUp = new JMenuItem("Select One Layer Up");
		selLayerUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentLayerIndex++;
				refreshLayerView();
			}
		});
		layers.add(selLayerUp);

		JMenuItem selLayerDown = new JMenuItem("Select One Layer Down");
		selLayerDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentLayerIndex--;
				refreshLayerView();
			}
		});
		layers.add(selLayerDown);

		layers.addSeparator();

		JMenuItem moveLayerAllUp = new JMenuItem("Move Selected Layer All Layers Up / Front");
		moveLayerAllUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.moveLayerFullyUp(currentLayerIndex);
				currentLayerIndex = picture.getLayerAmount() - 1;
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveLayerAllUp);

		JMenuItem moveLayerUp = new JMenuItem("Move Selected Layer One Layer Up");
		moveLayerUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.moveLayerOneUp(currentLayerIndex);
				currentLayerIndex++;
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveLayerUp);

		JMenuItem moveLayerDown = new JMenuItem("Move Selected Layer One Layer Down");
		moveLayerDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.moveLayerOneDown(currentLayerIndex);
				currentLayerIndex--;
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveLayerDown);

		JMenuItem moveLayerAllDown = new JMenuItem("Move Selected Layer All Layers Down / Back");
		moveLayerAllDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picture.moveLayerFullyDown(currentLayerIndex);
				currentLayerIndex = 0;
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveLayerAllDown);

		layers.addSeparator();

		JMenuItem moveToLeft = new JMenuItem("Move Selected Layer to Left");
		moveToLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = getCurrentLayer();
				curLayer.moveTo(0, curLayer.getOffsetY());
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveToLeft);

		JMenuItem moveToRight = new JMenuItem("Move Selected Layer to Right");
		moveToRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = getCurrentLayer();
				curLayer.moveTo(picture.getWidth() - curLayer.getWidth(), curLayer.getOffsetY());
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveToRight);

		JMenuItem moveToTop = new JMenuItem("Move Selected Layer to Top");
		moveToTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = getCurrentLayer();
				curLayer.moveTo(curLayer.getOffsetX(), 0);
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveToTop);

		JMenuItem moveToBottom = new JMenuItem("Move Selected Layer to Bottom");
		moveToBottom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = getCurrentLayer();
				curLayer.moveTo(curLayer.getOffsetX(), picture.getHeight() - curLayer.getHeight());
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveToBottom);

		JMenuItem moveToCenter = new JMenuItem("Move Selected Layer to Center");
		moveToCenter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayer curLayer = getCurrentLayer();
				curLayer.moveTo(
					(picture.getWidth() - curLayer.getWidth()) / 2,
					(picture.getHeight() - curLayer.getHeight()) / 2
				);
				refreshMainView();
				refreshLayerView();
			}
		});
		layers.add(moveToCenter);

		layers.addSeparator();

		JMenuItem bakeAllLayers = new JMenuItem("Bake All Layers Into One");
		bakeAllLayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = new ImageMultiLayered(picture.bake());
				setPictureUndoTakenCareOf(picture);
			}
		});
		layers.add(bakeAllLayers);


		JMenu tools = new JMenu("Tools");
		menu.add(tools);

		JMenuItem unsetTool = new JMenuItem("Unset Currently Used Tool");
		unsetTool.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				activeTool = null;
				refreshTools();
			}
		});
		tools.add(unsetTool);


		JMenu grid = new JMenu("Grid");
		menu.add(grid);

		JMenuItem newGrid2 = new JMenuItem("Create Empty Grid to Align Images");
		newGrid2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = true;
				showGridGUI(createNew);
			}
		});
		grid.add(newGrid2);

		JMenuItem copyToGrid = new JMenuItem("Copy Current Image Multiple Times into Grid");
		copyToGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = false;
				showGridGUI(createNew);
			}
		});
		grid.add(copyToGrid);


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

		colors.addSeparator();

		setForegroundToPipette = new JMenuItem(TOOL_TEXTS_PIPETTE_FG);
		setForegroundToPipette.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((activeTool == Tool.PIPETTE_FG) && (pipetteSize == 1)) {
					activeTool = null;
				} else {
					activeTool = Tool.PIPETTE_FG;
					pipetteSize = 1;
				}
				refreshTools();
			}
		});
		colors.add(setForegroundToPipette);

		setForegroundToPipette4 = new JMenuItem(TOOL_TEXTS_PIPETTE_FG + " + 4 Pixels Around");
		setForegroundToPipette4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((activeTool == Tool.PIPETTE_FG) && (pipetteSize == 5)) {
					activeTool = null;
				} else {
					activeTool = Tool.PIPETTE_FG;
					pipetteSize = 5;
				}
				refreshTools();
			}
		});
		colors.add(setForegroundToPipette4);

		setForegroundToPipette12 = new JMenuItem(TOOL_TEXTS_PIPETTE_FG + " + 12 Pixels Around");
		setForegroundToPipette12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((activeTool == Tool.PIPETTE_FG) && (pipetteSize == 13)) {
					activeTool = null;
				} else {
					activeTool = Tool.PIPETTE_FG;
					pipetteSize = 13;
				}
				refreshTools();
			}
		});
		colors.add(setForegroundToPipette12);

		setBackgroundToPipette = new JMenuItem(TOOL_TEXTS_PIPETTE_BG);
		setBackgroundToPipette.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pipetteSize = 1;
				if (activeTool == Tool.PIPETTE_BG) {
					activeTool = null;
				} else {
					activeTool = Tool.PIPETTE_BG;
				}
				refreshTools();
			}
		});
		colors.add(setBackgroundToPipette);

		JMenuItem setForegroundToColorPickerGUI = new JMenuItem("Set Foreground to a Particular Color (GUI Menu)");
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

		JMenuItem setBackgroundToColorPickerGUI = new JMenuItem("Set Background to a Particular Color (GUI Menu)");
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

		colors.addSeparator();

		JMenuItem setForegroundToMostCommon = new JMenuItem("Set Foreground to Most Common Color");
		setForegroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setForegroundColor(picture.bake().getMostCommonColor());
			}
		});
		colors.add(setForegroundToMostCommon);

		JMenuItem setBackgroundToMostCommon = new JMenuItem("Set Background to Most Common Color");
		setBackgroundToMostCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBackgroundColor(picture.bake().getMostCommonColor());
			}
		});
		colors.add(setBackgroundToMostCommon);

		JMenuItem setForegroundToMostCommonSur = new JMenuItem("Set Foreground to Most Common Surrounding Color");
		setForegroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setForegroundColor(picture.bake().getMostCommonSurroundingColor());
			}
		});
		colors.add(setForegroundToMostCommonSur);

		JMenuItem setBackgroundToMostCommonSur = new JMenuItem("Set Background to Most Common Surrounding Color");
		setBackgroundToMostCommonSur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBackgroundColor(picture.bake().getMostCommonSurroundingColor());
			}
		});
		colors.add(setBackgroundToMostCommonSur);


		JMenu draw = new JMenu("Draw");
		menu.add(draw);

		drawRectangleFG = new JMenuItem(TOOL_TEXTS_RECTANGLE_FG);
		drawRectangleFG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeTool == Tool.DRAW_RECTANGLE_FG) {
					activeTool = null;
				} else {
					activeTool = Tool.DRAW_RECTANGLE_FG;
				}
				refreshTools();
			}
		});
		draw.add(drawRectangleFG);

		drawRectangleBG = new JMenuItem(TOOL_TEXTS_RECTANGLE_BG);
		drawRectangleBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeTool == Tool.DRAW_RECTANGLE_BG) {
					activeTool = null;
				} else {
					activeTool = Tool.DRAW_RECTANGLE_BG;
				}
				refreshTools();
			}
		});
		draw.add(drawRectangleBG);

		drawAreaFG = new JMenuItem(TOOL_TEXTS_AREA_FG);
		drawAreaFG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeTool == Tool.DRAW_AREA_FG) {
					activeTool = null;
				} else {
					activeTool = Tool.DRAW_AREA_FG;
				}
				refreshTools();
			}
		});
		draw.add(drawAreaFG);

		drawAreaBG = new JMenuItem(TOOL_TEXTS_AREA_BG);
		drawAreaBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeTool == Tool.DRAW_AREA_BG) {
					activeTool = null;
				} else {
					activeTool = Tool.DRAW_AREA_BG;
				}
				refreshTools();
			}
		});
		draw.add(drawAreaBG);


		JMenu adjustPixels = new JMenu("Pixels");
		menu.add(adjustPixels);

		JMenuItem replaceEvWithForeground = new JMenuItem("Replace Everything with Foreground Color");
		replaceEvWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().clear(foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceEvWithForeground);

		JMenuItem replaceEvWithBackground = new JMenuItem("Replace Everything with Background Color");
		replaceEvWithBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().clear(backgroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceEvWithBackground);

		adjustPixels.addSeparator();

		JMenuItem replaceBackgroundForeground = new JMenuItem("Replace Background Color with Foreground Color");
		replaceBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(backgroundColor, foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceBackgroundForeground);

		JMenuItem replaceVCBackgroundForeground = new JMenuItem("Replace Very-Close-to-Background Color with Foreground Color");
		replaceVCBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(backgroundColor, foregroundColor, 24);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceVCBackgroundForeground);

		JMenuItem replaceCBackgroundForeground = new JMenuItem("Replace Close-to-Background Color with Foreground Color");
		replaceCBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(backgroundColor, foregroundColor, 64);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceCBackgroundForeground);

		JMenuItem replaceSBackgroundForeground = new JMenuItem("Replace Similar-ish-to-Background Color with Foreground Color");
		replaceSBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(backgroundColor, foregroundColor, 128);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceSBackgroundForeground);

		JMenuItem replaceVSBackgroundForeground = new JMenuItem("Replace Vaguely-Similar-ish-to-Background Color with Foreground Color");
		replaceVSBackgroundForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(backgroundColor, foregroundColor, 255);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceVSBackgroundForeground);

		adjustPixels.addSeparator();

		JMenuItem replaceAnythingButFgWithBg = new JMenuItem("Replace Anything but Foreground Color with Background Color");
		replaceAnythingButFgWithBg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColorsExcept(foregroundColor, backgroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceAnythingButFgWithBg);

		JMenuItem replaceAnythingButMcWithFg = new JMenuItem("Replace Anything but Most Common Color with Foreground Color");
		replaceAnythingButMcWithFg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColorsExcept(
					getCurrentImageLayer().getImage().getMostCommonColor(), foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceAnythingButMcWithFg);

		adjustPixels.addSeparator();

		JMenuItem replaceMostCommonForeground = new JMenuItem("Replace Most Common Color with Foreground Color");
		replaceMostCommonForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(
					getCurrentImageLayer().getImage().getMostCommonColor(), foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceMostCommonForeground);

		JMenuItem replaceMostCommonSurroundingForeground = new JMenuItem("Replace Most Common Surrounding Color with Foreground Color");
		replaceMostCommonSurroundingForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceColors(
					getCurrentImageLayer().getImage().getMostCommonSurroundingColor(), foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceMostCommonSurroundingForeground);

		adjustPixels.addSeparator();

		JMenuItem replaceStragglersWithForeground = new JMenuItem("Replace Stragglers (Single Pixels) based on Background Color with Foreground Color");
		replaceStragglersWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceStragglersWith(backgroundColor, foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceStragglersWithForeground);

		JMenuItem replaceStragglersIshWithForeground = new JMenuItem("Replace Stragglers-ish (Single-ish Pixels) based on Background Color with Foreground Color");
		replaceStragglersIshWithForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().replaceStragglersIshWith(backgroundColor, foregroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustPixels.add(replaceStragglersIshWithForeground);

		addPixelLevelDiffMapButtons(adjustPixels);


		JMenu adjustColors = new JMenu("Channels");
		menu.add(adjustColors);

		JMenuItem removeAbsoluteColors = new JMenuItem("Remove Colors by Absolute Brightness (to Grayscale)");
		removeAbsoluteColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().removeColors();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(removeAbsoluteColors);

		JMenuItem removePerceivedColors = new JMenuItem("Remove Colors by Perceived Brightness (to Grayscale)");
		removePerceivedColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().removePerceivedColors();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(removePerceivedColors);

		JMenuItem removeAbsoluteColorsBW = new JMenuItem("Remove Colors by Absolute Brightness (to Black/White)");
		removeAbsoluteColorsBW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().removeColors();
				getCurrentImageLayer().getImage().makeBlackAndWhite();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(removeAbsoluteColorsBW);

		JMenuItem removePerceivedColorsBW = new JMenuItem("Remove Colors by Perceived Brightness (to Black/White)");
		removePerceivedColorsBW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().removePerceivedColors();
				getCurrentImageLayer().getImage().makeBlackAndWhite();
				setPictureUndoTakenCareOf(picture);
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
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().extractBlackToAlpha();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(extractBlackToAlpha);

		JMenuItem extractWhiteToAlpha = new JMenuItem("Extract White to Alpha");
		extractWhiteToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().extractWhiteToAlpha();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(extractWhiteToAlpha);

		JMenuItem extractBgColToAlpha = new JMenuItem("Extract Background Color to Alpha");
		extractBgColToAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().extractBackgroundColorToAlpha();
				setPictureUndoTakenCareOf(picture);
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
				getCurrentImageLayer().getImage().removeAlpha();
				setPictureUndoTakenCareOf(picture);
			}
		});
		adjustColors.add(removeAlpha);

		JMenuItem bakeCurrentView = new JMenuItem("Remove Alpha Channel by Baking In Current Background View");
		bakeCurrentView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().bakeAlpha(new ColorRGBA(imageViewerLabel.getBackground()));
				setPictureUndoTakenCareOf(picture);
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
					channelChangeGUI = new ChannelChangeGUI(GUI.this);
				}
				channelChangeGUI.show();
			}
		});
		adjustColors.add(channelAdjust);


		JMenu invert = new JMenu("Invert");
		menu.add(invert);

		JMenuItem invertColors = new JMenuItem("Invert Colors");
		invertColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().invert();
				setPictureUndoTakenCareOf(picture);
			}
		});
		invert.add(invertColors);

		JMenuItem invertBrightness = new JMenuItem("Invert Brightness (Keeping Colors, Approach 1)");
		invertBrightness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().invertBrightness1();
				setPictureUndoTakenCareOf(picture);
			}
		});
		invert.add(invertBrightness);

		JMenuItem invertBrightness2 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 2)");
		invertBrightness2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().invertBrightness2();
				setPictureUndoTakenCareOf(picture);
			}
		});
		invert.add(invertBrightness2);

		JMenuItem invertBrightness3 = new JMenuItem("Invert Brightness (Keeping Colors, Approach 3)");
		invertBrightness3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().invertBrightness3();
				setPictureUndoTakenCareOf(picture);
			}
		});
		invert.add(invertBrightness3);


		JMenu dampen = new JMenu("Dampen");
		menu.add(dampen);

		JMenuItem undampenStrongly = new JMenuItem("0.25 - Undampen Strongly");
		undampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(0.25f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		dampen.add(undampenStrongly);

		JMenuItem undampenWeakly = new JMenuItem("0.75 - Undampen Slightly");
		undampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(0.75f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		dampen.add(undampenWeakly);

		JMenuItem dampenWeakly1 = new JMenuItem("1.1 - Dampen Extremely Slightly");
		dampenWeakly1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(1.1f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		dampen.add(dampenWeakly1);

		JMenuItem dampenWeakly25 = new JMenuItem("1.25 - Dampen Very Slightly");
		dampenWeakly25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(1.25f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		dampen.add(dampenWeakly25);

		JMenuItem dampenWeakly = new JMenuItem("1.5 - Dampen Slightly");
		dampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(1.5f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		dampen.add(dampenWeakly);

		JMenuItem dampenStrongly = new JMenuItem("2.0 - Dampen Strongly");
		dampenStrongly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().dampen(2);
				setPictureUndoTakenCareOf(picture);
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
				getCurrentImageLayer().getImage().editChannels("R", 0.25, "G", 0.25, "B", 0.25);
				setPictureUndoTakenCareOf(picture);
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("0.75 - Darken Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().editChannels("R", 0.75, "G", 0.75, "B", 0.75);
				setPictureUndoTakenCareOf(picture);
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.25 - Brighten Slightly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().editChannels("R", 1.25, "G", 1.25, "B", 1.25);
				setPictureUndoTakenCareOf(picture);
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.75 - Brighten Strongly");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().editChannels("R", 1.75, "G", 1.75, "B", 1.75);
				setPictureUndoTakenCareOf(picture);
			}
		});
		darkenBrighten.add(curMenuItem);

		curMenuItem = new JMenuItem("1.75 - Brighten Strongly Above Brightness Cutoff");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				int cutoff = 256+256;
				getCurrentImageLayer().getImage().editChannelsAboveCutoff(
					"R", 1.75, "G", 1.75, "B", 1.75, ColorRGBA.DEFAULT_ALLOW_OVERFLOW, cutoff);
				setPictureUndoTakenCareOf(picture);
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
				getCurrentImageLayer().getImage().intensify();
				setPictureUndoTakenCareOf(picture);
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
				getCurrentImageLayer().getImage().intensifySlightly();
				setPictureUndoTakenCareOf(picture);
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
				getCurrentImageLayer().getImage().intensifyExtremes();
				setPictureUndoTakenCareOf(picture);
			}
		});
		intensify.add(curMenuItem);

		intensify.addSeparator();

		curMenuItem = new JMenuItem("Create Map of Extremes and Non-Intense Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().createMapOfExtremes(foregroundColor, backgroundColor);
				setPictureUndoTakenCareOf(picture);
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
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.1f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 75:25 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.25f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 66:33 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.3333f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 60:40 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.4f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 50:50 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.5f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 40:60 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.6f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 33:66 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.6666f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 25:75 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.75f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image 10:90 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImage(otherPic, 0.9f);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mix in Previous Image from Left (old) to Right (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageLeftToRight(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Right (old) to Left (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageRightToLeft(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Top (old) to Bottom (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageTopToBottom(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image from Bottom (old) to Top (new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageBottomToTop(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mix in Previous Image (applying min)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageMin(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		curMenuItem = new JMenuItem("Mix in Previous Image (applying max)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().intermixImageMax(otherPic);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Mask out Previous Image (every same pixel becomes BG color)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().maskOutImage(otherPic, backgroundColor);
				setPictureUndoTakenCareOf(picture);
			}
		});
		mixing.add(curMenuItem);

		mixing.addSeparator();

		curMenuItem = new JMenuItem("Interlace Previous Image 50:50 (old:new)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image otherPic = undoablePicture.bake();
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().interlaceImage(otherPic);
				setPictureUndoTakenCareOf(picture);
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

		JMenuItem bgPurple = new JMenuItem("Set Window BG to Purple");
		bgPurple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWindowBackgroundColor((new ColorRGBA(80, 0, 110)).toColor());
			}
		});
		view.add(bgPurple);


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

		huh.addSeparator();

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

		JTextField arrowKeyInputField = new JTextField();
		menu.add(arrowKeyInputField);
		arrowKeyInputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				moveCurrentLayerBasedOnKeyEvent(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// moveCurrentLayerBasedOnKeyEvent(e);
			}
		});

		parent.setJMenuBar(menu);

		return menu;
	}

	private void moveCurrentLayerBasedOnKeyEvent(KeyEvent e) {
		int keyCode = e.getKeyCode();
		int modifier = 1;
		if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK) {
			modifier = 8;
		}
		switch (keyCode) {
			case KeyEvent.VK_UP:
				getCurrentLayer().move(0, -modifier);
				refreshMainView();
				refreshLayerView();
				break;
			case KeyEvent.VK_DOWN:
				getCurrentLayer().move(0, modifier);
				refreshMainView();
				refreshLayerView();
				break;
			case KeyEvent.VK_LEFT:
				getCurrentLayer().move(-modifier, 0);
				refreshMainView();
				refreshLayerView();
				break;
			case KeyEvent.VK_RIGHT:
				getCurrentLayer().move(modifier, 0);
				refreshMainView();
				refreshLayerView();
				break;
		 }
	}

	private void addPixelLevelDiffMapButtons(JMenuItem parentItem) {

		JMenuItem curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().createMapOfDifferences();
				setPictureUndoTakenCareOf(picture);
			}
		});
		parentItem.add(curMenuItem);

		curMenuItem = new JMenuItem("Create Map of Pixel-Level Differences (Black/White)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				getCurrentImageLayer().getImage().createMapOfDifferencesBW();
				setPictureUndoTakenCareOf(picture);
			}
		});
		parentItem.add(curMenuItem);
	}

	private void drawBlackWhiteBackground(int left, int top, int right, int bottom, int colFieldThirdSize) {
		// fill with black
		mainPanelLeftImg.drawRectangle(left, top, right, bottom, ColorRGBA.BLACK);

		// draw five white rectangles to achieve an overall tiled look
		mainPanelLeftImg.drawRectangle(left, top, left + colFieldThirdSize - 1, top + colFieldThirdSize - 1, ColorRGBA.WHITE);
		mainPanelLeftImg.drawRectangle(left + colFieldThirdSize, top + colFieldThirdSize,
			left + (2*colFieldThirdSize) - 1, top + (2*colFieldThirdSize) - 1, ColorRGBA.WHITE);
		mainPanelLeftImg.drawRectangle(left + (2*colFieldThirdSize), top + (2*colFieldThirdSize), right, bottom, ColorRGBA.WHITE);
		mainPanelLeftImg.drawRectangle(left, top + (2*colFieldThirdSize), left + colFieldThirdSize - 1, bottom, ColorRGBA.WHITE);
		mainPanelLeftImg.drawRectangle(left + (2*colFieldThirdSize), top, right, top + colFieldThirdSize - 1, ColorRGBA.WHITE);
	}

	// magic numbers within this function correspond to mouse listener in the createMainPanel() function below
	private void refreshLeftView() {

		int colFieldSize = 21;
		int colFieldThirdSize = colFieldSize / 3;

		// background overall
		mainPanelLeftImg.drawRectangle(0, 0, 79, 199, windowBackgroundColor);

		// background color
		int left = 12;
		int top = 12;
		int right = left + colFieldSize;
		int bottom = top + colFieldSize;
		drawBlackWhiteBackground(left, top, right, bottom, colFieldThirdSize);
		mainPanelLeftImg.drawRectangleWithTransparency(left, top, right, bottom, backgroundColor);

		// foreground color
		left = 2;
		top = 2;
		right = left + colFieldSize;
		bottom = top + colFieldSize;
		drawBlackWhiteBackground(left, top, right, bottom, colFieldThirdSize);
		mainPanelLeftImg.drawRectangleWithTransparency(left, top, right, bottom, foregroundColor);

		// color switcher
		left = 52;
		top = 8;
		right = left + colFieldSize;
		bottom = top + colFieldSize;
		ColorRGBA bgColNoTrans = backgroundColor.getWithoutTransparency();
		ColorRGBA fgColNoTrans = foregroundColor.getWithoutTransparency();
		mainPanelLeftImg.drawRectangle(left, top, right, bottom, bgColNoTrans);
		for (int x = left; x <= right; x++) {
			for (int y = 1 + top + right - x; y <= bottom; y++) {
				mainPanelLeftImg.setPixel(x, y, fgColNoTrans);
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


		textLayerPanel = new JPanel();
		textLayerPanel.setPreferredSize(new Dimension(100, 20));
		GridBagLayout textLayerPanelLayout = new GridBagLayout();
		textLayerPanel.setLayout(textLayerPanelLayout);
		textLayerPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(textLayerPanel, new Arrangement(0, 0, 1.0, 0.0));

		textLayerLabel = new JLabel();
		textLayerPanel.add(textLayerLabel, new Arrangement(0, 0, 0.0, 1.0));

		textLayerOffsetXInput = new JTextField();
		textLayerPanel.add(textLayerOffsetXInput, new Arrangement(1, 0, 0.1, 1.0));

		textLayerOffsetYInput = new JTextField();
		textLayerPanel.add(textLayerOffsetYInput, new Arrangement(2, 0, 0.1, 1.0));

		textLayerFontNameInput = new JTextField();
		textLayerPanel.add(textLayerFontNameInput, new Arrangement(3, 0, 0.2, 1.0));

		textLayerFontSizeInput = new JTextField();
		textLayerPanel.add(textLayerFontSizeInput, new Arrangement(4, 0, 0.05, 1.0));

		textLayerColorInput = new JTextField();
		textLayerPanel.add(textLayerColorInput, new Arrangement(5, 0, 0.2, 1.0));

		textLayerTextInput = new JTextField();
		textLayerPanel.add(textLayerTextInput, new Arrangement(6, 0, 1.0, 1.0));

		JButton textLayerApplyBtn = new JButton("Apply");
		textLayerApplyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayerBasedOnText txtLayer = getCurrentTextLayer();
				txtLayer.setOffsetX(StrUtils.strToInt(textLayerOffsetXInput.getText(), 0));
				txtLayer.setOffsetY(StrUtils.strToInt(textLayerOffsetYInput.getText(), 0));
				txtLayer.setFontName(textLayerFontNameInput.getText());
				txtLayer.setFontSize(StrUtils.strToInt(textLayerFontSizeInput.getText(), 10));
				txtLayer.setTextColor(ColorRGBA.fromString(textLayerColorInput.getText()));
				txtLayer.setText(textLayerTextInput.getText());
				refreshMainView();
			}
		});
		textLayerPanel.add(textLayerApplyBtn, new Arrangement(7, 0, 0.0, 1.0));


		imgLayerPanel = new JPanel();
		imgLayerPanel.setPreferredSize(new Dimension(100, 20));
		GridBagLayout imgLayerPanelLayout = new GridBagLayout();
		imgLayerPanel.setLayout(imgLayerPanelLayout);
		imgLayerPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(imgLayerPanel, new Arrangement(0, 0, 1.0, 0.0));

		imgLayerLabel = new JLabel();
		imgLayerPanel.add(imgLayerLabel, new Arrangement(0, 0, 0.0, 1.0));

		imgLayerOffsetXInput = new JTextField();
		imgLayerPanel.add(imgLayerOffsetXInput, new Arrangement(1, 0, 0.1, 1.0));

		imgLayerOffsetYInput = new JTextField();
		imgLayerPanel.add(imgLayerOffsetYInput, new Arrangement(2, 0, 0.1, 1.0));

		JButton imgLayerApplyBtn = new JButton("Apply");
		imgLayerApplyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayerBasedOnImage imgLayer = getCurrentImageLayer();
				imgLayer.setOffsetX(StrUtils.strToInt(imgLayerOffsetXInput.getText(), 0));
				imgLayer.setOffsetY(StrUtils.strToInt(imgLayerOffsetYInput.getText(), 0));
				refreshMainView();
			}
		});
		imgLayerPanel.add(imgLayerApplyBtn, new Arrangement(3, 0, 0.0, 1.0));


		JPanel mainLowerPanel = new JPanel();
		mainLowerPanel.setPreferredSize(new Dimension(800, 500));
		GridBagLayout mainLowerPanelLayout = new GridBagLayout();
		mainLowerPanel.setLayout(mainLowerPanelLayout);
		mainLowerPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(mainLowerPanel, new Arrangement(0, 1, 1.0, 1.0));

		mainLowerPanelLeft = new JPanel();
		GridBagLayout mainLowerPanelLeftLayout = new GridBagLayout();
		mainLowerPanelLeft.setLayout(mainLowerPanelLeftLayout);
		mainLowerPanelLeft.setBorder(BorderFactory.createEmptyBorder());
		mainLowerPanelLeft.setBackground(windowBackgroundColor.toColor());
		mainLowerPanelLeft.setOpaque(true);
		mainLowerPanel.add(mainLowerPanelLeft, new Arrangement(0, 0, 0.0, 1.0));

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

				// System.out.println("LeftViewer x: " + x + " y: " + y);

				if ((x >= 52) && (x < 72) && (y >= 8) && (y < 28)) {
					switchForeAndBackColor();
				}

				if ((x >= 0) && (x < 80) && (y >= 40) && (y < 168)) {
					if (e.getModifiersEx() == 0) {
						setForegroundColor(colorpickerImg.getPixelSafely(x, y - 40));
					} else {
						setBackgroundColor(colorpickerImg.getPixelSafely(x, y - 40));
					}
				}
			}
		});

		mainPanelLeft = new JPanel();
		mainPanelLeft.add(mainPanelLeftViewerLabel);
		mainPanelLeft.setPreferredSize(new Dimension(80, 1));
		mainPanelLeft.setBorder(BorderFactory.createEmptyBorder());
		mainLowerPanelLeft.add(mainPanelLeft, new Arrangement(0, 0, 1.0, 1.0));

		curPosXLabel = new JLabel();
		mainLowerPanelLeft.add(curPosXLabel, new Arrangement(0, 1, 1.0, 0.0));
		curPosYLabel = new JLabel();
		mainLowerPanelLeft.add(curPosYLabel, new Arrangement(0, 2, 1.0, 0.0));
		curPosWLabel = new JLabel();
		mainLowerPanelLeft.add(curPosWLabel, new Arrangement(0, 3, 1.0, 0.0));
		curPosHLabel = new JLabel();
		mainLowerPanelLeft.add(curPosHLabel, new Arrangement(0, 4, 1.0, 0.0));

		imageViewer = new ImageIcon();
		imageViewerLabel = new JLabel(imageViewer);
		imageViewerLabel.setBackground(Color.gray);
		imageViewerLabel.setOpaque(true);

		imageViewerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// request focus so that key events are forwarded there for moving layers around
				// menu.requestFocusInWindow();

				int x = getX(e);
				int y = getY(e);

				// System.out.println("ImageViewer x: " + x + ", y: " + y);

				prevClickX = lastClickX;
				prevClickY = lastClickY;
				lastClickX = x;
				lastClickY = y;

				if (activeTool != null) {
					switch (activeTool) {

						case PIPETTE_FG:
						case PIPETTE_BG:
							Image curImg = picture.bake();
							ColorRGBA newColor = curImg.getPixelSafely(x, y);

							if (pipetteSize > 1) {
								List<ColorRGBA> pixMix = new ArrayList<>();
								pixMix.add(newColor);
								pixMix.add(curImg.getPixelSafely(x+1, y));
								pixMix.add(curImg.getPixelSafely(x-1, y));
								pixMix.add(curImg.getPixelSafely(x, y+1));
								pixMix.add(curImg.getPixelSafely(x, y-1));
								if (pipetteSize > 5) {
									pixMix.add(curImg.getPixelSafely(x+2, y));
									pixMix.add(curImg.getPixelSafely(x-2, y));
									pixMix.add(curImg.getPixelSafely(x, y+2));
									pixMix.add(curImg.getPixelSafely(x, y-2));
									pixMix.add(curImg.getPixelSafely(x+1, y+1));
									pixMix.add(curImg.getPixelSafely(x+1, y-1));
									pixMix.add(curImg.getPixelSafely(x-1, y+1));
									pixMix.add(curImg.getPixelSafely(x-1, y-1));
								}
								newColor = ColorRGBA.mixPix(pixMix);
							}

							if (newColor != null) {
								if (activeTool == Tool.PIPETTE_FG) {
									setForegroundColor(newColor);
								} else {
									setBackgroundColor(newColor);
								}
							}
							break;

						case DRAW_RECTANGLE_FG:
						case DRAW_RECTANGLE_BG:
							Pair<Integer, Integer> newPoint = new Pair<>(x, y);
							if (lastDrawPoints.size() > 0) {
								Pair<Integer, Integer> prevPoint = lastDrawPoints.get(0);
								lastDrawPoints = new ArrayList<>();
								lastDrawPoints.add(prevPoint);
								lastDrawPoints.add(newPoint);
								ColorRGBA drawColor = foregroundColor;
								if (activeTool == Tool.DRAW_RECTANGLE_BG) {
									drawColor = backgroundColor;
								}
								int x1 = lastDrawPoints.get(0).getX();
								int x2 = lastDrawPoints.get(1).getX();
								int y1 = lastDrawPoints.get(0).getY();
								int y2 = lastDrawPoints.get(1).getY();
								if (x2 < x1) {
									x2 = lastDrawPoints.get(0).getX();
									x1 = lastDrawPoints.get(1).getX();
								}
								if (y2 < y1) {
									y2 = lastDrawPoints.get(0).getY();
									y1 = lastDrawPoints.get(1).getY();
								}
								Image drawImg = pictureBeforePointDrawing.copy();
								drawImg.drawRectangle(x1, y1, x2, y2, drawColor);
								getCurrentImageLayer().setImage(drawImg);
								setPictureUndoTakenCareOf(picture);
							} else {
								lastDrawPoints.add(newPoint);
							}
							break;

						case DRAW_AREA_FG:
						case DRAW_AREA_BG:
							newPoint = new Pair<>(x, y);
							lastDrawPoints.add(newPoint);
							if (lastDrawPoints.size() > 2) {
								ColorRGBA drawColor = foregroundColor;
								if (activeTool == Tool.DRAW_AREA_BG) {
									drawColor = backgroundColor;
								}
								Image drawImg = pictureBeforePointDrawing.copy();
								drawImg.drawArea(lastDrawPoints, drawColor);
								getCurrentImageLayer().setImage(drawImg);
								setPictureUndoTakenCareOf(picture);
							}
							break;
					}
				}

				mouseMoved(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int x = getX(e);
				int y = getY(e);

				curPosXLabel.setText(" X: " + x);
				curPosYLabel.setText(" Y: " + y);
				curPosWLabel.setText(" W: " + picture.getWidth());
				curPosHLabel.setText(" H: " + picture.getHeight());
				mainPanelLeft.repaint();
			}

			private int getX(MouseEvent e) {
				int lw = imageViewerLabel.getWidth();
				int iw = imageViewer.getIconWidth();
				int offsetX = 0;
				if (lw > iw) {
					offsetX = (lw - iw) / 2;
				}
				return e.getX() - offsetX;
			}

			private int getY(MouseEvent e) {
				int lh = imageViewerLabel.getHeight();
				int ih = imageViewer.getIconHeight();
				int offsetY = 0;
				if (lh > ih) {
					offsetY = (lh - ih) / 2;
				}
				return e.getY() - offsetY;
			}
		});

		mainPanelRight = new JScrollPane(imageViewerLabel);
		mainPanelRight.setBorder(BorderFactory.createEmptyBorder());
		mainLowerPanel.add(mainPanelRight, new Arrangement(1, 0, 1.0, 1.0));

		layerList = new JList<String>(getLayerCaptionArray());
		layerList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentLayerIndex = picture.getLayerAmount() - layerList.locationToIndex(e.getPoint()) - 1;
				refreshLayerView();
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		mainLowerPanel.add(layerList, new Arrangement(2, 0, 0.0, 1.0));

		parent.add(mainPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private void refreshTitleBarAndSaveExportItems() {

		String lastSavePathStr = lastSavePath;
		if (lastSavePathStr == null) {
			saveAgain.setEnabled(false);
			saveAgain.setText("Save Again");
			lastSavePathStr = "(unsaved)";
		} else {
			saveAgain.setEnabled(true);
			saveAgain.setText("Save Again as " + lastSavePathStr);
		}
		String lastExportPathStr = lastExportPath;
		if (lastExportPathStr == null) {
			exportAgain.setEnabled(false);
			exportAgain.setText("Export Again");
			lastExportPathStr = "(unexported)";
		} else {
			exportAgain.setEnabled(true);
			exportAgain.setText("Export Again as " + lastExportPathStr);
		}

		String unsavedStr = "";
		if (!savedSinceLastChange) {
			unsavedStr += "UNSAVED - ";
		}

		mainFrame.setTitle(Main.PROGRAM_TITLE + " - " + unsavedStr + "Pic: " + lastSavePathStr + " Exp: " + lastExportPathStr);
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
		Image origPic = picture.bake();

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
		exportImageToFile(origPic, tempFile);
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

	public Image getPictureBaked() {
		return picture.bake();
	}

	public void setPicture(Image newImage) {

		setPicture(new ImageMultiLayered(newImage));
	}

	public void setPicture(ImageMultiLayered newPicture) {

		saveCurPicForUndo();

		setPictureUndoTakenCareOf(newPicture);

		refreshLayerView();
	}

	private void setPictureUndoTakenCareOf(ImageMultiLayered newPicture) {

		picture = newPicture;

		refreshMainView();

		savedSinceLastChange = false;
		refreshTitleBarAndSaveExportItems();
	}

	private Image openFile(boolean returnImage) {

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

				return openImageFile(new File(augFilePicker.getSelectedFile()), returnImage);

			case JFileChooser.CANCEL_OPTION:
				// cancel was pressed... do nothing for now
				break;
		}

		return null;
	}

	private Image openImageFile(File imageFile, boolean returnImage) {

		String selFilename = imageFile.getCanonicalFilename();

		saveCurPicForUndo();
		if (selFilename.toLowerCase().endsWith(".pic")) {
			lastSavePath = selFilename;
			PicFile picFile = new PicFile(selFilename);
			picture = picFile.getImageMultiLayered();
			if (returnImage) {
				return picture.bake();
			}
		} else {
			lastExportPath = selFilename;
			Image img = imageFileCtrl.loadImageFromFile(imageFile);
			if (returnImage) {
				return img;
			}
			picture = new ImageMultiLayered(img);
		}
		refreshMainView();
		refreshTitleBarAndSaveExportItems();
		refreshLayerView();

		return null;
	}

	private void saveOrExportFile(boolean exporting) {

		JFileChooser augFilePicker;

		// if we find nothing better, use the last-used directory
		String lastDirectory = configuration.getValue(CONFIG_KEY_LAST_DIRECTORY);

		if ((lastDirectory != null) && !"".equals(lastDirectory)) {
			augFilePicker = new JFileChooser(new java.io.File(lastDirectory));
		} else {
			augFilePicker = new JFileChooser();
		}

		if (exporting) {
			augFilePicker.setDialogTitle("Export as Image File");
		} else {
			augFilePicker.setDialogTitle("Save as Image File");
		}
		augFilePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
		augFilePicker.setMultiSelectionEnabled(false);

		addSaveOrExportFileFilters(augFilePicker, exporting);

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

				if (exporting) {
					exportImageToFile(picture.bake(), selectedFile);
				} else {
					saveImageToFile(picture, selectedFile);
				}

				break;

			case JFileChooser.CANCEL_OPTION:
				// cancel was pressed... do nothing for now
				break;
		}
	}

	private void saveFileAgain() {
		saveImageToFile(picture, new File(lastSavePath));
	}

	private void saveImageToFile(ImageMultiLayered picture, File selectedFile) {
		if (!selectedFile.getFilename().toLowerCase().endsWith(".pic")) {
			selectedFile = new File(selectedFile.getFilename() + ".pic");
		}
		PicFile picFile = new PicFile(selectedFile);
		picFile.assign(picture);
		picFile.save();
		lastSavePath = selectedFile.getCanonicalFilename();
		savedSinceLastChange = true;
		refreshTitleBarAndSaveExportItems();
	}

	private void exportFileAgain() {
		exportImageToFile(picture.bake(), new File(lastExportPath));
	}

	private void exportImageToFile(Image picture, File selectedFile) {
		imageFileCtrl.saveImageToFile(picture, selectedFile);
		lastExportPath = selectedFile.getCanonicalFilename();
		refreshTitleBarAndSaveExportItems();
	}

	private void addOpenFileFilters(JFileChooser fileChooser) {
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Picturizer Picture (*.pic)", "pic"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG files (*.jpg, *.jpeg)", "jpg", "jpeg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap files (*.bmp)", "bmp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", "png"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Bitmap (*.pbm)", "pbm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Gray Map (*.pgm)", "pgm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Pixel Map (*.ppm)", "ppm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Document Format (*.pdf)", "pdf"));
	}

	private void addSaveOrExportFileFilters(JFileChooser fileChooser, boolean exporting) {
		if (exporting) {
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG files (*.jpg, *.jpeg)", "jpg", "jpeg"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap files (*.bmp)", "bmp"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", "png"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Bitmap (*.pbm)", "pbm"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Gray Map (*.pgm)", "pgm"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Portable Pixel Map (*.ppm)", "ppm"));
		} else {
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Picturizer Picture (*.pic)", "pic"));
		}
	}

	private void refreshMainView() {
		imageViewer.setImage(picture.bake().getAwtImage());
		// imageViewerLabel.repaint();
		mainPanelRight.revalidate();
		mainPanelRight.repaint();
	}

	private void saveCurPicForUndo() {
		undoablePicture3 = undoablePicture2;
		undoablePicture2 = undoablePicture1;
		undoablePicture1 = undoablePicture;
		if (picture == null) {
			undoablePicture = null;
		} else {
			undoablePicture = picture.copy();
		}
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
		mainLowerPanelLeft.setBackground(newBgColor);
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

	private void refreshTools() {
		if (activeTool != null) {
			switch (activeTool) {
				case DRAW_RECTANGLE_FG:
				case DRAW_RECTANGLE_BG:
				case DRAW_AREA_FG:
				case DRAW_AREA_BG:
					this.lastDrawPoints = new ArrayList<>();
					pictureBeforePointDrawing = getCurrentImageLayer().getImage().copy();
					break;
			}
		}

		adjustToolTitle(setForegroundToPipette, TOOL_TEXTS_PIPETTE_FG, (activeTool == Tool.PIPETTE_FG) && (pipetteSize == 1));
		adjustToolTitle(setForegroundToPipette4, TOOL_TEXTS_PIPETTE_FG + " + 4 Pixels Around", (activeTool == Tool.PIPETTE_FG) && (pipetteSize == 5));
		adjustToolTitle(setForegroundToPipette12, TOOL_TEXTS_PIPETTE_FG + " + 12 Pixels Around", (activeTool == Tool.PIPETTE_FG) && (pipetteSize == 13));
		adjustToolTitle(setBackgroundToPipette, TOOL_TEXTS_PIPETTE_BG, activeTool == Tool.PIPETTE_BG);
		adjustToolTitle(drawRectangleFG, TOOL_TEXTS_RECTANGLE_FG, activeTool == Tool.DRAW_RECTANGLE_FG);
		adjustToolTitle(drawRectangleBG, TOOL_TEXTS_RECTANGLE_BG, activeTool == Tool.DRAW_RECTANGLE_BG);
		adjustToolTitle(drawAreaFG, TOOL_TEXTS_AREA_FG, activeTool == Tool.DRAW_AREA_FG);
		adjustToolTitle(drawAreaBG, TOOL_TEXTS_AREA_BG, activeTool == Tool.DRAW_AREA_BG);
	}

	private void adjustToolTitle(JMenuItem item, String itemText, boolean isActive) {
		if (isActive) {
			item.setText(TOOL_SELECTED_START_STR + itemText + TOOL_SELECTED_END_STR);
		} else {
			item.setText(itemText);
		}
	}

	private void showGridGUI(boolean createNew) {

		if (createGridGUI == null) {
			createGridGUI = new CreateGridGUI(GUI.this);
		}

		createGridGUI.show(createNew, foregroundColor, backgroundColor, picture.bake());
	}

	private void showExpandShrinkGUI() {

		if (expandShrinkGUI == null) {
			expandShrinkGUI = new ExpandShrinkGUI(GUI.this);
		}

		expandShrinkGUI.show(foregroundColor, backgroundColor, picture.bake());
	}

	private void showResizeGUI(boolean resample) {

		if (resizeGUI == null) {
			resizeGUI = new ResizeGUI(GUI.this);
		}

		resizeGUI.show(picture.bake(), resample);
	}

	private ImageLayer getCurrentLayer() {
		ImageLayer layer = picture.getLayer(currentLayerIndex);
		if (layer != null) {
			return layer;
		}
		return new ImageLayerBasedOnText(0, 0, "", "", 1, foregroundColor);
	}

	private ImageLayerBasedOnImage getCurrentImageLayer() {
		ImageLayer layer = picture.getLayer(currentLayerIndex);
		if (layer != null) {
			if (layer instanceof ImageLayerBasedOnImage) {
				ImageLayerBasedOnImage imgLayer = (ImageLayerBasedOnImage) layer;
				return imgLayer;
			}
		}

		GuiUtils.complain("The currently selected layer is not an image layer!");
		// return a layer not attached to anything to that requests to this are just ignored - as the complaining is already done...
		Image tempImg = new Image(picture.getWidth(), picture.getHeight());
		return new ImageLayerBasedOnImage(0, 0, tempImg);
	}

	private ImageLayerBasedOnText getCurrentTextLayer() {
		ImageLayer layer = picture.getLayer(currentLayerIndex);
		if (layer != null) {
			if (layer instanceof ImageLayerBasedOnText) {
				ImageLayerBasedOnText txtLayer = (ImageLayerBasedOnText) layer;
				return txtLayer;
			}
		}

		GuiUtils.complain("The currently selected layer is not a text layer!");
		// return a layer not attached to anything to that requests to this are just ignored - as the complaining is already done...
		return new ImageLayerBasedOnText(0, 0, "", "", 1, foregroundColor);
	}

	private String[] getLayerCaptionArray() {
		if (picture == null) {
			return new String[0];
		}
		int amount = picture.getLayerAmount();
		String[] result = new String[amount];
		for (int i = 0; i < amount; i++) {
			ImageLayer layer = picture.getLayer(i);
			if (layer instanceof ImageLayerBasedOnText) {
				String captionStr = ((ImageLayerBasedOnText) layer).getText();
				if (captionStr.length() > 9) {
					captionStr = captionStr.substring(0, 9) + "...";
				}
				result[amount - i - 1] = "TXT#" + i + ": " + captionStr;
			} else {
				result[amount - i - 1] = "IMG#" + i;
			}
		}
		return result;
	}

	private void refreshLayerView() {
		if (currentLayerIndex >= picture.getLayerAmount()) {
			currentLayerIndex = picture.getLayerAmount() - 1;
		}
		if (currentLayerIndex < 0) {
			currentLayerIndex = 0;
		}
		layerList.setListData(getLayerCaptionArray());
		if (picture.getLayerAmount() > 0) {
			layerList.setSelectedIndex(picture.getLayerAmount() - currentLayerIndex - 1);
		}
		textLayerPanel.setVisible(false);
		imgLayerPanel.setVisible(false);
		ImageLayer layer = picture.getLayer(currentLayerIndex);
		if (layer != null) {
			if (layer instanceof ImageLayerBasedOnText) {
				ImageLayerBasedOnText txtLayer = (ImageLayerBasedOnText) layer;
				textLayerLabel.setText("TXT#" + currentLayerIndex);
				textLayerOffsetXInput.setText(""+txtLayer.getOffsetX());
				textLayerOffsetYInput.setText(""+txtLayer.getOffsetY());
				textLayerFontNameInput.setText(txtLayer.getFontName());
				textLayerFontSizeInput.setText(""+txtLayer.getFontSize());
				textLayerColorInput.setText(txtLayer.getTextColor().toString());
				textLayerTextInput.setText(txtLayer.getText());
				textLayerPanel.setVisible(true);
			}
			if (layer instanceof ImageLayerBasedOnImage) {
				ImageLayerBasedOnImage imgLayer = (ImageLayerBasedOnImage) layer;
				imgLayerLabel.setText("IMG#" + currentLayerIndex);
				imgLayerOffsetXInput.setText(""+imgLayer.getOffsetX());
				imgLayerOffsetYInput.setText(""+imgLayer.getOffsetY());
				imgLayerPanel.setVisible(true);
			}
		}
	}
}
