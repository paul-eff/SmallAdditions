package me.gigawartrex.smalladditions.io;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BookWriter {

	private final MessageHelper msghelp = new MessageHelper();

	public BookWriter() {

		File directory = new File("./plugins/" + Constants.name + "/BookTextFiles/");

		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public void defaultBook(String fileName, boolean reset) {

		File file = new File("./plugins/" + Constants.name + "/BookTextFiles/" + fileName + ".txt");

		if (file.exists() && reset) {
			file.delete();
		}

		if (file.exists()) {

			msghelp.sendConsole(fileName + " found and (re)loaded!", ChatColor.GREEN);
		} else {

			if (reset) {
				msghelp.sendConsole("Deleting " + fileName + " and creating default...", ChatColor.RED);
			} else {
				msghelp.sendConsole(fileName + " not found. Creating default...", ChatColor.RED);
			}

			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

				writer.write("-- ERROR -- \n");
				writer.write("File for this Book not found in Plugin Folder. \n");
				writer.write("Please let a System Admin check in \n");
				writer.write("<Server Home Folder>/plugins/SmallAdditions/BookTextFiles/ \n");
				writer.write("-- ERROR -- \n");

			} catch (IOException e) {
				e.printStackTrace();
			}

			msghelp.sendConsole("New " + fileName + " created!", ChatColor.GREEN);
		}

	}
}
