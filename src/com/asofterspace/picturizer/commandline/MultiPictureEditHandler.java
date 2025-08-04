/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.picturizer.Picturizer;
import com.asofterspace.toolbox.images.ColorRGBA;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;


public class MultiPictureEditHandler {

	private Record mpeRoot = null;


	public MultiPictureEditHandler(Record mpeRootRec) {
		this.mpeRoot = mpeRootRec;
	}

	public void run() {
		if (mpeRoot == null) {
			System.out.println("config missing!");
			return;
		}

		List<String> inputFiles = mpeRoot.getArrayAsStringList("input");

		Directory outputDir = new Directory(mpeRoot.getString("outputDir"));
		outputDir.create();

		String outputName = mpeRoot.getString("outputName");
		int outputNum = mpeRoot.getInteger("outputNumStartAt", 1);

		for (String inputFile : inputFiles) {
			// will output usually one image per input file (but possibly more, if some are split into multiples)
			List<Image> images = applyActionsToFile(inputFile);

			for (Image img : images) {
				String curOutputName = outputName;
				curOutputName = StrUtils.replaceAll(curOutputName, "%NUM%", ""+outputNum);
				File outputFile = new File(outputDir, curOutputName);
				Picturizer.getImageFileCtrl().saveImageToFile(img, outputFile);
				outputNum++;
			}
		}
	}

	private List<Image> applyActionsToFile(String inputFileName) {
		List<Image> result = new ArrayList<>();
		result.add(Picturizer.getImageFileCtrl().loadImageFromFile(new File(inputFileName)));

		List<String> actions = mpeRoot.getArrayAsStringList("actions");
		for (String action : actions) {
			switch (action) {
				case "turnLeft":
					for (Image img : result) {
						img.rotateLeft();
					}
					break;
				case "turnRight":
					for (Image img : result) {
						img.rotateRight();
					}
					break;
				case "splitLeftRight":
					List<Image> newRes = new ArrayList<>();
					for (Image img : result) {
						Image splitImg = img.copy();
						splitImg.expandRightBy(- (splitImg.getWidth() / 2), ColorRGBA.WHITE);
						newRes.add(splitImg);
						splitImg = img;
						splitImg.expandLeftBy(- (splitImg.getWidth() / 2), ColorRGBA.WHITE);
						newRes.add(splitImg);
					}
					result = newRes;
					break;
				default:
					break;
			}
		}

		return result;
	}

}
