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

	private File file = null;

	private int frameNum = 0;

	private Image img = null;


	public VideoFrame(File baseOnFile) {
		this.file = baseOnFile;
	}

	public void init(int num) {
		this.frameNum = num;
		this.img = Picturizer.getImageFileCtrl().loadImageFromFile(this.file);
	}

	public void apply(List<VideoEffectContainer> effectContainers) {
		for (VideoEffectContainer effectContainer : effectContainers) {
			effectContainer.applyTo(frameNum, img);
		}
	}

	public void save(Directory targetDir) {
		File targetFile = new File(targetDir, StrUtils.leftPad0(frameNum, 8) + ".png");
		Picturizer.getImageFileCtrl().saveImageToFile(this.img, targetFile);
	}

	public void clear() {
		file = null;
		img = null;
	}
}
