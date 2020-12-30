package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.*;
import java.util.ArrayList;

/**
 * Class for handling "/sa" commands.
 *
 * @author Paul Ferlitz
 * @version 1.0 2020-12-28 Initial Version
 * @since 1.0
 */
public class Book extends ItemStack
{
    // Class variables
    private final BookMeta meta;

    /**
     * Class constructor.
     *
     * @param fileName Name of the file containing the text for this book. NO trailing file type (e.g. ".txt")!
     * @param author   The book's author.
     * @param title    The book's title.
     * @since 1.0
     */
    public Book(String fileName, String author, String title)
    {
        super(Material.WRITTEN_BOOK);
        this.setAmount(1);
        String path = "./plugins/" + Constants.name + "/BookTextFiles/" + fileName + ".txt";
        meta = (BookMeta) this.getItemMeta();
        meta.setAuthor(author);
        meta.setTitle(title);
        meta.setPages(fileToPages(new File(path)));
        setItemMeta(meta);
    }

    /**
     * Method to convert a given file (e.g. TXT) to a Minecraft book.
     *
     * @param file The file to use.
     * @return An ArrayList of strings representing each line in the book.
     * @since 1.0
     */
    private ArrayList<String> fileToPages(File file)
    {
        // Method variables
        ArrayList<String> list = new ArrayList<>();
        StringBuilder fullString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line = br.readLine();
            // Read in whole file
            while (line != null)
            {
                fullString.append(line);
                line = br.readLine();
            }

            // Points at upper end char of substring
            int pointer = 0;
            // Points at lower end char of substring
            int newpointer;
            // For while loop
            boolean noSpace;

            // Build pages while string is left
            while (true)
            {
                noSpace = true;
                newpointer = pointer;
                pointer += 256;
                if (pointer >= fullString.length())
                {
                    list.add(fullString.substring(newpointer));
                    break;
                }
                while (noSpace)
                {
                    if (fullString.charAt(pointer - 1) != ' ')
                    {
                        pointer--;
                    } else if (fullString.charAt(pointer - 1) == '-' || fullString.charAt(pointer - 1) == ' ')
                    {
                        list.add(fullString.substring(newpointer, pointer));
                        noSpace = false;
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Method to get the book's metadata.
     *
     * @return The book's metadata.
     * @since 1.0
     */
    public BookMeta getMeta()
    {
        return this.meta;
    }

}