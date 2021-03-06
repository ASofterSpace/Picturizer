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
import com.asofterspace.toolbox.images.ColorRGB;
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

	private JScrollPane mainPanelRight;

	private JMenuItem close;

	private ConfigFile configuration;

	private ImageIcon imageViewer;
	private JLabel imageViewerLabel;
	private Image picture;
	private Image undoablePicture;
	private Image redoablePicture;

	private QrGUI qrGUI;
	private ChannelChangeGUI channelChangeGUI;

	private ImageFileCtrl imageFileCtrl;

	private final static Directory TEMP_DIR = new Directory("temp");


	public GUI(ConfigFile configFile) {
		this.configuration = configFile;

		// we want to handle as many image formats as we can...
		// even opening images from PDF files, if possible :)
		this.imageFileCtrl = new ImageFileCtrl();
		this.imageFileCtrl.addHandler(new PdfImageHandler());
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
							createPicFromScreenshot(ColorRGB.fromString((String) value));
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
				refreshView();
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

		JMenu removeColors = new JMenu("Remove Colors");
		menu.add(removeColors);

		JMenuItem removeAbsoluteColors = new JMenuItem("Remove Colors by Absolute Brightness");
		removeAbsoluteColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.removeColors();
				refreshView();
			}
		});
		removeColors.add(removeAbsoluteColors);

		JMenuItem removePerceivedColors = new JMenuItem("Remove Colors by Perceived Brightness");
		removePerceivedColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.removePerceivedColors();
				refreshView();
			}
		});
		removeColors.add(removePerceivedColors);

		JMenu invert = new JMenu("Invert");
		menu.add(invert);

		JMenuItem invertColors = new JMenuItem("Invert Colors");
		invertColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.invert();
				refreshView();
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
				refreshView();
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
				refreshView();
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
				refreshView();
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
				refreshView();
			}
		});
		dampen.add(undampenWeakly);

		JMenuItem dampenWeakly = new JMenuItem("1.5 - Dampen Slightly");
		dampenWeakly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurPicForUndo();
				picture = picture.copy();
				picture.dampen(1.5f);
				refreshView();
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
				refreshView();
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
				refreshView();
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
				refreshView();
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
				refreshView();
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
				refreshView();
			}
		});
		darkenBrighten.add(curMenuItem);

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

		imageViewer = new ImageIcon();
		imageViewerLabel = new JLabel(imageViewer);

		mainPanelRight = new JScrollPane(imageViewerLabel);
		mainPanelRight.setBorder(BorderFactory.createEmptyBorder());

		mainPanel.add(mainPanelRight, new Arrangement(0, 0, 1.0, 1.0));

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

		ColorRGB background = new ColorRGB(255, 255, 255);
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
		origPic.expandBottomBy(pdfPic.getHeight() - overlapRows, new ColorRGB(255, 255, 255));

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

	private void createPicFromScreenshot(ColorRGB background) {

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
			ColorRGB firstPixel = pic.getPixel(picLeft, y);
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
				picture = imageFileCtrl.loadImageFromFile(selectedFile);
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

	private void refreshView() {
		imageViewer.setImage(picture.getAwtImage());
		// imageViewerLabel.repaint();
		mainPanelRight.revalidate();
		mainPanelRight.repaint();
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
