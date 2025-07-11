/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.io.IoUtils;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.Utils;

import javax.swing.SwingUtilities;


public class Picturizer {

	public final static String PROGRAM_TITLE = "Picturizer";
	public final static String VERSION_NUMBER = "0.0.2.9(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "9. November 2019 - 10. July 2025";

	private static ConfigFile config;


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		if (args.length > 0) {
			if (args[0].equals("--version")) {
				System.out.println(Utils.getFullProgramIdentifierWithDate());
				return;
			}

			if (args[0].equals("--version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}
		}

		String fileToOpen = IoUtils.assembleArgumentsIntoOne(args);

		if (fileToOpen == null) {
			System.out.println("Pictures, wheee! \\o/");
		} else {
			System.out.println("Playing with '" + fileToOpen + "'...");
		}

		// load config
		boolean onlyUseDefaultIfBroken = true;
		config = new ConfigFile("settings", true, Record.emptyObject(), onlyUseDefaultIfBroken);

		SwingUtilities.invokeLater(new GUI(config, fileToOpen));
	}

}
