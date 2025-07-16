/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui;

import com.asofterspace.picturizer.gui.menu.GUIMenuChannels;
import com.asofterspace.picturizer.gui.menu.GUIMenuColors;
import com.asofterspace.picturizer.gui.menu.GUIMenuDampen;
import com.asofterspace.picturizer.gui.menu.GUIMenuDarkenBrighten;
import com.asofterspace.picturizer.gui.menu.GUIMenuDraw;
import com.asofterspace.picturizer.gui.menu.GUIMenuEdit;
import com.asofterspace.picturizer.gui.menu.GUIMenuFile;
import com.asofterspace.picturizer.gui.menu.GUIMenuGlitch;
import com.asofterspace.picturizer.gui.menu.GUIMenuGrid;
import com.asofterspace.picturizer.gui.menu.GUIMenuHelp;
import com.asofterspace.picturizer.gui.menu.GUIMenuIntensify;
import com.asofterspace.picturizer.gui.menu.GUIMenuInvert;
import com.asofterspace.picturizer.gui.menu.GUIMenuLayers;
import com.asofterspace.picturizer.gui.menu.GUIMenuMixing;
import com.asofterspace.picturizer.gui.menu.GUIMenuNew;
import com.asofterspace.picturizer.gui.menu.GUIMenuPixels;
import com.asofterspace.picturizer.gui.menu.GUIMenuTools;
import com.asofterspace.picturizer.gui.menu.GUIMenuWindow;
import com.asofterspace.picturizer.Picturizer;
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

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class GUI extends MainWindow {

	private final static String CONFIG_KEY_WIDTH = "mainFrameWidth";
	private final static String CONFIG_KEY_HEIGHT = "mainFrameHeight";
	private final static String CONFIG_KEY_LEFT = "mainFrameLeft";
	private final static String CONFIG_KEY_TOP = "mainFrameTop";
	public final static String CONFIG_KEY_LAST_DIRECTORY = "lastDirectory";

	private final static int MAIN_VIEW_OFFSET = 8;
	private final static ColorRGBA LINE_COLOR_SURROUND = ColorRGBA.BLACK;
	private final static ColorRGBA LINE_COLOR_LAYER = new ColorRGBA(150, 0, 200, 255);
	private final static ColorRGBA LINE_COLOR_CLICKRECT = new ColorRGBA(200, 0, 120, 255);

	private String lastSavePath;
	private String lastExportPath;
	private File lastOpenedFile;

	private JPanel mainPanel;
	private JPanel mainPanelLeft;
	private JPanel mainPanelLeftButtons;
	private JPanel mainPanelLeftColorPicker;
	private JList<String> layerList;
	private JScrollPane mainPanelRight;
	private JPanel imgLayerPanel;
	private JLabel imgLayerLabel;
	private JTextField imgLayerOffsetXInput;
	private JTextField imgLayerOffsetYInput;
	private JTextField imgLayerTextInput;
	private JPanel textLayerPanel;
	private JLabel textLayerLabel;
	private JTextField textLayerOffsetXInput;
	private JTextField textLayerOffsetYInput;
	private JTextField textLayerFontNameInput;
	private JTextField textLayerFontSizeInput;
	private JTextField textLayerColorInput;
	private JTextField textLayerTextInput;
	private JLabel curPosXLabel;
	private JLabel curPosYLabel;
	private JLabel curPosWLabel;
	private JLabel curPosHLabel;

	private GUIMenuFile guiMenuFile;
	private GUIMenuNew guiMenuNew;
	private GUIMenuEdit guiMenuEdit;
	private GUIMenuLayers guiMenuLayers;
	private GUIMenuTools guiMenuTools;
	private GUIMenuGrid guiMenuGrid;
	private GUIMenuColors guiMenuColors;
	private GUIMenuDraw guiMenuDraw;
	private GUIMenuPixels guiMenuPixels;
	private GUIMenuChannels guiMenuChannels;
	private GUIMenuInvert guiMenuInvert;
	private GUIMenuDampen guiMenuDampen;
	private GUIMenuDarkenBrighten guiMenuDarkenBrighten;
	private GUIMenuIntensify guiMenuIntensify;
	private GUIMenuGlitch guiMenuGlitch;
	private GUIMenuMixing guiMenuMixing;
	private GUIMenuWindow guiMenuWindow;
	private GUIMenuHelp guiMenuHelp;

	private GUIButtonHolder guiButtonHolder;

	private ConfigFile configuration;
	private String fileToOpenAfterStartup;

	private Image mainPanelLeftImg;
	private ImageIcon mainPanelLeftViewer;
	private JLabel mainPanelLeftViewerLabel;

	private ImageIcon imageViewer;
	private JLabel imageViewerLabel;
	private ImageMultiLayered picture;
	private ImageMultiLayered undoablePicture5;
	private ImageMultiLayered undoablePicture4;
	private ImageMultiLayered undoablePicture3;
	private ImageMultiLayered undoablePicture2;
	private ImageMultiLayered undoablePicture1;
	private ImageMultiLayered undoablePicture;
	private ImageMultiLayered redoablePicture;

	private ImageFileCtrl imageFileCtrl;
	private Image colorpickerImg;

	private Tool activeTool = null;
	private ColorRGBA foregroundColor = ColorRGBA.BLACK;
	private ColorRGBA backgroundColor = ColorRGBA.WHITE;
	private ColorRGBA windowBackgroundColor = new ColorRGBA(Color.gray);

	private int currentLayerIndex = 0;
	private int activeToolSize = 1;
	private List<Pair<Integer, Integer>> lastDrawPoints = new ArrayList<>();
	private Image pictureBeforePointDrawing;
	private Image drawPicture;
	private int prevClickX = 0;
	private int prevClickY = 0;
	private int lastClickX = 0;
	private int lastClickY = 0;
	private double zoomFactor = 1.0;

	private boolean savedSinceLastChange = true;


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
		Integer lastLeftRead = configuration.getInteger(CONFIG_KEY_LEFT, -1);
		if (lastLeftRead < 0) {
			lastLeftRead = 0;
		}
		final Integer lastLeft = lastLeftRead;
		Integer lastTopRead = configuration.getInteger(CONFIG_KEY_TOP, -1);
		if (lastTopRead < 0) {
			lastTopRead = 0;
		}
		final Integer lastTop = lastTopRead;

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
					File fileToOpenNow = new File(fileToOpenAfterStartup);
					Directory containingDir = fileToOpenNow.getParentDirectory();
					// for now, store this configuration in memory but do not necessary save if we open picturizer more as image viewer for a second
					configuration.set(CONFIG_KEY_LAST_DIRECTORY, containingDir.getAbsoluteDirname());
					openImageFile(fileToOpenNow, returnImage);
				}

				setPosLabelTexts(0, 0);
			}
		});

		guiMenuNew.createNewEmptyFile(this);
	}

	private JMenuBar createMenu(JFrame parent) {

		JMenuBar menu = new JMenuBar();

		guiMenuFile = new GUIMenuFile();
		menu.add(guiMenuFile.createMenu(this));
		guiMenuNew = new GUIMenuNew();
		menu.add(guiMenuNew.createMenu(this));
		guiMenuEdit = new GUIMenuEdit();
		menu.add(guiMenuEdit.createMenu(this));
		guiMenuLayers = new GUIMenuLayers();
		menu.add(guiMenuLayers.createMenu(this));
		guiMenuTools = new GUIMenuTools();
		menu.add(guiMenuTools.createMenu(this));
		guiMenuGrid = new GUIMenuGrid();
		menu.add(guiMenuGrid.createMenu(this));
		guiMenuColors = new GUIMenuColors();
		menu.add(guiMenuColors.createMenu(this));
		guiMenuDraw = new GUIMenuDraw();
		menu.add(guiMenuDraw.createMenu(this));
		guiMenuPixels = new GUIMenuPixels();
		menu.add(guiMenuPixels.createMenu(this));
		guiMenuChannels = new GUIMenuChannels();
		menu.add(guiMenuChannels.createMenu(this));
		guiMenuInvert = new GUIMenuInvert();
		menu.add(guiMenuInvert.createMenu(this));
		guiMenuDampen = new GUIMenuDampen();
		menu.add(guiMenuDampen.createMenu(this));
		guiMenuDarkenBrighten = new GUIMenuDarkenBrighten();
		menu.add(guiMenuDarkenBrighten.createMenu(this));
		guiMenuIntensify = new GUIMenuIntensify();
		menu.add(guiMenuIntensify.createMenu(this));
		guiMenuGlitch = new GUIMenuGlitch();
		menu.add(guiMenuGlitch.createMenu(this));
		guiMenuMixing = new GUIMenuMixing();
		menu.add(guiMenuMixing.createMenu(this));
		guiMenuWindow = new GUIMenuWindow();
		menu.add(guiMenuWindow.createMenu(this));
		guiMenuHelp = new GUIMenuHelp();
		menu.add(guiMenuHelp.createMenu(this));

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
	void refreshLeftView() {

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

		mainPanelLeftColorPicker.revalidate();
		mainPanelLeftColorPicker.repaint();
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
				refreshLayerView();
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

		imgLayerTextInput = new JTextField();
		imgLayerPanel.add(imgLayerTextInput, new Arrangement(3, 0, 0.2, 1.0));

		JButton imgLayerApplyBtn = new JButton("Apply");
		imgLayerApplyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageLayerBasedOnImage imgLayer = getCurrentImageLayer();
				imgLayer.setOffsetX(StrUtils.strToInt(imgLayerOffsetXInput.getText(), 0));
				imgLayer.setOffsetY(StrUtils.strToInt(imgLayerOffsetYInput.getText(), 0));
				imgLayer.setCaption(imgLayerTextInput.getText());
				refreshMainView();
				refreshLayerView();
			}
		});
		imgLayerPanel.add(imgLayerApplyBtn, new Arrangement(4, 0, 0.0, 1.0));


		JPanel mainLowerPanel = new JPanel();
		mainLowerPanel.setPreferredSize(new Dimension(800, 500));
		GridBagLayout mainLowerPanelLayout = new GridBagLayout();
		mainLowerPanel.setLayout(mainLowerPanelLayout);
		mainLowerPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(mainLowerPanel, new Arrangement(0, 1, 1.0, 1.0));

		mainPanelLeft = new JPanel();
		GridBagLayout mainPanelLeftLayout = new GridBagLayout();
		mainPanelLeft.setLayout(mainPanelLeftLayout);
		mainPanelLeft.setBorder(BorderFactory.createEmptyBorder());
		mainPanelLeft.setBackground(windowBackgroundColor.toColor());
		mainPanelLeft.setOpaque(true);
		mainLowerPanel.add(mainPanelLeft, new Arrangement(0, 0, 0.0, 1.0));

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

		mainPanelLeftColorPicker = new JPanel();
		mainPanelLeftColorPicker.add(mainPanelLeftViewerLabel);
		mainPanelLeftColorPicker.setPreferredSize(new Dimension(80, 164));
		mainPanelLeftColorPicker.setBorder(BorderFactory.createEmptyBorder());
		mainPanelLeftColorPicker.setBackground(windowBackgroundColor.toColor());
		mainPanelLeft.add(mainPanelLeftColorPicker, new Arrangement(0, 0, 1.0, 0.0));

		mainPanelLeftButtons = new JPanel();
		mainPanelLeftButtons.setBackground(windowBackgroundColor.toColor());
		guiButtonHolder = new GUIButtonHolder();
		guiButtonHolder.createButtons(this, mainPanelLeftButtons);
		mainPanelLeft.add(mainPanelLeftButtons, new Arrangement(0, 1, 1.0, 0.0));

		JLabel spaceHolderLabel = new JLabel();
		mainPanelLeft.add(spaceHolderLabel, new Arrangement(0, 2, 1.0, 1.0));

		curPosXLabel = new JLabel();
		mainPanelLeft.add(curPosXLabel, new Arrangement(0, 3, 1.0, 0.0));
		curPosYLabel = new JLabel();
		mainPanelLeft.add(curPosYLabel, new Arrangement(0, 4, 1.0, 0.0));
		curPosWLabel = new JLabel();
		mainPanelLeft.add(curPosWLabel, new Arrangement(0, 5, 1.0, 0.0));
		curPosHLabel = new JLabel();
		mainPanelLeft.add(curPosHLabel, new Arrangement(0, 6, 1.0, 0.0));

		imageViewer = new ImageIcon();
		imageViewerLabel = new JLabel(imageViewer);
		imageViewerLabel.setOpaque(true);
		imageViewerLabel.setBackground(windowBackgroundColor.toColor());

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

				refreshMainView();

				if (activeTool != null) {
					switch (activeTool) {

						case PIPETTE_FG:
						case PIPETTE_BG:
							Image curImg = picture.bake();
							ColorRGBA newColor = curImg.getPixelSafely(x, y);

							if (activeToolSize > 1) {
								List<ColorRGBA> pixMix = new ArrayList<>();
								pixMix.add(newColor);
								pixMix.add(curImg.getPixelSafely(x+1, y));
								pixMix.add(curImg.getPixelSafely(x-1, y));
								pixMix.add(curImg.getPixelSafely(x, y+1));
								pixMix.add(curImg.getPixelSafely(x, y-1));
								if (activeToolSize > 5) {
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

						case DRAW_PEN_FG:
							drawPicture.drawPen(x, y, foregroundColor, activeToolSize);
							getCurrentImageLayer().setImage(drawPicture);
							setPictureUndoTakenCareOf(picture);
							break;

						case DRAW_LINES_FG:
							if (lastDrawPoints.size() > 0) {
								drawPicture.setLineWidth(activeToolSize);
								drawPicture.drawLine(x, y, lastDrawPoints.get(0).getX(), lastDrawPoints.get(0).getY(), foregroundColor);
								getCurrentImageLayer().setImage(drawPicture);
								setPictureUndoTakenCareOf(picture);
							}
							Pair<Integer, Integer> newPoint = new Pair<>(x, y);
							lastDrawPoints = new ArrayList<>();
							lastDrawPoints.add(newPoint);
							break;

						case FILL_FG:
							pictureBeforePointDrawing.fillConnectedArea(x, y, foregroundColor);
							setPicture(pictureBeforePointDrawing);
							break;

						case FILL_ROUGHLY_FG:
							pictureBeforePointDrawing.fillConnectedArea(x, y, foregroundColor, 64);
							setPicture(pictureBeforePointDrawing);
							break;

						case DRAW_RECTANGLE_FG:
						case DRAW_RECTANGLE_BG:
							newPoint = new Pair<>(x, y);
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
						case DRAW_QUADS_FG:
						case DRAW_QUADS_BG:
							newPoint = new Pair<>(x, y);
							lastDrawPoints.add(newPoint);
							if (lastDrawPoints.size() > 2) {
								ColorRGBA drawColor = foregroundColor;
								if ((activeTool == Tool.DRAW_AREA_BG) || (activeTool == Tool.DRAW_QUADS_BG)) {
									drawColor = backgroundColor;
								}
								Image drawImg = pictureBeforePointDrawing.copy();
								drawImg.drawArea(lastDrawPoints, drawColor);
								getCurrentImageLayer().setImage(drawImg);
								setPictureUndoTakenCareOf(picture);

								if ((activeTool == Tool.DRAW_QUADS_FG) || (activeTool == Tool.DRAW_QUADS_BG)) {
									if (lastDrawPoints.size() > 3) {
										Tool curTool = activeTool;
										forceActiveTool(null);
										forceActiveTool(curTool);
									}
								}
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

				setPosLabelTexts(x, y);
			}

			private int getX(MouseEvent e) {
				int lw = imageViewerLabel.getWidth();
				int iw = imageViewer.getIconWidth();
				int offsetX = 0;
				if (lw > iw) {
					offsetX = (lw - iw) / 2;
				}
				int result = e.getX() - offsetX - MAIN_VIEW_OFFSET;
				return (int) Math.round(result / zoomFactor);
			}

			private int getY(MouseEvent e) {
				int lh = imageViewerLabel.getHeight();
				int ih = imageViewer.getIconHeight();
				int offsetY = 0;
				if (lh > ih) {
					offsetY = (lh - ih) / 2;
				}
				int result = e.getY() - offsetY - MAIN_VIEW_OFFSET;
				return (int) Math.round(result / zoomFactor);
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
				refreshMainView();
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

	void refreshTitleBarAndSaveExportItems() {

		JMenuItem saveAgain = guiMenuFile.getSaveAgain();
		JMenuItem exportAgain = guiMenuFile.getExportAgain();

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

		mainFrame.setTitle(Picturizer.PROGRAM_TITLE + " - " + unsavedStr + "Pic: " + lastSavePathStr + " Exp: " + lastExportPathStr);
	}

	public Image getPictureBaked() {
		return picture.bake();
	}

	public ImageMultiLayered getPicture() {
		return picture;
	}

	public void setPicture(Image newImage) {

		setPicture(new ImageMultiLayered(newImage));
	}

	public void setPicture(ImageMultiLayered newPicture) {

		saveCurPicForUndo();

		setPictureUndoTakenCareOf(newPicture);

		refreshLayerView();
	}

	public void setPictureUndoTakenCareOf(ImageMultiLayered newPicture) {

		picture = newPicture;

		refreshMainView();

		savedSinceLastChange = false;
		refreshTitleBarAndSaveExportItems();
	}

	public ImageMultiLayered getUndoablePicture() {
		return undoablePicture;
	}

	public Image openFile(boolean returnImage) {

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
		augFilePicker.setFileHidingEnabled(false);

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

		lastOpenedFile = imageFile;

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

	public void saveOrExportFile(boolean exporting) {

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

	public void saveFileAgain() {
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

	public void exportFileAgain() {
		exportImageToFile(picture.bake(), new File(lastExportPath));
	}

	public void exportImageToFile(Image picture, File selectedFile) {
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

	public void refreshMainView() {
		int offset = MAIN_VIEW_OFFSET;
		Image zoomedPicture = picture.bake();
		if ((zoomFactor < 0.95) || (zoomFactor > 1.05)) {
			zoomedPicture.resizeBy(zoomFactor, zoomFactor);
		}
		Image ivImg = new Image(zoomedPicture.getWidth() + offset + offset, zoomedPicture.getHeight() + offset + offset, ColorRGBA.TRANS);
		ivImg.draw(zoomedPicture, offset, offset);

		// dotted rectangle around current layer
		ImageLayer ly = getCurrentLayer();
		int left = offset - 1 + (int) Math.round(ly.getOffsetX() * zoomFactor);
		int top = offset - 1 + (int) Math.round(ly.getOffsetY() * zoomFactor);
		int right = (int) Math.round((ly.getOffsetX() + ly.getWidth()) * zoomFactor) + offset;
		int bottom = (int) Math.round((ly.getOffsetY() + ly.getHeight()) * zoomFactor) + offset;
		ivImg.drawDottedLine(left, top, right, top, LINE_COLOR_LAYER);
		ivImg.drawDottedLine(left, bottom, right, bottom, LINE_COLOR_LAYER);
		ivImg.drawDottedLine(left, top, left, bottom, LINE_COLOR_LAYER);
		ivImg.drawDottedLine(right, top, right, bottom, LINE_COLOR_LAYER);

		// dotted rectangle around picture overall
		left = offset - 1;
		top = offset - 1;
		right = zoomedPicture.getWidth() + offset;
		bottom = zoomedPicture.getHeight() + offset;
		ivImg.drawDottedLine(left, top, right, top, LINE_COLOR_SURROUND);
		ivImg.drawDottedLine(left, bottom, right, bottom, LINE_COLOR_SURROUND);
		ivImg.drawDottedLine(left, top, left, bottom, LINE_COLOR_SURROUND);
		ivImg.drawDottedLine(right, top, right, bottom, LINE_COLOR_SURROUND);

		if ((activeTool == null) && (prevClickX != lastClickX) && (prevClickY != lastClickY)) {
			// dotted rectangle around last clicked area
			if (prevClickX < lastClickX) {
				left = prevClickX;
				right = lastClickX + 1;
			} else {
				left = lastClickX;
				right = prevClickX + 1;
			}
			if (prevClickY < lastClickY) {
				top = prevClickY;
				bottom = lastClickY + 1;
			} else {
				top = lastClickY;
				bottom = prevClickY + 1;
			}
			left = (int) Math.round(left * zoomFactor) + offset - 1;
			top = (int) Math.round(top * zoomFactor) + offset - 1;
			right = (int) Math.round(right * zoomFactor) + offset;
			bottom = (int) Math.round(bottom * zoomFactor) + offset;
			ivImg.drawDottedLine(left, top, right, top, LINE_COLOR_CLICKRECT);
			ivImg.drawDottedLine(left, bottom, right, bottom, LINE_COLOR_CLICKRECT);
			ivImg.drawDottedLine(left, top, left, bottom, LINE_COLOR_CLICKRECT);
			ivImg.drawDottedLine(right, top, right, bottom, LINE_COLOR_CLICKRECT);
		}

		imageViewer.setImage(ivImg.getAwtImage());
		// imageViewerLabel.repaint();
		mainPanelRight.revalidate();
		mainPanelRight.repaint();
	}

	public void saveCurPicForUndo() {
		undoablePicture5 = undoablePicture4;
		undoablePicture4 = undoablePicture3;
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

	public void switchForeAndBackColor() {
		ColorRGBA prevBackgroundColor = backgroundColor;
		backgroundColor = foregroundColor;
		foregroundColor = prevBackgroundColor;
		refreshLeftView();
	}

	public ColorRGBA getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(ColorRGBA col) {
		foregroundColor = col;
		refreshLeftView();
	}

	public ColorRGBA getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(ColorRGBA col) {
		backgroundColor = col;
		refreshLeftView();
	}

	public void setWindowBackgroundColor(Color newBgColor) {
		windowBackgroundColor = new ColorRGBA(newBgColor);
		imageViewerLabel.setBackground(newBgColor);
		mainPanelLeftViewerLabel.setBackground(newBgColor);
		mainPanelLeft.setBackground(newBgColor);
		mainPanelLeftColorPicker.setBackground(newBgColor);
		mainPanelLeftButtons.setBackground(newBgColor);
		refreshLeftView();
	}

	public void undo() {
		if (undoablePicture == null) {
			return;
		}
		redoablePicture = picture;
		picture = undoablePicture;
		undoablePicture = undoablePicture1;
		undoablePicture1 = undoablePicture2;
		undoablePicture2 = undoablePicture3;
		undoablePicture3 = undoablePicture4;
		undoablePicture4 = undoablePicture5;
		refreshMainView();
		refreshLayerView();
	}

	public void redo() {
		if (redoablePicture == null) {
			return;
		}
		undoablePicture5 = undoablePicture4;
		undoablePicture4 = undoablePicture3;
		undoablePicture3 = undoablePicture2;
		undoablePicture2 = undoablePicture1;
		undoablePicture1 = undoablePicture;
		undoablePicture = picture;
		picture = redoablePicture;
		redoablePicture = null;
		refreshMainView();
		refreshLayerView();
	}

	Tool getActiveTool() {
		return activeTool;
	}

	void forceActiveTool(Tool tool) {
		if (tool != null) {
			saveCurPicForUndo();
		}
		activeTool = tool;
		refreshTools();
	}

	public void setActiveTool(Tool tool) {
		if (activeTool == tool) {
			activeTool = null;
		} else {
			activeTool = tool;
		}
		refreshTools();
	}

	public void setActiveTool(Tool tool, int size) {
		if ((activeTool == tool) && (activeToolSize == size)) {
			activeTool = null;
		} else {
			activeTool = tool;
			activeToolSize = size;
		}
		refreshTools();
	}

	private void refreshTools() {
		if (activeTool != null) {
			switch (activeTool) {
				case DRAW_PEN_FG:
				case DRAW_LINES_FG:
				case FILL_FG:
				case FILL_ROUGHLY_FG:
				case DRAW_RECTANGLE_FG:
				case DRAW_RECTANGLE_BG:
				case DRAW_QUADS_FG:
				case DRAW_QUADS_BG:
				case DRAW_AREA_FG:
				case DRAW_AREA_BG:
					this.lastDrawPoints = new ArrayList<>();
					pictureBeforePointDrawing = getCurrentImageLayer().getImage().copy();
					break;
			}
			if ((activeTool == Tool.DRAW_PEN_FG) || (activeTool == Tool.DRAW_LINES_FG)) {
				this.drawPicture = pictureBeforePointDrawing;
			}
		}

		guiMenuColors.refreshTools(activeTool, activeToolSize);
		guiMenuDraw.refreshTools(activeTool, activeToolSize);
		guiButtonHolder.refreshTools(activeTool);
	}

	public ImageLayer getCurrentLayer() {
		ImageLayer layer = picture.getLayer(currentLayerIndex);
		if (layer != null) {
			return layer;
		}
		return new ImageLayerBasedOnText(0, 0, "", "", 1, foregroundColor);
	}

	public ImageLayerBasedOnImage getCurrentImageLayer() {
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
		return new ImageLayerBasedOnImage(0, 0, tempImg, "(temp)");
	}

	ImageLayerBasedOnText getCurrentTextLayer() {
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

	public String getTextLayerCaptionText() {
		List<String> texts = new ArrayList<>();
		if (picture != null) {
			int amount = picture.getLayerAmount();
			for (int i = 0; i < amount; i++) {
				ImageLayer layer = picture.getLayer(i);
				if (layer instanceof ImageLayerBasedOnText) {
					texts.add(((ImageLayerBasedOnText) layer).getText());
				}
			}
		}
		return StrUtils.join("\n", texts);
	}

	private String[] getLayerCaptionArray() {
		if (picture == null) {
			return new String[0];
		}
		int amount = picture.getLayerAmount();
		String[] result = new String[amount];
		for (int i = 0; i < amount; i++) {
			ImageLayer layer = picture.getLayer(i);
			String captionStr = "?";
			if (layer instanceof ImageLayerBasedOnText) {
				captionStr = "TXT#" + i + ": " + ((ImageLayerBasedOnText) layer).getText();
			} else {
				if (layer instanceof ImageLayerBasedOnImage) {
					String imgCaption = ((ImageLayerBasedOnImage) layer).getCaption();
					if (imgCaption == null) {
						imgCaption = "";
					}
					captionStr = "IMG#" + i + ": " + imgCaption;
				}
			}
			if (captionStr.length() > 18) {
				captionStr = captionStr.substring(0, 16) + "...";
			}
			result[amount - i - 1] = captionStr;
		}
		return result;
	}

	private void setPosLabelTexts(int x, int y) {
		curPosXLabel.setText(" X: " + x);
		curPosYLabel.setText(" Y: " + y);
		curPosWLabel.setText(" W: " + picture.getWidth());
		curPosHLabel.setText(" H: " + picture.getHeight());
		mainPanelLeftColorPicker.repaint();
	}

	public void refreshLayerView() {
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
				String captionStr = imgLayer.getCaption();
				if (captionStr == null) {
					captionStr = "";
				}
				imgLayerTextInput.setText(captionStr);
				imgLayerPanel.setVisible(true);
			}
		}
	}

	public int getCurrentLayerIndex() {
		return currentLayerIndex;
	}

	public void setCurrentLayerIndex(int currentLayerIndex) {
		this.currentLayerIndex = currentLayerIndex;
	}

	public int getPrevClickX() {
		return prevClickX;
	}

	public int getPrevClickY() {
		return prevClickY;
	}

	public int getLastClickX() {
		return lastClickX;
	}

	public int getLastClickY() {
		return lastClickY;
	}

	public ConfigFile getConfiguration() {
		return configuration;
	}

	public File getLastOpenedFile() {
		return lastOpenedFile;
	}

	void setZoomFactor(double newZoomFactor) {
		zoomFactor = newZoomFactor;
		refreshMainView();
	}

	public ImageFileCtrl getImageFileCtrl() {
		return imageFileCtrl;
	}

}
