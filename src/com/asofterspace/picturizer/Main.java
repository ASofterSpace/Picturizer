/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.Utils;

import javax.swing.SwingUtilities;


public class Main {

	public final static String PROGRAM_TITLE = "Picturizer";
	public final static String VERSION_NUMBER = "0.0.1.9(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "9. November 2019 - 31. December 2024";

	private static ConfigFile config;


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		String fileToOpen = null;

		if (args.length > 0) {
			if (args[0].equals("--version")) {
				System.out.println(Utils.getFullProgramIdentifierWithDate());
				return;
			}

			if (args[0].equals("--version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}

			// when several arguments are given, assume that it is just one file whose name contains spaces
			if (args.length > 1) {
				StringBuilder fileToOpenBuilder = new StringBuilder();
				String sep = "";
				for (int i = 0; i < args.length; i++) {
					fileToOpenBuilder.append(sep);
					fileToOpenBuilder.append(args[i]);
					sep = " ";
				}
				fileToOpen = fileToOpenBuilder.toString();
			} else {
				fileToOpen = args[0];
			}
		}

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
