package me.gigawartrex.smalladditions.files;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

abstract class FileHelper {

    private String fileName = ""; // Layout: "fileName"
    private String basePath = "plugins/" + Constants.name + "/";
    private String path = ""; // Layout: "folder1/folder2/"

    /**
     * First Constructor to create one file in the base Plugin directory
     *
     * @param fileName Name of the file without a file suffix
     */
    public FileHelper(String fileName) {
        this.fileName = fileName;

        // Create Base Directory
        File file = new File(this.basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Second Constructor to create one file in a custom directory
     *
     * @param path     Path of the file
     * @param fileName Name of the file without a file suffix
     */
    public FileHelper(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;

        // Create Base Directory
        File file = new File(this.basePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        // Create Directory if specified
        if (!this.path.equals("") && !this.path.equals(" ")) {
            file = new File(this.basePath + this.path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * Method to return the file name
     *
     * @return The file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Method to return a file
     *
     * @return File associated with this object
     */
    public YamlConfiguration loadFile() {
        YamlConfiguration ymlFile = new YamlConfiguration();
        try {
            ymlFile.load(this.basePath + this.path + this.fileName + ".yml");
            return ymlFile;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to save a file
     *
     * @param file The file which should be saved
     * @return If the operation was successful or not
     */
    public boolean saveFile(YamlConfiguration file) {
        YamlConfiguration ymlFile = file;
        try {
            ymlFile.save(this.basePath + this.path + this.fileName + ".yml");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check for a file's existence
     *
     * @return If file exists
     */
    public boolean checkForFile() {
        File file = new File(this.basePath + this.path + this.fileName + ".yml");
        return file.exists();
    }

    /**
     * Method to delete a file
     *
     * @return If the operation was successful
     */
    public boolean deleteFile() {
        if (checkForFile()) {
            File file = new File(this.basePath + this.path + this.fileName + ".yml");
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * Method to create a file
     *
     * @return If operation was successful
     */
    public boolean createFile() {
        if (!checkForFile()) {
            if (!this.fileName.equals("")) {
                File file = new File(this.basePath + this.path + this.fileName + ".yml");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    throw new NoFileNameException();
                } catch (NoFileNameException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
