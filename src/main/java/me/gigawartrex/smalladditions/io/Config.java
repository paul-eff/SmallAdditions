package me.gigawartrex.smalladditions.io;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Class to handle interactions with the plugin's config file.
 *
 * @author Paul Ferlitz
 */
public class Config extends FileHelper
{
    // Class variable
    private static final MessageHelper msghelp = new MessageHelper();

    /**
     * Class constructor.
     */
    public Config()
    {
        super("Config");
    }

    /**
     * Method to create a default config file.
     *
     * @param reset if the file should be reset to it's default
     */
    public void defaultConfig(boolean reset)
    {
        // Delete file in case of reset
        if (reset) deleteFile();

        if (checkForFile())
        {
            msghelp.sendConsole(getFileName() + " found and (re)loaded!", ChatColor.GREEN);
        } else
        {
            if (reset)
            {
                msghelp.sendConsole("Deleting " + getFileName() + " and creating default...", ChatColor.RED);
            } else
            {
                msghelp.sendConsole(getFileName() + " not found. Creating default...", ChatColor.RED);
            }

            createFile();

            //File defaults
            YamlConfiguration ymlFile = loadFile();
            ymlFile.set(getFileName() + ".Settings.maxLumberjackSize", "250");
            ymlFile.set(getFileName() + ".Settings.maxMinerSize", "250");
            ymlFile.set(getFileName() + ".Settings.serverPercentageSleepingForSkip", "0.25");
            for (String mod : Constants.modsList)
            {
                ymlFile.set(getFileName() + ".Settings.Mods." + mod, "true");
            }

            /*for (int level = 1; level <= (Constants.modsList.size() * 5); level++)
            {
                ymlFile.set(getFileName() + ".Leveling." + level, "" + (level * 100));
            }*/

            /*for (int level = 5; level <= (Constants.modsList.size() * 5); level += 5)
            {
                ymlFile.set(getFileName() + ".Leveling.Modlevel." + Constants.modsList.get((level / 5) - 1), "" + level);
            }*/

            saveFile(ymlFile);

            for (Player player : Constants.console.getServer().getOnlinePlayers())
            {
                if (read("Config.Players." + player.getUniqueId()).equals(""))
                {
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
                    //write(getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Level", "0");
                    //write(getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Book received?", "" + false);
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Ninjajoin", "" + false);
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Hide", "" + false);

                    //writePlayerStatus(player, false);
                    for (String mod : Constants.modsList)
                    {
                        writeModStatus(player, mod, false);
                    }
                }
            }

            msghelp.sendConsole("New " + getFileName() + " created!", ChatColor.GREEN);
        }
    }

    /**
     * Method to ensure a file exists.
     * Should be called before any serious file interaction.
     */
    private void doRuntimeCheck()
    {
        if (!checkForFile()) defaultConfig(false);
    }

    /**
     * Method to write a value to the Config file.
     *
     * @param path  the YAML path to the values location
     * @param value the value to be written
     */
    public void write(String path, String value)
    {
        doRuntimeCheck();

        YamlConfiguration ymlFile = loadFile();
        ymlFile.set(path, value);

        saveFile(ymlFile);
    }

    /**
     * Method to read a value from the Config file.
     * Defaults to an empty String if the player has no entry.
     *
     * @param path the YAML path to the value
     * @return the {@code String} value that was read
     */
    public String read(String path)
    {
        doRuntimeCheck();

        YamlConfiguration ymlFile = loadFile();
        if (ymlFile.get(path) != null) return ymlFile.getString(path);
        return "";
    }

    /**
     * Method to set if the player is using the mastering mode.
     *
     * @param player  the target {@code Player}
     * @param isUsing if the {@code Player} is using the mode
     */
    /*public void writePlayerStatus(Player player, Boolean isUsing)
    {
        write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
        write(getFileName() + ".Players." + player.getUniqueId() + ".Mastering on?", "" + isUsing);
    }*/

    /**
     * Method to read if the player is using the mastering mode.
     * Defaults to false if the player has no entry.
     *
     * @param player the target {@code Player}
     * @return {@code True} if the {@code Player} is using the mode
     */
    /*public boolean readPlayerStatus(Player player)
    {
        if (read(getFileName() + ".Players." + player.getUniqueId() + ".Mastering on?").equals(""))
        {
            write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
            write(getFileName() + ".Players." + player.getUniqueId() + ".Mastering on?", "" + false);
        }
        return Boolean.parseBoolean(read(getFileName() + ".Players." + player.getUniqueId() + ".Mastering on?"));
    }*/

    /**
     * Method to write a player's mod using status.
     *
     * @param player  the target {@code Player}
     * @param mod     the mod in question
     * @param isUsing {@code True} if the {@code Player} is using the mod
     */
    public void writeModStatus(Player player, String mod, Boolean isUsing)
    {
        write(getFileName() + ".Players." + player.getUniqueId() + ".Mods." + mod, "" + isUsing);
    }

    /**
     * Method to read a player's mod using status.
     * Defaults to false if the player has no entry.
     *
     * @param player the target {@code Player}
     * @param mod    the mod in question
     * @return {@code True} if the {@code Player} is using the mod
     */
    public boolean readModStatus(Player player, String mod)
    {
        if (read(getFileName() + ".Players." + player.getUniqueId() + ".Mods." + mod).equals(""))
        {
            write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
            write(getFileName() + ".Players." + player.getUniqueId() + ".Mods." + mod, "" + false);
        }
        return Boolean.parseBoolean(read(getFileName() + ".Players." + player.getUniqueId() + ".Mods." + mod));
    }
}
