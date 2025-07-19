/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer.commandline;

import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.utils.Record;


public class CommandLineHandler {

	private Record inputRoot = null;


	public CommandLineHandler(File inputFile) {
		if (!inputFile.exists()) {
			System.out.println("Input file does not exist!");
			return;
		}
		JsonFile jsonInputFile = new JsonFile(inputFile);
		try {
			inputRoot = jsonInputFile.getAllContents();
		} catch (JsonParseException e) {
			System.out.println("Input file JSON parse exception: " + e);
		}
	}

	public void run() {
		if (inputRoot == null) {
			return;
		}

		String type = inputRoot.getString("type");
		if (type == null) {
			System.out.println("No type given!");
			return;
		}

		switch (type) {
			case "video":
				VideoHandler vh = new VideoHandler(inputRoot.get("config"));
				vh.run();
				break;
			case "configGenerator":
				ConfigGenerationHandler cgh = new ConfigGenerationHandler(inputRoot.get("config"));
				cgh.run();
				break;
			default:
				System.out.println("Type '" + type + "' unknown!");
				break;
		}
	}
}
