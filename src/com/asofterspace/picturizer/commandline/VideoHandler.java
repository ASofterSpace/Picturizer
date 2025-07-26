/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.picturizer.video.VideoEffectContainer;
import com.asofterspace.picturizer.video.VideoFrame;
import com.asofterspace.picturizer.video.VideoInfoContainer;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
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

		// List<String> framesOut = new ArrayList<>();
		// for (VideoFrame frame : frames) {
			// framesOut.add(frame.toString());
		// }
		// SimpleFile framesOutFile = new SimpleFile("framesOutFile.txt");
		// framesOutFile.saveContents(framesOut);

		Directory targetDir = new Directory(videoRoot.getString("targetDir"));

		File stopFile = null;
		String stopFileName = videoRoot.getString("stopFile");
		if (stopFileName != null) {
			stopFile = new File(stopFileName);
		}

		boolean onlySaveChanged = videoRoot.getBoolean("onlySaveChanged", false);
		int targetFileNameDigits = videoRoot.getInteger("targetFileNameDigits", 8);

		applyEffects(frames, effectContainers, targetDir, stopFile, onlySaveChanged, targetFileNameDigits);
	}

	private List<VideoFrame> convertListOfContainersToFrames(List<VideoInfoContainer> infoContainers) {
		List<VideoFrame> frames = new ArrayList<>();

		for (VideoInfoContainer infoContainer : infoContainers) {
			frames.addAll(infoContainer.getFrames());
		}

		return frames;
	}

	private void applyEffects(List<VideoFrame> frames, List<VideoEffectContainer> effectContainers, Directory targetDir, File stopFile,
		boolean onlySaveChanged, int targetFileNameDigits) {

		targetDir.create();

		int num = 0;

		VideoFrame lastVidFrame = null;

		for (VideoFrame frame : frames) {
			if (num % 64 == 0) {
				System.out.println("Working on frame " + num + "...");
			}
			frame.init(num, targetDir, targetFileNameDigits);
			if (!frame.alreadyExists()) {
				frame.apply(effectContainers, lastVidFrame);
				if ((!onlySaveChanged) || frame.wasChangedByEffect()) {
					frame.save();
				}
			}
			if (lastVidFrame != null) {
				lastVidFrame.clear();
			}
			lastVidFrame = frame;
			num++;
			if (stopFile != null) {
				if (stopFile.exists()) {
					System.out.println("Stopping, as " + stopFile.getCanonicalFilename() + " exists, and deleting the stop file again...");
					System.out.println("(Feel free to simply restart to start from where this run left off!)");
					stopFile.delete();
					break;
				}
			}
		}
	}
}
