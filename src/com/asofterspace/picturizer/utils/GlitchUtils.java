/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.utils;

import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.utils.MathUtils;


public class GlitchUtils {

	public static Image boxShatter(Image img) {
		Image drawImg = img.copy();
		int boxAmount = MathUtils.randomInteger(5) + 3;
		for (int i = 0; i < boxAmount; i++) {
			int drawAtX = MathUtils.randomInteger((drawImg.getWidth() * 12) / 10) - ((drawImg.getWidth() * 2) / 10);
			int drawAtY = MathUtils.randomInteger((drawImg.getHeight() * 12) / 10) - ((drawImg.getHeight() * 2) / 10);
			int fromX = MathUtils.randomInteger(img.getWidth());
			int fromY = MathUtils.randomInteger(img.getHeight());
			int untilX = fromX + MathUtils.randomInteger(img.getWidth() - fromX);
			int untilY = fromY + MathUtils.randomInteger(img.getHeight() - fromY);
			drawImg.draw(img, drawAtX, drawAtY, fromX, fromY, untilX, untilY);
		}
		return drawImg;
	}

	public static Image boxSwap(Image img) {
		Image drawImg = img.copy();
		int boxAmount = MathUtils.randomInteger(5) + 3;
		for (int i = 0; i < boxAmount; i++) {
			int fromX1 = MathUtils.randomInteger(img.getWidth());
			int fromY1 = MathUtils.randomInteger(img.getHeight());
			int untilX1 = fromX1 + MathUtils.randomInteger(img.getWidth() / 5);
			int untilY1 = fromY1 + MathUtils.randomInteger(img.getHeight() / 5);
			int fromX2 = MathUtils.randomInteger(img.getWidth());
			int fromY2 = MathUtils.randomInteger(img.getHeight());
			int untilX2 = fromX2 + untilX1 - fromX1;
			int untilY2 = fromY2 + untilY1 - fromY1;
			drawImg.draw(img, fromX2, fromY2, fromX1, fromY1, untilX1, untilY1);
			drawImg.draw(img, fromX1, fromY1, fromX2, fromY2, untilX2, untilY2);
		}
		return drawImg;
	}

	public static Image boxKrizzel(Image img) {
		Image drawImg = img.copy();
		int boxAmount = MathUtils.randomInteger(25) + 10;
		img.createNoise();
		for (int i = 0; i < boxAmount; i++) {
			int fromX = MathUtils.randomInteger(img.getWidth());
			int fromY = MathUtils.randomInteger(img.getHeight());
			int divX = 10;
			int divY = 1;
			if (MathUtils.randomInteger(2) < 1) {
				divX = 1;
				divY = 10;
			}
			int untilX = fromX + (MathUtils.randomInteger(img.getWidth()) / divX);
			int untilY = fromY + (MathUtils.randomInteger(img.getHeight()) / divY);
			drawImg.draw(img, fromX, fromY, fromX, fromY, untilX, untilY);
		}
		return drawImg;
	}

	public static Image boxPixelate(Image img) {
		Image drawImg = img.copy();
		int boxAmount = MathUtils.randomInteger(5) + 3;
		img.pixelate(MathUtils.randomInteger(16)+4);
		for (int i = 0; i < boxAmount; i++) {
			int fromX = MathUtils.randomInteger(img.getWidth());
			int fromY = MathUtils.randomInteger(img.getHeight());
			int untilX = fromX + MathUtils.randomInteger(img.getWidth() - fromX);
			int untilY = fromY + MathUtils.randomInteger(img.getHeight() - fromY);
			drawImg.draw(img, fromX, fromY, fromX, fromY, untilX, untilY);
		}
		return drawImg;
	}

	public static Image lineifyVertically(Image img) {
		Image drawImg = img.copy();
		int lineAmount = MathUtils.randomInteger(25) + 15;
		for (int l = 0; l < lineAmount; l++) {
			int curY = MathUtils.randomInteger(img.getHeight());
			drawImg.drawRectangle(0, curY, img.getWidth() - 1, curY, img.getPixel(0, curY));
		}
		return drawImg;
	}

	public static Image boxLineifyVertically(Image img) {
		Image drawImg = img.copy();

		int boxAmount = MathUtils.randomInteger(5) + 3;
		for (int i = 0; i < boxAmount; i++) {

			int midX = MathUtils.randomInteger(img.getWidth());
			int midY = MathUtils.randomInteger(img.getHeight());
			int wid = MathUtils.randomInteger(img.getWidth() / 3);
			int hei = MathUtils.randomInteger(img.getHeight() / 3);
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
			if (untilX >= img.getWidth()) {
				untilX = img.getWidth() - 1;
			}
			if (untilY >= img.getHeight()) {
				untilY = img.getHeight() - 1;
			}

			int lineAmount = MathUtils.randomInteger(9) + 3;
			for (int l = 0; l < lineAmount; l++) {
				int curY = fromY + MathUtils.randomInteger(untilY - fromY);
				drawImg.drawRectangle(fromX, curY, untilX, curY, img.getPixel(0, curY));
			}
		}
		return drawImg;
	}

	public static Image individualPixels(Image img) {
		Image drawImg = img.copy();
		int pixAmount = MathUtils.randomInteger(15) + 5;
		int pixSize = MathUtils.randomInteger(9) + 3;
		for (int p = 0; p < pixAmount; p++) {
			int curX = MathUtils.randomInteger(img.getWidth() / pixSize);
			int curY = MathUtils.randomInteger(img.getHeight() / pixSize);
			drawImg.drawRectangle(curX*pixSize, curY*pixSize, (curX+1)*pixSize, (curY+1)*pixSize,
				img.getPixel(curX*pixSize, curY*pixSize));
		}
		return drawImg;
	}
}
