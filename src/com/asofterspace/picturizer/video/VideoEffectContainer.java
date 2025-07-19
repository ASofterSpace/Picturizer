/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.video;

import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.utils.MathUtils;
import com.asofterspace.toolbox.utils.Record;


public class VideoEffectContainer {

	private int fromFrameNum = 0;
	private int toFrameNum = -1;
	private String effect = null;
	private Integer fadeIn = null;
	private Integer fadeOut = null;
	private Integer top = null;
	private Integer left = null;
	private Integer bottom = null;
	private Integer fromX = null;
	private Integer fromY = null;
	private Integer untilX = null;
	private Integer untilY = null;
	private Integer right = null;
	private Integer size = null;
	private Integer amount = null;
	private ColorRGBA color = null;
	private int currentRandomState1 = 0;
	private int currentRandomState2 = 0;


	public VideoEffectContainer(Record rec) {
		fromFrameNum = rec.getInteger("from", 0);
		toFrameNum = rec.getInteger("to", -1);
		effect = rec.getString("effect");
		fadeIn = rec.getInteger("fadeIn", null);
		fadeOut = rec.getInteger("fadeOut", null);
		top = rec.getInteger("top", null);
		left = rec.getInteger("left", null);
		bottom = rec.getInteger("bottom", null);
		fromX = rec.getInteger("fromX", null);
		fromY = rec.getInteger("fromY", null);
		untilX = rec.getInteger("untilX", null);
		untilY = rec.getInteger("untilY", null);
		right = rec.getInteger("right", null);
		size = rec.getInteger("size", null);
		amount = rec.getInteger("amount", null);
		String colStr = rec.getString("color", null);
		if (colStr != null) {
			color = ColorRGBA.fromString(colStr);
		}
	}

	public void applyTo(int frameNum, Image img) {
		if (frameNum < fromFrameNum) {
			return;
		}
		if (frameNum > toFrameNum) {
			return;
		}
		if (effect == null) {
			System.out.println("Encountered null effect!");
			return;
		}
		Image baseImg = img.copy();
		switch (effect) {
			case "glitch-load-image":
				/*
				ConfigFile configuration = gui.getConfiguration();
				String lastDirectory = configuration.getValue(GUI.CONFIG_KEY_LAST_DIRECTORY);
				if (lastDirectory != null) {
					Directory lastDir = new Directory(lastDirectory);
					boolean recursively = false;
					List<File> imageFiles = lastDir.getAllFiles(recursively);
					int randomFileNum = MathUtils.randomInteger(imageFiles.size());
					Image newImg = Picturizer.getImageFileCtrl().loadImageFromFile(imageFiles.get(randomFileNum));
					int drawAtX = MathUtils.randomInteger((img.getWidth() * 12) / 10) - ((img.getWidth() * 2) / 10);
					int drawAtY = MathUtils.randomInteger((img.getHeight() * 12) / 10) - ((img.getHeight() * 2) / 10);
					img.draw(newImg, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
				}
				*/
				break;

			case "glitch-box-shatter":
				img.draw(baseImg, top, left, fromX, fromY, untilX, untilY);
				break;

			case "glitch-box-swap":
				/*
				int fromX2 = MathUtils.randomInteger(baseImg.getWidth());
				int fromY2 = MathUtils.randomInteger(baseImg.getHeight());
				int untilX2 = fromX2 + untilX - fromX;
				int untilY2 = fromY2 + untilY - fromY;
				img.draw(baseImg, fromX2, fromY2, fromX, fromY, untilX, untilY);
				img.draw(baseImg, fromX, fromY, fromX2, fromY2, untilX2, untilY2);
				*/
				break;

			case "glitch-box-krizzel":
				Image workImg = img.copy();
				workImg.createNoise();
				img.draw(workImg, top, left, fromX, fromY, untilX, untilY);
				break;

			case "glitch-box-pixelate":
				workImg = img.copy();
				workImg.pixelate(size);
				img.draw(workImg, left, top, left, top, right, bottom);
				break;

			case "glitch-box-line":
				/*
				int lineAmount = MathUtils.randomInteger(7) + 3;
				for (int l = 0; l < lineAmount; l++) {
					int curY = fromY + MathUtils.randomInteger(untilY - fromY);
					img.drawRectangle(fromX, curY, untilX, curY, baseImg.getPixel(0, curY));
				}
				*/
				break;

			case "glitch-individual-pixels":
				int pixSize = size;
				int curX = left/pixSize;
				int curY = top/pixSize;
				img.drawRectangle(curX*pixSize, curY*pixSize, (curX+1)*pixSize, (curY+1)*pixSize,
					baseImg.getPixel(curX*pixSize, curY*pixSize));
				break;

			case "glitch-rectangle":
				img.drawLine(left, top, right, top, color);
				img.drawLine(left, bottom, right, bottom, color);
				img.drawLine(left, top, left, bottom, color);
				img.drawLine(right, top, right, bottom, color);
				break;

			case "glitch-stripes":
				// turn the whole picture stripe-y (so darker stripes, maybe 32 light and 32 dark ones?)
				workImg = img.copy();
				workImg.editChannels("R", 0.75, "G", 0.75, "B", 0.75);
				int stripeAmount = amount;
				for (int i = 0; i < stripeAmount; i++) {
					int x1 = 0;
					int x2 = img.getWidth();
					int y1 = (((2*i)) * img.getHeight()) / (2*stripeAmount);
					int y2 = (((2*i)+1) * img.getHeight()) / (2*stripeAmount);
					img.draw(workImg, x1, y1, x1, y1, x2, y2);
				}
				break;

			case "glitch-wobble":
				img.shiftPosition(currentRandomState1, currentRandomState2);
				currentRandomState1 += MathUtils.randomInteger(5) - 2;
				currentRandomState2 += MathUtils.randomInteger(5) - 2;
				break;

			case "intensify":
				img.intensify();
				break;

			case "dampen":
				img.dampen(1.5f);
				break;

			default:
				System.out.println("Encountered unknown effect: '" + effect + "'");
				break;
		}

		if (fadeIn != null) {
			if (frameNum - fromFrameNum < fadeIn) {
				float fadeAmount = (frameNum - fromFrameNum) / (1.0f * fadeIn);
				img.intermixImage(baseImg, fadeAmount);

			}
		}

		if (fadeOut != null) {
			if (toFrameNum - frameNum < fadeOut) {
				float fadeAmount = (toFrameNum - frameNum) / (1.0f * fadeOut);
				img.intermixImage(baseImg, fadeAmount);

			}
		}
	}

}
