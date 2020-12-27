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

				writer.write("-- WIP -- \n");
				writer.write("This book was not filled with content yet. \n");
				writer.write("  -SimpleAdditions Team \n");
				writer.write("-- WIP -- \n");

			} catch (IOException e) {
				e.printStackTrace();
			}

			msghelp.sendConsole("New " + fileName + " created!", ChatColor.GREEN);
		}

	}
}
