/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.toolbox.utils.Record;

import java.util.ArrayList;
import java.util.List;


public class VideoHandler {

	private Record videoRoot = null;


	public CommandLineHandler(Record videoRootRec) {
		this.videoRoot = videoRootRec;
	}

	public void run() {
		if (videoRoot == null) {
			System.out.println("config missing!");
			return;
		}

		List<VideoFrame> frames = new ArrayList<>();

		List<Record> frameRecs = videoRoot.getArray("frames");
		for (Record frameRec : frameRecs) {
			frames.add(new VideoFrame(frameRec));
		}
	}
}
