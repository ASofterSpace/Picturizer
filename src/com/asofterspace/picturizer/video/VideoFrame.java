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

	private boolean changedByEffect = false;


	public VideoFrame(File inputFile) {
		this.inputFile = inputFile;
	}

	public void init(int num, Directory targetDir) {
		this.frameNum = num;
		this.outputFile = new File(targetDir, StrUtils.leftPad0(frameNum, 8) + ".png");
	}

	public Image getImage() {
		if (this.img == null) {
			this.img = Picturizer.getImageFileCtrl().loadImageFromFile(this.inputFile);
		}
		return this.img;
	}

	public void apply(List<VideoEffectContainer> effectContainers) {
		for (VideoEffectContainer effectContainer : effectContainers) {
			boolean didChange = effectContainer.applyTo(frameNum, this);
			if (didChange) {
				changedByEffect = true;
			}
		}
	}

	public boolean wasChangedByEffect() {
		return changedByEffect;
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
