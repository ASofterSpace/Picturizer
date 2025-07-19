/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.picturizer.video.VideoEffectContainer;
import com.asofterspace.picturizer.video.VideoFrame;
import com.asofterspace.picturizer.video.VideoInfoContainer;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.utils.Record;

import java.util.ArrayList;
import java.util.List;


public class VideoHandler {

	private Record videoRoot = null;


	public VideoHandler(Record videoRootRec) {
		this.videoRoot = videoRootRec;
	}

	public void run() {
		if (videoRoot == null) {
			System.out.println("config missing!");
			return;
		}

		// to convert videos into frames in the first place, use:
		// ffmpeg -i input.mp4 "frames/%06d.png"
		// (for which the "frames" target dir has to exist)
		//
		// regarding frame amount:
		// if we e.g. have 25778 frames for a video that is
		// 1030 seconds long, then to get from a timestamp
		// to frame number we just have to use:
		// 25778 / 1030 = 25.027184466019417
		// so:
		// ((60 * MM) + SS) * 25.027

		List<VideoInfoContainer> baseContainers = new ArrayList<>();

		List<Record> recs = videoRoot.getArray("base");
		for (Record r : recs) {
			baseContainers.add(new VideoInfoContainer(r));
		}

		List<VideoEffectContainer> effectContainers = new ArrayList<>();

		recs = videoRoot.getArray("effects");
		for (Record r : recs) {
			effectContainers.add(new VideoEffectContainer(r));
		}

		List<VideoFrame> frames = convertListOfContainersToFrames(baseContainers);

		Directory targetDir = new Directory(videoRoot.getString("targetDir"));

		applyEffects(frames, effectContainers, targetDir);
	}

	private List<VideoFrame> convertListOfContainersToFrames(List<VideoInfoContainer> infoContainers) {
		List<VideoFrame> frames = new ArrayList<>();

		for (VideoInfoContainer infoContainer : infoContainers) {
			frames.addAll(infoContainer.getFrames());
		}

		return frames;
	}

	private void applyEffects(List<VideoFrame> frames, List<VideoEffectContainer> effectContainers, Directory targetDir) {

		targetDir.create();

		int num = 0;

		for (VideoFrame frame : frames) {
			if (num % 64 == 0) {
				System.out.println("Working on frame " + num + "...");
			}
			frame.init(num);
			frame.apply(effectContainers);
			frame.save(targetDir);
			frame.clear();
			num++;
		}
	}
}
