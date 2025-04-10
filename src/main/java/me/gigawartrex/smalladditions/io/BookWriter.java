package me.gigawartrex.smalladditions.io;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for handling writing files for a specified book.
 *
 * @author Paul Ferlitz
 */
public class BookWriter
{
    // Class variables
    private final MessageHelper msghelp = new MessageHelper();

    /**
     * Class constructor.
     */
    public BookWriter()
    {
        File directory = new File("./plugins/" + Constants.name + "/BookTextFiles/");

        if (!directory.exists()) directory.mkdirs();
    }

    /**
     * Method to check for a default book file, or reset it.
     *
     * @param fileName the books source file name
     * @param reset    if the source file should be reset to it's default
     */
    public void defaultBook(String fileName, boolean reset)
    {
        File file = new File("./plugins/" + Constants.name + "/BookTextFiles/" + fileName + ".txt");

        // Delete file in case of reset
        if (file.exists() && reset) file.delete();

        if (file.exists())
        {
            msghelp.sendConsole(fileName + " found and (re)loaded!", ChatColor.GREEN);
        } else
        {
            if (reset)
            {
                msghelp.sendConsole("Deleting " + fileName + " and creating default...", ChatColor.RED);
            } else
            {
                msghelp.sendConsole(fileName + " not found. Creating default...", ChatColor.RED);
            }
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            // Write default to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
            {
                writer.write("Thanks for downloading SmallAdditions!         \n");
                writer.write("Type \"/help SmallAdditions\" for in depth information, or just \"/sa menu\" to get right into it! \n");
                writer.write("-SmallAdditions Team \n");
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            msghelp.sendConsole("New " + fileName + " created!", ChatColor.GREEN);
        }
    }
}
