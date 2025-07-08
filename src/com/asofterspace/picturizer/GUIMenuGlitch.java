/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageLayer;
import com.asofterspace.toolbox.images.ImageLayerBasedOnImage;
import com.asofterspace.toolbox.images.ImageMultiLayered;
import com.asofterspace.toolbox.utils.MathUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuGlitch {

	private static volatile boolean glitchThreadRunning = false;


	JMenu createMenu(GUI gui) {

		JMenu glitch = new JMenu("Glitch");

		JMenuItem curMenuItem;

		curMenuItem = new JMenuItem("Box-Shatter");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();
						int boxAmount = MathUtils.randomInteger(5) + 3;
						for (int i = 0; i < boxAmount; i++) {
							int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
							int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
							int fromX = MathUtils.randomInteger(baseImg.getWidth());
							int fromY = MathUtils.randomInteger(baseImg.getHeight());
							int untilX = fromX + MathUtils.randomInteger(baseImg.getWidth() - fromX);
							int untilY = fromY + MathUtils.randomInteger(baseImg.getHeight() - fromY);
							drawImg.draw(baseImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
						}
						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Box-Krizzel");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();
						int boxAmount = MathUtils.randomInteger(25) + 10;
						baseImg.createNoise();
						for (int i = 0; i < boxAmount; i++) {
							int fromX = MathUtils.randomInteger(baseImg.getWidth());
							int fromY = MathUtils.randomInteger(baseImg.getHeight());
							int divX = 10;
							int divY = 1;
							if (MathUtils.randomInteger(2) < 1) {
								divX = 1;
								divY = 10;
							}
							int untilX = fromX + (MathUtils.randomInteger(baseImg.getWidth()) / divX);
							int untilY = fromY + (MathUtils.randomInteger(baseImg.getHeight()) / divY);
							drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
						}
						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Box-Pixelate");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();
						int boxAmount = MathUtils.randomInteger(5) + 3;
						baseImg.pixelate(MathUtils.randomInteger(16)+4);
						for (int i = 0; i < boxAmount; i++) {
							int fromX = MathUtils.randomInteger(baseImg.getWidth());
							int fromY = MathUtils.randomInteger(baseImg.getHeight());
							int untilX = fromX + MathUtils.randomInteger(baseImg.getWidth() - fromX);
							int untilY = fromY + MathUtils.randomInteger(baseImg.getHeight() - fromY);
							drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
						}
						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Lineify Vertically");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();
						int lineAmount = MathUtils.randomInteger(25) + 15;
						for (int l = 0; l < lineAmount; l++) {
							int curY = MathUtils.randomInteger(baseImg.getHeight());
							drawImg.drawRectangle(0, curY, baseImg.getWidth() - 1, curY, baseImg.getPixel(0, curY));
						}
						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Box-Lineify Vertically");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();

						int boxAmount = MathUtils.randomInteger(5) + 3;
						for (int i = 0; i < boxAmount; i++) {

							int midX = MathUtils.randomInteger(baseImg.getWidth());
							int midY = MathUtils.randomInteger(baseImg.getHeight());
							int wid = MathUtils.randomInteger(baseImg.getWidth() / 3);
							int hei = MathUtils.randomInteger(baseImg.getHeight() / 3);
							int fromX = midX - wid;
							int fromY = midY - hei;
							int untilX = midX + wid;
							int untilY = midY + hei;
							if (fromX < 0) {
								fromX = 0;
							}
							if (fromY < 0) {
								fromY = 0;
							}
							if (untilX >= baseImg.getWidth()) {
								untilX = baseImg.getWidth() - 1;
							}
							if (untilY >= baseImg.getHeight()) {
								untilY = baseImg.getHeight() - 1;
							}

							int lineAmount = MathUtils.randomInteger(9) + 3;
							for (int l = 0; l < lineAmount; l++) {
								int curY = fromY + MathUtils.randomInteger(untilY - fromY);
								drawImg.drawRectangle(fromX, curY, untilX, curY, baseImg.getPixel(0, curY));
							}
							il.setImage(drawImg);
							gui.setPictureUndoTakenCareOf(iml);
						}
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Individual Pixels");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						Image baseImg = il.getImage();
						Image drawImg = baseImg.copy();
						int pixAmount = MathUtils.randomInteger(15) + 5;
						int pixSize = MathUtils.randomInteger(9) + 3;
						for (int p = 0; p < pixAmount; p++) {
							int curX = MathUtils.randomInteger(baseImg.getWidth() / pixSize);
							int curY = MathUtils.randomInteger(baseImg.getHeight() / pixSize);
							drawImg.drawRectangle(curX*pixSize, curY*pixSize, (curX+1)*pixSize, (curY+1)*pixSize,
								baseImg.getPixel(curX*pixSize, curY*pixSize));
						}
						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Random Glitch");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyRandomGlitch(gui);
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Glitch Continuously (Start / Stop)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				glitchThreadRunning = !glitchThreadRunning;

				if (glitchThreadRunning) {
					Thread glitchThread = new Thread() {
						public void run() {
							while (glitchThreadRunning) {
								applyRandomGlitch(gui);
								try {
									// wait 128 ms between glitches
									Thread.sleep(128);
								} catch (InterruptedException e) {
									// task interrupted - let's bail, this is not so important anyway!
									break;
								}
							}
						}
					};
					glitchThread.start();
				}
			}
		});
		glitch.add(curMenuItem);

		return glitch;
	}

	private void applyRandomGlitch(GUI gui) {

		gui.saveCurPicForUndo();
		ImageMultiLayered iml = gui.getPicture();
		ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
		if (layer != null) {
			if (layer instanceof ImageLayerBasedOnImage) {
				ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
				Image baseImg = il.getImage();
				Image drawImg = baseImg.copy();
				Image origImg = baseImg.copy();
				int midX = MathUtils.randomInteger(baseImg.getWidth());
				int midY = MathUtils.randomInteger(baseImg.getHeight());
				int wid = MathUtils.randomInteger(baseImg.getWidth());
				int hei = MathUtils.randomInteger(baseImg.getHeight());
				int fromX = midX - wid;
				int fromY = midY - hei;
				int untilX = midX + wid;
				int untilY = midY + hei;
				if (fromX < 0) {
					fromX = 0;
				}
				if (fromY < 0) {
					fromY = 0;
				}
				if (untilX >= baseImg.getWidth()) {
					untilX = baseImg.getWidth() - 1;
				}
				if (untilY >= baseImg.getHeight()) {
					untilY = baseImg.getHeight() - 1;
				}

				int whichGlitch = MathUtils.randomInteger(9);
				switch (whichGlitch) {
					case 0:
						// box-shatter
						int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
						int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
						drawImg.draw(baseImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
						break;
					case 1:
						// box-krizzel
						baseImg.createNoise();
						int boxAmount = MathUtils.randomInteger(10) + 2;
						for (int i = 0; i < boxAmount; i++) {
							fromX = MathUtils.randomInteger(baseImg.getWidth());
							fromY = MathUtils.randomInteger(baseImg.getHeight());
							int divX = 25;
							int divY = 5;
							if (MathUtils.randomInteger(2) < 1) {
								divX = 5;
								divY = 25;
							}
							untilX = fromX + (MathUtils.randomInteger(baseImg.getWidth()) / divX);
							untilY = fromY + (MathUtils.randomInteger(baseImg.getHeight()) / divY);
							drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
						}
						break;
					case 2:
						// box-pixelate
						baseImg.pixelate(MathUtils.randomInteger(16)+4);
						drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
						break;
					case 3:
						// box-line
						int lineAmount = MathUtils.randomInteger(7) + 3;
						for (int l = 0; l < lineAmount; l++) {
							int curY = fromY + MathUtils.randomInteger(untilY - fromY);
							drawImg.drawRectangle(fromX, curY, untilX, curY, baseImg.getPixel(0, curY));
						}
						break;
					default:
						// individual pixels
						int pixAmount = MathUtils.randomInteger(9) + 3;
						int pixSize = MathUtils.randomInteger(9) + 3;
						for (int p = 0; p < pixAmount; p++) {
							int curX = MathUtils.randomInteger(baseImg.getWidth() / pixSize);
							int curY = MathUtils.randomInteger(baseImg.getHeight() / pixSize);
							drawImg.drawRectangle(curX*pixSize, curY*pixSize, (curX+1)*pixSize, (curY+1)*pixSize,
								baseImg.getPixel(curX*pixSize, curY*pixSize));
						}
						break;
				}
				drawImg.intermixImage(origImg, (float) MathUtils.randomDouble(1.0));

				il.setImage(drawImg);
				gui.setPictureUndoTakenCareOf(iml);
			}
		}
	}

}
