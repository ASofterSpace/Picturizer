/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;

import com.asofterspace.picturizer.commandline.CommandLineHandler;
import com.asofterspace.picturizer.gui.GUI;
import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.images.ImageFileCtrl;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.IoUtils;
import com.asofterspace.toolbox.pdf.PdfImageHandler;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.Utils;

import javax.swing.SwingUtilities;


public class Picturizer {

	public final static String PROGRAM_TITLE = "Picturizer";
	public final static String VERSION_NUMBER = "0.0.3.5(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "9. November 2019 - 25. July 2025";

	private static ConfigFile config;

	private static ImageFileCtrl imageFileCtrl;


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

		// we want to handle as many image formats as we can...
		// even opening images from PDF files, if possible :)
		imageFileCtrl = new ImageFileCtrl();
		imageFileCtrl.addHandler(new PdfImageHandler());

		String fileToOpen = IoUtils.assembleArgumentsIntoOne(args);

		// load config
		boolean onlyUseDefaultIfBroken = true;
		config = new ConfigFile("settings", true, Record.emptyObject(), onlyUseDefaultIfBroken);

		if (fileToOpen == null) {
			System.out.println("Starting GUI to play with pictures, wheee! \\o/");
		} else {
			if (fileToOpen.toLowerCase().endsWith(".json")) {
				System.out.println("Starting in commandline-mode with instruction file '" + fileToOpen + "'...");
				CommandLineHandler clHandler = new CommandLineHandler(new File(fileToOpen));
				clHandler.run();
				return;
			} else {
				System.out.println("Starting GUI to play with input file '" + fileToOpen + "'...");
			}
		}

		SwingUtilities.invokeLater(new GUI(config, fileToOpen));
	}

	public static ImageFileCtrl getImageFileCtrl() {
		return imageFileCtrl;
	}

}
