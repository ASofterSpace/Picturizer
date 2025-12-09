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

	private static final boolean debug = false;

	private String storedEffect = null;
	private Integer storedFromFrameNum = null;
	private Integer storedToFrameNum = null;
	private Integer storedTo2FrameNum = null;
	private Integer storedTo3FrameNum = null;
	private String text = null;
	private String font = null;
	private Integer fadeIn = null;
	private Integer fadeOut = null;
	private Integer maximizeIn = null;
	private Integer minimizeOut = null;
	private Integer top = null;
	private Integer left = null;
	private Integer bottom = null;
	private Integer right = null;
	private Integer fromX = null;
	private Integer fromY = null;
	private Integer midX = null;
	private Integer midY = null;
	private Integer untilX = null;
	private Integer untilY = null;
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
		storedFromFrameNum = rec.getInteger("from", null);
		storedToFrameNum = rec.getInteger("to", null);
		storedTo2FrameNum = rec.getInteger("to2", null);
		storedTo3FrameNum = rec.getInteger("to3", null);
		storedEffect = rec.getString("effect");
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
		midX = rec.getInteger("midX", null);
		midY = rec.getInteger("midY", null);
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

	public boolean applyTo(int frameNum, int lastFrameNum, VideoFrame vidFrame, VideoFrame prevVidFrame) {
		String effect = storedEffect;
		Integer fromFrameNum = storedFromFrameNum;
		Integer toFrameNum = storedToFrameNum;
		if ("hide-appear-wobble-image-part".equals(effect)) {
			if (frameNum < storedToFrameNum) {
				effect = "hide-image-part";
			} else {
				// actually go directly to wobble but fade in the wobble
				effect = "appear-wobble-image-part";
				fromFrameNum = storedToFrameNum;
				toFrameNum = storedTo2FrameNum;
				/*
				if (frameNum < storedTo2FrameNum) {
					effect = "appear-image-part";
					fromFrameNum = storedToFrameNum;
					toFrameNum = storedTo2FrameNum;
				} else {
					effect = "wobble-image-part";
					fromFrameNum = storedTo2FrameNum;
					toFrameNum = storedTo3FrameNum;
				}
				*/
			}
		}

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

		int fromFrameNumSafe = 0;
		if (fromFrameNum != null) {
			fromFrameNumSafe = fromFrameNum;
		}
		int toFrameNumSafe = lastFrameNum;
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
					savedVidFrame = prevVidFrame;
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

		String debugLine = null;
		if (debug) {
			debugLine = frameNum + ": " + effect;
		}
		switch (effect) {
			case "hide-image-part":
				// if (midX,midY) is given, draw a rotated rectangle
				if ((midX != null) && (midY != null)) {
					img.drawRotatedRectangle(fromX, fromY, midX, midY, untilX, untilY, color);
				} else {
					img.drawRectangle(fromX, fromY, untilX, untilY, color);
				}
				break;

			case "appear-image-part":
				// if (midX,midY) is given, draw a rotated rectangle
				if ((midX != null) && (midY != null)) {
					img.drawRotatedRectangle(fromX, fromY, midX, midY, untilX, untilY, color);
				} else {
					img.drawRectangle(fromX, fromY, untilX, untilY, color);
				}
				float amountOfFramesForThisEffectf = toFrameNumSafe - fromFrameNumSafe;
				float fadeAmount = (toFrameNumSafe - frameNum) / amountOfFramesForThisEffectf;
				img.intermixImage(baseImg, fadeAmount);
				if (debug) {
					debugLine += " (fadeAmount: " + fadeAmount + ")";
				}
				break;

			case "appear-wobble-image-part":
			case "wobble-image-part":
				Image imgPart = null;
				// if (midX,midY) is given, draw a rotated rectangle
				if ((midX != null) && (midY != null)) {
					imgPart = img.copyRotatedRectangle(fromX, fromY, midX, midY, untilX, untilY, color);
					img.drawRotatedRectangle(fromX, fromY, midX, midY, untilX, untilY, color);
				} else {
					imgPart = img.copy(fromY, untilX, untilY, fromX);
					img.drawRectangle(fromX, fromY, untilX, untilY, color);
				}
				amountOfFramesForThisEffectf = toFrameNumSafe - fromFrameNumSafe;
				float resizeFactor = 1.0f;
				// wobble in four parts, which are actually three:
				// (1): go back (shrink)
				if (frameNum < fromFrameNumSafe + (amountOfFramesForThisEffectf / 4)) {
					float target = fromFrameNumSafe + (amountOfFramesForThisEffectf / 4);
					resizeFactor = ((target - frameNum) / amountOfFramesForThisEffectf) + 0.75f;
					if (debug) {
						debugLine += " (case (1)";
					}
				} else {
					// (2): go forward (expand) until regular amount
					// (3): go forward (expand) further
					if (frameNum < fromFrameNumSafe + ((amountOfFramesForThisEffectf * 3) / 4)) {
						float target = fromFrameNumSafe + ((amountOfFramesForThisEffectf * 3) / 4);
						resizeFactor = (((2 * (frameNum - target)) / amountOfFramesForThisEffectf) / 2) + 1.25f;
						if (debug) {
							debugLine += " (case (2)/(3)";
						}
					} else {
						// (4): go back (shrink) until original
						float target = fromFrameNumSafe + amountOfFramesForThisEffectf;
						resizeFactor = ((target - frameNum) / amountOfFramesForThisEffectf) + 1.0f;
						if (debug) {
							debugLine += " (case (4)";
						}
					}
				}
				int widthBefore = imgPart.getWidth();
				int heightBefore = imgPart.getHeight();
				int widthAfter = (int) (widthBefore * resizeFactor);
				int heightAfter = (int) (heightBefore * resizeFactor);
				imgPart.resampleTo(widthAfter, heightAfter);
				int yOffset = (heightBefore - heightAfter) / 2;
				int xOffset = (widthBefore - widthAfter) / 2;
				if (debug) {
					debugLine += ", resizeFactor: " + resizeFactor + ")";
				}

				Image extraImg = null;
				if ("appear-wobble-image-part".equals(effect)) {
					extraImg = img.copy();
				}

				if ((midX != null) && (midY != null)) {
					int otherMidX = fromX + untilX - midX;
					int otherMidY = fromY + untilY - midY;
					int minX = MathUtils.min(fromX, midX, untilX, otherMidX);
					int minY = MathUtils.min(fromY, midY, untilY, otherMidY);
					img.draw(imgPart, minX + xOffset, minY + yOffset, color);
				} else {
					int minX = MathUtils.min(fromX, untilX);
					int minY = MathUtils.min(fromY, untilY);
					// draw transparently when expanding or using a rotated rectangle,
					// but non-transparently when shrinking a normal rectangle
					// (as the background is drawn over anyway and non-transparently is faster)
					if (resizeFactor > 1.0f) {
						img.draw(imgPart, minX + xOffset, minY + yOffset, color);
					} else {
						img.draw(imgPart, minX + xOffset, minY + yOffset);
					}
				}

				if (extraImg != null) {
					float amountOfFramesForFade = amountOfFramesForThisEffectf / 8;
					fadeAmount = (frameNum - fromFrameNumSafe) / amountOfFramesForFade;
					if (fadeAmount < 1.0f) {
						img.intermixImage(extraImg, fadeAmount);
					}
				}

				break;

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
				shiftedImg = workImg.copy();
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

		if (debug) {
			System.out.println(debugLine);
		}

		return true;
	}

}
