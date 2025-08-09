/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.video;

import com.asofterspace.picturizer.Picturizer;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.utils.MathUtils;
import com.asofterspace.toolbox.utils.Record;

import java.util.List;


public class VideoEffectContainer {

	private Integer fromFrameNum = null;
	private Integer toFrameNum = null;
	private String effect = null;
	private String text = null;
	private String font = null;
	private Integer fadeIn = null;
	private Integer fadeOut = null;
	private Integer maximizeIn = null;
	private Integer minimizeOut = null;
	private Integer top = null;
	private Integer left = null;
	private Integer bottom = null;
	private Integer fromX = null;
	private Integer fromY = null;
	private Integer untilX = null;
	private Integer untilY = null;
	private Integer right = null;
	private Integer size = null;
	private Integer wobble = null;
	private Integer amount = null;
	private Integer r = null;
	private Integer g = null;
	private Integer b = null;
	private Integer a = null;
	private ColorRGBA color = null;
	private Directory baseDir = null;
	private List<File> baseDirFiles = null;
	private int currentRandomState1 = 0;
	private int currentRandomState2 = 0;
	private VideoFrame savedVidFrame = null;


	public VideoEffectContainer(Record rec) {
		fromFrameNum = rec.getInteger("from", null);
		toFrameNum = rec.getInteger("to", null);
		effect = rec.getString("effect");
		font = rec.getString("font");
		text = rec.getString("text");
		fadeIn = rec.getInteger("fadeIn", null);
		fadeOut = rec.getInteger("fadeOut", null);
		maximizeIn = rec.getInteger("maximizeIn", null);
		minimizeOut = rec.getInteger("minimizeOut", null);
		top = rec.getInteger("top", null);
		left = rec.getInteger("left", null);
		bottom = rec.getInteger("bottom", null);
		fromX = rec.getInteger("fromX", null);
		fromY = rec.getInteger("fromY", null);
		untilX = rec.getInteger("untilX", null);
		untilY = rec.getInteger("untilY", null);
		right = rec.getInteger("right", null);
		size = rec.getInteger("size", null);
		wobble = rec.getInteger("wobble", null);
		amount = rec.getInteger("amount", null);
		r = rec.getInteger("r", null);
		g = rec.getInteger("g", null);
		b = rec.getInteger("b", null);
		a = rec.getInteger("a", null);
		String colStr = rec.getString("color", null);
		if (colStr != null) {
			color = ColorRGBA.fromString(colStr);
		}
		String baseDirStr = rec.getString("baseDir", null);
		if (baseDirStr != null) {
			baseDir = new Directory(baseDirStr);
			boolean recursively = false;
			baseDirFiles = baseDir.getAllFiles(recursively);
		}
	}

