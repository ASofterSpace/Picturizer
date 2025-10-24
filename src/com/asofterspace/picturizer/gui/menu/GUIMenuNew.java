/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.picturizer.gui.QrGUI;
import com.asofterspace.toolbox.gui.GuiUtils;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageMultiLayered;
import com.asofterspace.toolbox.io.File;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuNew {

	private QrGUI qrGUI = null;


	public JMenu createMenu(GUI gui) {

		JMenu newFile = new JMenu("New");

		JMenuItem curItem = new JMenuItem("Empty (of same size as current image = clear with background color)");
		curItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewEmptyFile(gui);
			}
		});
		newFile.add(curItem);

		curItem = new JMenuItem("Noise (of same size as current image)");
		curItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewNoisyFile(gui, 1);
			}
		});
		newFile.add(curItem);

		curItem = new JMenuItem("Diamond (with foreground on background color of same size as current image)");
		curItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int newWidth = 128;
				int newHeight = 128;
				ImageMultiLayered picture = gui.getPicture();
				if (picture != null) {
					newWidth = picture.getWidth();
					newHeight = picture.getHeight();
				}
				Image newImg = new Image(newWidth, newHeight, gui.getBackgroundColor());
				newImg.drawDiamond(0, 0, newWidth-1, newHeight-1, gui.getForegroundColor());
				gui.setPicture(newImg);
			}
		});
		newFile.add(curItem);

		curItem = new JMenuItem("Ellipse (with foreground on background color of same size as current image)");
		curItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int newWidth = 128;
				int newHeight = 128;
				ImageMultiLayered picture = gui.getPicture();
				if (picture != null) {
					newWidth = picture.getWidth();
					newHeight = picture.getHeight();
				}
				Image newImg = new Image(newWidth, newHeight, gui.getBackgroundColor());
				newImg.drawEllipse(0, 0, newWidth-1, newHeight-1, gui.getForegroundColor());
				gui.setPicture(newImg);
			}
		});
		newFile.add(curItem);

		JMenuItem screenshotFile = new JMenuItem("Screenshot");
		screenshotFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createScreenshot(gui);
			}
		});
		newFile.add(screenshotFile);

		Map<String, Object> screenshotBackgrounds =
			gui.getConfiguration().getAllContents().getObjectMap("screenshotBackgrounds");

		for (Map.Entry<String, Object> entry : screenshotBackgrounds.entrySet()) {
			String colorName = entry.getKey();
			Object value = entry.getValue();
			if (value != null) {
				if (value instanceof String) {
					JMenuItem picFromScreenshot = new JMenuItem("Picture from Screenshot (" + colorName + ")");
					picFromScreenshot.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							createPicFromScreenshot(gui, ColorRGBA.fromString((String) value));
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
				createPicFromPDF1(gui);
			}
		});
		newFile.add(picFromPDF1);

		JMenuItem picFromPDF2 = new JMenuItem("Picture from PDF 2");
		picFromPDF2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createPicFromPDF2(gui);
			}
		});
		newFile.add(picFromPDF2);

		JMenuItem qrCodeCore = new JMenuItem("QR Code Core");
		qrCodeCore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (qrGUI == null) {
					qrGUI = new QrGUI(gui);
				}
				qrGUI.setUseFrame(false);
				qrGUI.setBackgroundColor(gui.getBackgroundColor());
				qrGUI.setForegroundColor(gui.getForegroundColor());
				qrGUI.show();
			}
		});
		newFile.add(qrCodeCore);

		JMenuItem qrCodeFramed = new JMenuItem("QR Code Framed");
		qrCodeFramed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (qrGUI == null) {
					qrGUI = new QrGUI(gui);
				}
				qrGUI.setUseFrame(true);
				qrGUI.setBackgroundColor(gui.getBackgroundColor());
				qrGUI.setForegroundColor(gui.getForegroundColor());
				qrGUI.show();
			}
		});
		newFile.add(qrCodeFramed);

		JMenuItem newGrid1 = new JMenuItem("Empty Grid to Align Images");
		newGrid1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean createNew = true;
				GUIMenuGrid.showGridGUI(gui, createNew);
			}
		});
		newFile.add(newGrid1);

		return newFile;
	}

	public void createNewEmptyFile(GUI gui) {
		createNewNoisyFile(gui, 0);
	}

	private void createNewNoisyFile(GUI gui, int generateNoise) {
		int newWidth = 128;
		int newHeight = 128;
		ImageMultiLayered picture = gui.getPicture();
		if (picture != null) {
			newWidth = picture.getWidth();
			newHeight = picture.getHeight();
		}
		Image newImg = new Image(newWidth, newHeight, gui.getBackgroundColor());
		if (generateNoise > 0) {
			newImg.createNoise();
		}
		gui.setPicture(newImg);
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

	private void createPicFromPDF1(GUI gui) {

		Image pdfPic = getPdfPic();

		gui.setPicture(pdfPic);
	}

	private void createPicFromPDF2(GUI gui) {

		// take the first picture (from the top of the page)
		Image origPic = gui.getPicture().bake();

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

		gui.setPicture(origPic);

		// save the screenshot immediately automagically
		GUIMenuFile.TEMP_DIR.create();
		File tempFile = null;
		int fileNum = 1;
		while (true) {
			tempFile = new File(GUIMenuFile.TEMP_DIR, "pdf_" + fileNum + ".png");
			if (tempFile.exists()) {
				fileNum++;
			} else {
				break;
			}
		}
		gui.exportImageToFile(origPic, tempFile);
	}

	private void createPicFromScreenshot(GUI gui, ColorRGBA background) {

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

		gui.setPicture(pic);
	}

	private void createScreenshot(GUI gui) {

		try {
			Robot robot = new Robot();

			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

			BufferedImage screenshotBufImg = robot.createScreenCapture(new Rectangle(screenDimension));

			Image screenshotImg = Image.createFromAwtImage(screenshotBufImg);

			gui.setPicture(screenshotImg);

		} catch (AWTException e) {
			// whoops! guess not...
		}
	}

}
