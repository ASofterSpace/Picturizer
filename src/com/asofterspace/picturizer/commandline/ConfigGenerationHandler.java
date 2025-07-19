/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.utils.MathUtils;
import com.asofterspace.toolbox.utils.Record;

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

		if (kind != null) {
			switch (kind) {
				case "glitch":
					List<Record> effects = new ArrayList<>();
					Map<Integer, Integer> effectOngoingUntil = new HashMap<>();
					int from = cgRoot.getInteger("from", 0);
					int to = cgRoot.getInteger("to", 0);
					int width = cgRoot.getInteger("width", 0);
					int height = cgRoot.getInteger("height", 0);
					int cur = from;
					int lastPrintAt = cur;
					while (cur < to) {
						Record r = Record.emptyObject();
						r.set("from", cur);
						int toFrame = cur + MathUtils.randomInteger(1024);
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

						int newEffectNum = MathUtils.randomInteger(20);
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
								r.set("untilX", right);
								r.set("untilY", bottom);
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

						if (MathUtils.randomInteger(5) < 3) {
							int len = toFrame - cur;
							r.set("fadeIn", MathUtils.randomInteger(len / 2));
						}
						if (MathUtils.randomInteger(5) < 3) {
							int len = toFrame - cur;
							r.set("fadeOut", MathUtils.randomInteger(len / 2));
						}

						effects.add(r);
						cur += MathUtils.randomInteger(8);

						if (lastPrintAt + 128 < cur) {
							System.out.println("At " + cur + " / " + to + "...");
							lastPrintAt = cur;
						}
					}
					Record configRec = templateRoot.get("config");
					configRec.set("effects", effects);
					JsonFile outFile = new JsonFile(targetFile);
					outFile.save(templateRoot);
					System.out.println("Saved output, based on " + templateFile.getCanonicalFilename() + ", to: " + outFile.getCanonicalFilename());
					return;
			}
		}

		System.out.println("Unknown config generation kind: " + kind);
	}
}
