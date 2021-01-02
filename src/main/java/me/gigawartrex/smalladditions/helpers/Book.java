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
 */
public class Book extends ItemStack
{
    // Class variables
    private final BookMeta meta;

    /**
     * Class constructor.
     *
     * @param fileName the name of the file containing the text for this book
     * @param author   the book's author
     * @param title    the book's title
     */
    public Book(String fileName, String author, String title)
    {
        super(Material.WRITTEN_BOOK);
        this.setAmount(1);
        String path = "./plugins/" + Constants.name + "/BookTextFiles/" + fileName + ".txt";
        meta = (BookMeta) this.getItemMeta();
        author = (author == null || author.equals("")) ? "Anonymous" : author;
        meta.setAuthor(author);
        meta.setTitle(title);
        meta.setPages(fileToPages(new File(path)));
        setItemMeta(meta);
    }

    /**
     * Method to convert a given file (e.g. TXT) to a Minecraft book.
     *
     * @param file the file to use
     * @return an {@code ArrayList<String>} holding each line of the book
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
            int uPointer = 0;
            // Points at lower end char of substring
            int lPointer;
            // For while loop
            boolean noSpace;

            // Build pages while string is left
            while (true)
            {
                noSpace = true;
                lPointer = uPointer;
                uPointer += 256;
                if (uPointer >= fullString.length())
                {
                    list.add(fullString.substring(lPointer));
                    break;
                }
                while (noSpace)
                {
                    if (fullString.charAt(uPointer - 1) != ' ')
                    {
                        uPointer--;
                    } else if (fullString.charAt(uPointer - 1) == '-' || fullString.charAt(uPointer - 1) == ' ')
                    {
                        list.add(fullString.substring(lPointer, uPointer));
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
     * @return the book's metadata
     */
    public BookMeta getMeta()
    {
        return this.meta;
    }

}