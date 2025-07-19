/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.gui.menu;

import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.picturizer.Picturizer;
import com.asofterspace.picturizer.utils.GlitchUtils;
import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.images.ImageLayer;
import com.asofterspace.toolbox.images.ImageLayerBasedOnImage;
import com.asofterspace.toolbox.images.ImageMultiLayered;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.utils.MathUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUIMenuGlitch {

	private static volatile boolean glitchThreadRunning = false;


	public JMenu createMenu(GUI gui) {

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
						il.setImage(GlitchUtils.boxShatter(il.getImage()));
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		curMenuItem = new JMenuItem("Box-Swap");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.saveCurPicForUndo();
				ImageMultiLayered iml = gui.getPicture();
				ImageLayer layer = iml.getLayer(gui.getCurrentLayerIndex());
				if (layer != null) {
					if (layer instanceof ImageLayerBasedOnImage) {
						ImageLayerBasedOnImage il = (ImageLayerBasedOnImage) layer;
						il.setImage(GlitchUtils.boxSwap(il.getImage()));
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
						il.setImage(GlitchUtils.boxKrizzel(il.getImage()));
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
						il.setImage(GlitchUtils.boxPixelate(il.getImage()));
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
						il.setImage(GlitchUtils.lineifyVertically(il.getImage()));
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
						il.setImage(GlitchUtils.boxLineifyVertically(il.getImage()));
						gui.setPictureUndoTakenCareOf(iml);
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
						il.setImage(GlitchUtils.individualPixels(il.getImage()));
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
				applyRandomGlitch(gui, false);
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
								applyRandomGlitch(gui, false);
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

		curMenuItem = new JMenuItem("Glitch Continuously in Directory of Last Opened File (Start / Stop)");
		curMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				glitchThreadRunning = !glitchThreadRunning;

				if (glitchThreadRunning) {
					Thread glitchThread = new Thread() {
						public void run() {
							while (glitchThreadRunning) {
								applyRandomGlitch(gui, true);
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

	private void applyRandomGlitch(GUI gui, boolean loadRandomImageFiles) {

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

				int whichGlitch = MathUtils.randomInteger(20);
				// skip the image loading if we do not want it to happen
				if (!loadRandomImageFiles) {
					whichGlitch = whichGlitch + 2;
				}
				switch (whichGlitch) {
					case 0:
					case 1:
						// load an image
						ConfigFile configuration = gui.getConfiguration();
						String lastDirectory = configuration.getValue(GUI.CONFIG_KEY_LAST_DIRECTORY);
						if (lastDirectory != null) {
							Directory lastDir = new Directory(lastDirectory);
							boolean recursively = false;
							List<File> imageFiles = lastDir.getAllFiles(recursively);
							int randomFileNum = MathUtils.randomInteger(imageFiles.size());
							Image newImg = Picturizer.getImageFileCtrl().loadImageFromFile(imageFiles.get(randomFileNum));
							int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
							int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
							drawImg.draw(newImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
						}
						break;
					case 2:
					case 3:
					case 4:
						// box-shatter
						int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
						int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
						drawImg.draw(baseImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
						break;
					case 5:
					case 6:
						// box-swap
						int fromX2 = MathUtils.randomInteger(baseImg.getWidth());
						int fromY2 = MathUtils.randomInteger(baseImg.getHeight());
						int untilX2 = fromX2 + untilX - fromX;
						int untilY2 = fromY2 + untilY - fromY;
						drawImg.draw(baseImg, fromX2, fromY2, fromX, fromY, untilX, untilY);
						drawImg.draw(baseImg, fromX, fromY, fromX2, fromY2, untilX2, untilY2);
						break;
					case 7:
					case 8:
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
					case 9:
					case 10:
					case 11:
						// box-pixelate
						baseImg.pixelate(MathUtils.randomInteger(16)+4);
						drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
						break;
					case 12:
					case 13:
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
