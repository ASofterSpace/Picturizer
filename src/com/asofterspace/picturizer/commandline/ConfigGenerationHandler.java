/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.ImageLayerBasedOnText;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.utils.MathUtils;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigGenerationHandler {

	private Record cgRoot = null;


	public ConfigGenerationHandler(Record cgRootRec) {
		this.cgRoot = cgRootRec;
	}

	public void run() {
		if (cgRoot == null) {
			System.out.println("config missing!");
			return;
		}

		JsonFile templateFile = new JsonFile(cgRoot.getString("template"));
		Record templateRoot = null;
		try {
			templateRoot = templateFile.getAllContents();
		} catch (JsonParseException e) {
			System.out.println(e);
			return;
		}

		File targetFile = new File(cgRoot.getString("target"));

		String kind = cgRoot.getString("kind");

		List<Record> effects = new ArrayList<>();

		Record configRec = templateRoot.get("config");
		if (cgRoot.getBoolean("templateKeepExistingEffects", true)) {
			effects.addAll(configRec.getArray("effects"));
		}

		if (kind != null) {
			switch (kind) {
				case "subtitles":
					int width = cgRoot.getInteger("width", 0);
					int height = cgRoot.getInteger("height", 0);
					SimpleFile srtFile = new SimpleFile(cgRoot.getString("srtInput"));
					List<String> srtLines = srtFile.getContents();
					int lineKind = 0;
					int curFromFrame = 0;
					int curToFrame = 0;
					double fps = cgRoot.getDouble("fps", 24.0);
					String curSub = null;
					// List<String> delOutLines = new ArrayList<>();
					for (String line : srtLines) {
						switch (lineKind) {
							case 0:
								if (!line.equals("")) {
									lineKind++;
								}
								break;
							case 1:
								if (line.contains(" --> ")) {
									curFromFrame = timestampToFrameNum(line.substring(0, line.indexOf(" --> ")), fps);
									curToFrame = timestampToFrameNum(line.substring(line.indexOf(" --> ") + 5), fps);
									// for (int f = curFromFrame; f < curToFrame + 1; f++) {
										// delOutLines.add("rm " + StrUtils.leftPad0(f, 8) + ".png");
									// }
									lineKind++;
								}
								break;
							case 2:
								if (line.equals("")) {
									Record r = Record.emptyObject();
									int offsetX = 0;
									int offsetY = 0;
									String fontName = "Neuropol-Regular";
									Integer fontSize = 32;
									ColorRGBA textColor = new ColorRGBA(MathUtils.randomInteger(128) + 128, MathUtils.randomInteger(128),
										MathUtils.randomInteger(128) + 128);
									ImageLayerBasedOnText ilText = new ImageLayerBasedOnText(offsetX, offsetY, curSub, fontName, fontSize, textColor);
									int textWidth = ilText.getWidth();
									r.set("effect", "text");
									r.set("from", curFromFrame);
									r.set("to", curToFrame);
									r.set("font", fontName);
									r.set("size", fontSize);
									r.set("left", MathUtils.randomInteger(width - textWidth));
									r.set("top", MathUtils.randomInteger(height / 10));
									r.set("text", curSub);
									r.set("r", textColor.getR());
									r.set("g", textColor.getG());
									r.set("b", textColor.getB());
									// r.set("color", textColor.toString());
									int len = curToFrame - curFromFrame;
									if (MathUtils.randomInteger(5) < 3) {
										r.set("fadeIn", MathUtils.randomInteger(len / 4));
									}
									if (MathUtils.randomInteger(5) < 3) {
										r.set("fadeOut", MathUtils.randomInteger(len / 4));
									}
									effects.add(r);
									lineKind = 0;
									curSub = null;
								} else {
									if (curSub == null) {
										curSub = line;
									} else {
										curSub += "\n" + line;
									}
								}
								break;
						}
					}
					configRec.set("effects", effects);
					JsonFile outFile = new JsonFile(targetFile);
					outFile.save(templateRoot);
					// SimpleFile delFile = new SimpleFile("delete_subtitle_frames.sh");
					// delFile.saveContents(delOutLines);
					System.out.println("Saved output, based on " + templateFile.getCanonicalFilename() + ", to: " + outFile.getCanonicalFilename());
					return;

				case "glitch":
					Map<Integer, Integer> effectOngoingUntil = new HashMap<>();
					int from = cgRoot.getInteger("from", 0);
					int to = cgRoot.getInteger("to", 0);
					width = cgRoot.getInteger("width", 0);
					height = cgRoot.getInteger("height", 0);
					int cur = from;
					int lastPrintAt = cur;
					while (cur < to) {
						Record r = Record.emptyObject();
						r.set("from", cur);
						int duration = MathUtils.randomInteger(512);
						if (duration % 2 == 0) {
							duration = duration / 2;
						}
						int toFrame = cur + duration;
						if (toFrame > to) {
							toFrame = to;
						}
						r.set("to", toFrame);
						int left = MathUtils.randomInteger(width);
						int right = MathUtils.randomInteger(width);
						if (left > right) {
							int c = left;
							left = right;
							right = c;
						}
						int top = MathUtils.randomInteger(height);
						int bottom = MathUtils.randomInteger(height);
						if (top > bottom) {
							int c = top;
							top = bottom;
							bottom = c;
						}

						int newEffectNum = MathUtils.randomInteger(40);
						switch (newEffectNum) {
							case 6:
							case 7:
							case 8:
							case 9:
							case 12:
							case 13:
							case 14:
								// max 1 at the same time
								Integer thisEffectOngoingUntil = effectOngoingUntil.get(newEffectNum);
								if ((thisEffectOngoingUntil != null) && (thisEffectOngoingUntil > cur)) {
									// if one of these still ongoing, do a default individual pixelation instead
									newEffectNum = 20;
								}
								break;
						}

						effectOngoingUntil.put(newEffectNum, toFrame);

						switch (newEffectNum) {

							case 0:
							case 1:
								r.set("effect", "glitch-box-pixelate");
								r.set("left", left);
								r.set("top", top);
								r.set("right", right);
								r.set("bottom", bottom);
								r.set("size", MathUtils.randomInteger(16) + 4);
								break;

							case 2:
							case 3:
								r.set("effect", "glitch-box-shatter");
								r.set("left", MathUtils.randomInteger(width));
								r.set("top", MathUtils.randomInteger(height));
								r.set("fromX", left);
								r.set("fromY", top);
								r.set("untilX", right);
								r.set("untilY", bottom);
								break;

							case 4:
							case 5:
								r.set("effect", "glitch-rectangle");
								r.set("left", left);
								r.set("top", top);
								r.set("right", right);
								r.set("bottom", bottom);
								r.set("color", (new ColorRGBA(MathUtils.randomInteger(128) + 128, MathUtils.randomInteger(128),
									MathUtils.randomInteger(128) + 128)).toString());
								break;

							case 6:
							case 7:
							case 8:
								r.set("effect", "glitch-box-krizzel");
								r.set("left", MathUtils.randomInteger(width));
								r.set("top", MathUtils.randomInteger(height));
								r.set("fromX", left);
								r.set("fromY", top);
								r.set("untilX", left + ((right - left) / 2));
								r.set("untilY", top + ((bottom - top) / 2));
								break;

							case 9:
								r.set("effect", "glitch-stripes");
								r.set("amount", MathUtils.randomInteger(32)+16);
								break;

							case 10:
							case 11:
								r.set("effect", "glitch-wobble");
								break;

							case 12:
								r.set("effect", "intensify");
								break;

							case 13:
								r.set("effect", "dampen");
								break;

							case 14:
								r.set("effect", "colorize");
								r.set("r", MathUtils.randomInteger(64) + 90);
								r.set("g", MathUtils.randomInteger(64) + 24);
								r.set("b", MathUtils.randomInteger(64) + 100);
								break;

							case 15:
								r.set("effect", "box-colorize");
								r.set("r", MathUtils.randomInteger(64) + 90);
								r.set("g", MathUtils.randomInteger(64) + 24);
								r.set("b", MathUtils.randomInteger(64) + 100);
								r.set("left", left);
								r.set("top", top);
								r.set("right", right);
								r.set("bottom", bottom);
								break;

							default:
								r.set("effect", "glitch-individual-pixels");
								r.set("left", MathUtils.randomInteger(width));
								r.set("top", MathUtils.randomInteger(height));
								r.set("size", MathUtils.randomInteger(16) + 4);
								break;
						}

						int len = toFrame - cur;
						if (MathUtils.randomInteger(5) < 3) {
							r.set("fadeIn", MathUtils.randomInteger(len / 2));
						}
						if (MathUtils.randomInteger(5) < 3) {
							r.set("fadeOut", MathUtils.randomInteger(len / 2));
						}

						effects.add(r);
						cur += MathUtils.randomInteger(10);

						if (lastPrintAt + 128 < cur) {
							System.out.println("At " + cur + " / " + to + "...");
							lastPrintAt = cur;
						}
					}
					configRec.set("effects", effects);
					outFile = new JsonFile(targetFile);
					outFile.save(templateRoot);
					System.out.println("Saved output, based on " + templateFile.getCanonicalFilename() + ", to: " + outFile.getCanonicalFilename());
					return;
			}
		}

		System.out.println("Unknown config generation kind: " + kind);
	}

	private static int timestampToFrameNum(String timestampStr, double fps) {
		int result = 0;
		if (timestampStr.contains(".")) {
			result += (int) (fps * StrUtils.strToDouble("0." + timestampStr.substring(timestampStr.indexOf(".") + 1)));
			timestampStr = timestampStr.substring(0, timestampStr.indexOf("."));
		}
		if (timestampStr.contains(",")) {
			result += (int) (fps * StrUtils.strToDouble("0." + timestampStr.substring(timestampStr.indexOf(",") + 1)));
			timestampStr = timestampStr.substring(0, timestampStr.indexOf(","));
		}
		if (timestampStr.contains(":")) {
			result += (int) (fps * StrUtils.strToInt(timestampStr.substring(timestampStr.lastIndexOf(":") + 1)));
			timestampStr = timestampStr.substring(0, timestampStr.lastIndexOf(":"));
			if (timestampStr.contains(":")) {
				result += (int) (fps * 60 * StrUtils.strToInt(timestampStr.substring(timestampStr.lastIndexOf(":") + 1)));
				timestampStr = timestampStr.substring(0, timestampStr.lastIndexOf(":"));
				result += (int) (fps * 60 * 60 * StrUtils.strToInt(timestampStr));
			} else {
				result += (int) (fps * 60 * StrUtils.strToInt(timestampStr));
			}
		} else {
			result += (int) (fps * StrUtils.strToInt(timestampStr));
		}
		return result;
	}
}