	public boolean applyTo(int frameNum, VideoFrame vidFrame, VideoFrame lastVidFrame) {
		if ((fromFrameNum != null) && (frameNum < fromFrameNum)) {
			return false;
		}
		if ((toFrameNum != null) && (frameNum > toFrameNum)) {
			return false;
		}
		if (effect == null) {
			System.out.println("Encountered null effect!");
			return false;
		}

		int fromFrameNumSafe = frameNum;
		if (fromFrameNum != null) {
			fromFrameNumSafe = fromFrameNum;
		}
		int toFrameNumSafe = frameNum;
		if (toFrameNum != null) {
			toFrameNumSafe = toFrameNum;
		}

		// general / overall effects that might not even work on each image, so no need to lead it
		switch (effect) {
			case "video-base-transitions":
				if (vidFrame.isFirstFrameOfSequence()) {
					currentRandomState1 = maximizeIn;
					if (savedVidFrame != null) {
						savedVidFrame.unlock();
						savedVidFrame.clear();
					}
					savedVidFrame = lastVidFrame;
					if (savedVidFrame != null) {
						savedVidFrame.lock();
					}
				}
				if (currentRandomState1 > 0) {
					if (savedVidFrame != null) {
						Image img = vidFrame.getImage();
						Image baseImg = img.copy();
						img.draw(savedVidFrame.getImage(), 0, 0);
						double percSize = (1.0 * (maximizeIn - currentRandomState1)) / maximizeIn;
						boolean keepAspectRatio = true;
						baseImg.resampleTo((int) (percSize * img.getWidth()), (int) (percSize * img.getHeight()), keepAspectRatio);
						img.draw(baseImg, (img.getWidth() - baseImg.getWidth()) / 2, img.getHeight() - baseImg.getHeight());
						currentRandomState1--;
						return true;
					} else {
						currentRandomState1 = 0;
					}
				}
				return false;
		}

		boolean allowOverflow = false;

		// effects that genuinely work on all affected frame images, so we can load it once centrally here
		Image img = vidFrame.getImage();
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
				workImg.editChannels("R", 0.75, "G", 0.75, "B", 0.75, allowOverflow);
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
				if (wobble == null) {
					wobble = 2;
				}
				currentRandomState1 += MathUtils.randomInteger(1 + (2 * wobble)) - wobble;
				currentRandomState2 += MathUtils.randomInteger(1 + (2 * wobble)) - wobble;
				break;

			case "colorize":
				img.editChannels("R", r / 100.0f, "G", g / 100.0f, "B", b / 100.0f, allowOverflow);
				break;

			case "box-colorize":
				workImg = img.copy();
				workImg.editChannels("R", r / 100.0f, "G", g / 100.0f, "B", b / 100.0f, allowOverflow);
				img.draw(workImg, left, top, left, top, right, bottom);
				break;

			case "intensify":
				img.intensify();
				break;

			case "offset-color-shift":
				workImg = img.copy();
				workImg.editChannels("R", r / 100.0f, "G", g / 100.0f, "B", b / 100.0f, allowOverflow);
				Image shiftedImg = workImg.copy();
				shiftedImg.draw(workImg, fromX, fromY);
				img.intermixImageMax(shiftedImg);
				break;

			case "zoom-color-shift":
				workImg = img.copy();
				workImg.editChannels("R", r / 100.0f, "G", g / 100.0f, "B", b / 100.0f, allowOverflow);
				Image shiftedImg = workImg.copy();
				double zoom = fromX / 1000.0;
				workImg.resampleTo((int) (workImg.getWidth() * zoom), (int) (workImg.getHeight() * zoom));
				shiftedImg.draw(workImg, (img.getWidth() - workImg.getWidth()) / 2, (img.getHeight() - workImg.getHeight()) / 2);
				img.intermixImageMax(shiftedImg);
				break;

			case "dampen":
				img.dampen(1.25f);
				break;

			case "text":
				// not currently supported with transparent drawing!
				Boolean useAntiAliasing = false;

				ColorRGBA textColor = color;
				if (textColor == null) {
					r += MathUtils.randomInteger(3) - 1;
					g += MathUtils.randomInteger(3) - 1;
					b += MathUtils.randomInteger(3) - 1;
					if (r > 255) {
						r = 255;
					}
					if (r < 0) {
						r = 0;
					}
					if (g > 255) {
						g = 255;
					}
					if (g < 0) {
						g = 0;
					}
					if (b > 255) {
						b = 255;
					}
					if (b < 0) {
						b = 0;
					}
					textColor = new ColorRGBA(r, g, b);
				}

				img.drawTextTransparently(text, top + currentRandomState1 + 2, null, null, left + currentRandomState2 - 1,
					font, size, useAntiAliasing, ColorRGBA.BLACK);
				img.drawTextTransparently(text, top + currentRandomState1 - 1, null, null, left + currentRandomState2 + 2,
					font, size, useAntiAliasing, ColorRGBA.BLACK);
				img.drawTextTransparently(text, top + currentRandomState1 + 2, null, null, left + currentRandomState2 + 2,
					font, size, useAntiAliasing, ColorRGBA.BLACK);
				img.drawTextTransparently(text, top + currentRandomState1, null, null, left + currentRandomState2,
					font, size, useAntiAliasing, textColor);

				if (wobble != null) {
					currentRandomState1 += MathUtils.randomInteger(1 + (2 * wobble)) - wobble;
					currentRandomState2 += MathUtils.randomInteger(1 + (2 * wobble)) - wobble;
				}
				break;

			case "splice-in-video":
				int splicedInFrameNum = frameNum - fromFrameNumSafe;
				if (splicedInFrameNum < baseDirFiles.size()) {
					File splicedInFrameFile = baseDirFiles.get(splicedInFrameNum);
					if (splicedInFrameFile != null) {
						Image splicedInImg = Picturizer.getImageFileCtrl().loadImageFromFile(splicedInFrameFile);
						if (splicedInImg != null) {
							boolean keepAspectRatio = true;

							boolean drewWithTransition = false;
							if (maximizeIn != null) {
								if (frameNum < fromFrameNumSafe + maximizeIn) {
									double percSize = (1.0 * (frameNum - fromFrameNumSafe)) / maximizeIn;
									splicedInImg.resampleTo((int) (percSize * img.getWidth()), (int) (percSize * img.getHeight()), keepAspectRatio);
									img.draw(splicedInImg, (img.getWidth() - splicedInImg.getWidth()) / 2, img.getHeight() - splicedInImg.getHeight());
									drewWithTransition = true;
								}
							}
							if (minimizeOut != null) {
								if (frameNum > toFrameNumSafe - minimizeOut) {
									double percSize = (1.0 * (toFrameNumSafe - frameNum)) / minimizeOut;
									splicedInImg.resampleTo((int) (percSize * img.getWidth()), (int) (percSize * img.getHeight()), keepAspectRatio);
									img.draw(splicedInImg, (img.getWidth() - splicedInImg.getWidth()) / 2, img.getHeight() - splicedInImg.getHeight());
									drewWithTransition = true;
								}
							}
							if (!drewWithTransition) {
								if ((img.getWidth() != splicedInImg.getWidth()) ||
									(img.getHeight() != splicedInImg.getHeight())) {
									splicedInImg.resampleTo(img.getWidth(), img.getHeight(), keepAspectRatio);
								}
								img.draw(splicedInImg, 0, 0);
							}
						}
					}
				}
				break;

			default:
				System.out.println("Encountered unknown effect: '" + effect + "'");
				break;
		}

		if (fadeIn != null) {
			if (frameNum - fromFrameNumSafe < fadeIn) {
				float fadeAmount = (frameNum - fromFrameNumSafe) / (1.0f * fadeIn);
				img.intermixImage(baseImg, fadeAmount);
			}
		}

		if (fadeOut != null) {
			if (toFrameNumSafe - frameNum < fadeOut) {
				float fadeAmount = (toFrameNumSafe - frameNum) / (1.0f * fadeOut);
				img.intermixImage(baseImg, fadeAmount);
			}
		}

		return true;
	}

}
