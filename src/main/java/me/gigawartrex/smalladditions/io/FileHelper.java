package me.gigawartrex.smalladditions.io;

import me.gigawartrex.smalladditions.exceptions.NoFileNameException;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Class for handling general file interactions.
 *
 * @author Paul Ferlitz
 */
public abstract class FileHelper
{
    // Class variables
    private String fileName; // Layout: "fileName"
    private final String basePath = "plugins/" + Constants.name + "/";
    private String path = ""; // Layout: "folder1/folder2/"

    /**
     * Constructor to create one file in the base plugin directory.
     *
     * @param fileName the name of the file without a file suffix
     */
    public FileHelper(String fileName)
    {
        this.fileName = fileName;

        // Create Base Directory
        File file = new File(this.basePath);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }

    /**
     * Constructor to create one file in a custom directory.
     *
     * @param path     the path of the file inside the plugin's base directory
     * @param fileName the name of the file without a file suffix
     */
    public FileHelper(String path, String fileName)
    {
        this.path = path;
        this.fileName = fileName;

        // Create Base Directory
        File file = new File(this.basePath);
        if (!file.exists()) file.mkdirs();

        // Create Directory if specified
        if (!this.path.equals("") && !this.path.equals(" "))
        {
            file = new File(this.basePath + this.path);
            if (!file.exists()) file.mkdirs();
        }
    }

    /**
     * Method to return the file name.
     *
     * @return the file name
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * Method to return a file.
     *
     * @return the file associated with this object
     */
    public YamlConfiguration loadFile()
    {
        YamlConfiguration ymlFile = new YamlConfiguration();
        try
        {
            ymlFile.load(this.basePath + this.path + this.fileName + ".yml");
            return ymlFile;
        } catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to save a file.
     *
     * @param file the file which should be saved
     * @return {@code True} if the operation was successful
     */
    public boolean saveFile(YamlConfiguration file)
    {
        try
        {
            file.save(this.basePath + this.path + this.fileName + ".yml");
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check for a file's existence.
     *
     * @return {@code True} if the file exists
     */
    public boolean checkForFile()
    {
        File file = new File(this.basePath + this.path + this.fileName + ".yml");
        return file.exists();
    }

    /**
     * Method to delete a file.
     *
     * @return {@code True} if the operation was successful
     */
    public boolean deleteFile()
    {
        if (checkForFile())
        {
            File file = new File(this.basePath + this.path + this.fileName + ".yml");
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * Method to create a file.
     *
     * @return {@code True} if operation was successful
     */
    public boolean createFile()
    {
        if (!checkForFile())
        {
            if (!this.fileName.equals(""))
            {
                File file = new File(this.basePath + this.path + this.fileName + ".yml");
                if (!file.exists())
                {
                    try
                    {
                        file.createNewFile();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            } else
            {
                try
                {
                    throw new NoFileNameException();
                } catch (NoFileNameException e)
                {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
