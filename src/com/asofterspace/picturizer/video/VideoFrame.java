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

	private boolean thisIsFirstFrameOfSequence = false;
	private File inputFile = null;
	private File outputFile = null;
	private int locked = 0;

	private int frameNum = 0;

	private Image img = null;

	private boolean changedByEffect = false;


	public VideoFrame(File inputFile, boolean isFirstFrameOfSequence) {
		this.inputFile = inputFile;
		this.thisIsFirstFrameOfSequence = isFirstFrameOfSequence;
	}

	public void init(int num, Directory targetDir, int targetFileNameDigits) {
		this.frameNum = num;
		this.outputFile = new File(targetDir, StrUtils.leftPad0(frameNum, targetFileNameDigits) + ".png");
	}

	public Image getImage() {
		if (this.img == null) {
			this.img = Picturizer.getImageFileCtrl().loadImageFromFile(this.inputFile);
		}
		return this.img;
	}

	public void apply(List<VideoEffectContainer> effectContainers, VideoFrame lastVidFrame) {
		for (VideoEffectContainer effectContainer : effectContainers) {
			boolean didChange = effectContainer.applyTo(frameNum, this, lastVidFrame);
			if (didChange) {
				changedByEffect = true;
			}
		}
	}

	public boolean wasChangedByEffect() {
		return changedByEffect;
	}

	public void save() {
		// shortcut: if the picture wasn't even loaded...
		if (this.img == null) {
			// just copy it instead of loading and saving it!
			this.inputFile.copyToDisk(outputFile);
		} else {
			Picturizer.getImageFileCtrl().saveImageToFile(this.img, outputFile);
		}
	}

	public boolean alreadyExists() {
		return outputFile.exists();
	}

	public void lock() {
		this.locked++;
	}

	public void unlock() {
		this.locked--;
	}

	public void clear() {
		if (this.locked > 0) {
			return;
		}
		// do not clear the input file so that the VideoFrame can be re-initialized for a looped video
		// this.inputFile = null;
		this.outputFile = null;
		this.img = null;
	}

	public boolean isFirstFrameOfSequence() {
		return thisIsFirstFrameOfSequence;
	}

	@Override
	public String toString() {
		return "VideoFrame [inputFile: " + this.inputFile + "]";
	}

}
