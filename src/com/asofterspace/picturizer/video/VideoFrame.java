/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.video;

import com.asofterspace.picturizer.Picturizer;
import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.utils.StrUtils;

import java.util.List;


public class VideoFrame {

	private File inputFile = null;
	private File outputFile = null;

	private int frameNum = 0;

	private Image img = null;


	public VideoFrame(File inputFile) {
		this.inputFile = inputFile;
	}

	public void init(int num, Directory targetDir) {
		this.frameNum = num;
		this.outputFile = new File(targetDir, StrUtils.leftPad0(frameNum, 8) + ".png");
	}

	public void load() {
		this.img = Picturizer.getImageFileCtrl().loadImageFromFile(this.inputFile);
	}

	public void apply(List<VideoEffectContainer> effectContainers) {
		for (VideoEffectContainer effectContainer : effectContainers) {
			effectContainer.applyTo(frameNum, img);
		}
	}

	public void save() {
		Picturizer.getImageFileCtrl().saveImageToFile(this.img, outputFile);
	}

	public boolean alreadyExists() {
		return outputFile.exists();
	}

	public void clear() {
		this.inputFile = null;
		this.outputFile = null;
		this.img = null;
	}
}
