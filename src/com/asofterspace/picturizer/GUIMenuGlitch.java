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
						int boxAmount = MathUtils.randomInteger(5) + 3;
						baseImg.createNoise();
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

		curMenuItem = new JMenuItem("Random Glitch");
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
						Image origImg = baseImg.copy();
						int midX = MathUtils.randomInteger(baseImg.getWidth());
						int midY = MathUtils.randomInteger(baseImg.getHeight());
						int wid = MathUtils.randomInteger(baseImg.getWidth());
						int hei = MathUtils.randomInteger(baseImg.getHeight());
						int fromX = midX - wid;
						int fromY = midY - hei;
						int untilX = midX + wid;
						int untilY = midY + hei;
						if (fromX > 0) {
							fromX = 0;
						}
						if (fromY > 0) {
							fromY = 0;
						}
						if (untilX >= baseImg.getWidth()) {
							untilX = baseImg.getWidth() - 1;
						}
						if (untilY >= baseImg.getHeight()) {
							untilY = baseImg.getHeight() - 1;
						}

						int whichGlitch = MathUtils.randomInteger(3);
						switch (whichGlitch) {
							case 0:
								int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
								int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
								drawImg.draw(baseImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
								break;
							case 1:
								baseImg.createNoise();
								drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
								break;
							default:
								baseImg.pixelate(MathUtils.randomInteger(16)+4);
								drawImg.draw(baseImg, fromX, fromY, fromX, fromY, untilX, untilY);
								break;
						}
						drawImg.intermixImage(origImg, (float) MathUtils.randomDouble(1.0));

						il.setImage(drawImg);
						gui.setPictureUndoTakenCareOf(iml);
					}
				}
			}
		});
		glitch.add(curMenuItem);

		return glitch;
	}
}
