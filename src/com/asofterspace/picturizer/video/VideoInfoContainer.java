/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.video;

import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;


public class VideoInfoContainer {

	private Record initFromRec = null;


	public VideoInfoContainer(Record rec) {
		this.initFromRec = rec;
	}

	public List<VideoFrame> getFrames() {
		List<VideoFrame> frames = new ArrayList<>();

		// for all files in this directory, ordered alphabetically...
		String dirStr = initFromRec.getString("dir");
		if (dirStr != null) {
			Directory dir = new Directory(dirStr);
			boolean recursively = false;
			List<File> frameFiles = dir.getAllFiles(recursively);
			frameFiles = SortUtils.sortAlphabetically(frameFiles);
			boolean started = false;
			// ... take all starting with filename "from" (if that field exists, or from first) ...
			String from = initFromRec.getString("from");
			// ... until the one with filename "to" (if that field exists, or until end)
			String to = initFromRec.getString("to");
			if (from == null) {
				started = true;
			}
			for (File file : frameFiles) {
				if (!started) {
					if (from.equals(file.getLocalFilename())) {
						started = true;
					}
				}
				if (started) {
					frames.add(new VideoFrame(file));
				}
				if (to != null) {
					if (to.equals(file.getLocalFilename())) {
						break;
					}
				}
			}
		}

		return frames;
	}
}
