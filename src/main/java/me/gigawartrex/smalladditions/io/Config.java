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
    private static final Double configVersion = 1.0;
    private static final MessageHelper msghelp = new MessageHelper();

    /**
     * Class constructor.
     */
    public Config()
    {
        super("Config");
    }

    /**
     * Method to do a version check on the configuration file.
     */
    public void doVersionCheck()
    {
        String actualConfigVersion = read("Version").equals("") ? "N/A" : read("Version");
        if (actualConfigVersion.equals("N/A") || !actualConfigVersion.contains(configVersion.toString()))
        {
            msghelp.sendConsole(getFileName() + " mismatch! (Found version " + actualConfigVersion + " but needed " + configVersion + ")", ChatColor.RED);
            msghelp.sendConsole("Please type \"/sa resetall\" to generate a compatible version.", ChatColor.RED);
            msghelp.sendConsole("Not doing this may cause bugs or limit functionality!", ChatColor.RED);
        }
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
            ymlFile.set("Version", configVersion);
            ymlFile.set(getFileName() + ".Settings.maxLumberjackSize", "250");
            ymlFile.set(getFileName() + ".Settings.maxMinerSize", "250");
            ymlFile.set(getFileName() + ".Settings.serverPercentageSleepingForSkip", "0.25");
            ymlFile.set(getFileName() + ".Settings.doEndermanGriefing", "true");
            ymlFile.set(getFileName() + ".Settings.doCreeperGriefing", "true");
            ymlFile.set(getFileName() + ".Settings.doGhastGriefing", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.rawOreBlockSmelting", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.buddingAmethystCraftable", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.woolRecoloring", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.lightBlockCraftable", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.stonecutterStoneConversion", "true");
            ymlFile.set(getFileName() + ".Settings.Recipes.stonecutterUnsmoothRecipe", "true");
            for (String mod : Constants.modsList)
            {
                ymlFile.set(getFileName() + ".Settings.Mods." + mod, "true");
            }

            saveFile(ymlFile);

            for (Player player : Constants.console.getServer().getOnlinePlayers())
            {
                if (read("Config.Players." + player.getUniqueId()).equals(""))
                {
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Book received", "" + false);
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Ninjajoin", "" + false);
                    write(getFileName() + ".Players." + player.getUniqueId() + ".Hide", "" + false);

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
     * Method to write a player's mod using status.
     *
     * @param player  the target {@code Player}
     * @param mod     the mod in question
     * @param isUsing {@code True} if the {@code Player} is using the mod
     */
    public void writeModStatus(Player player, String mod, boolean isUsing)
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

    /**
     * Method to write a player's attribute using status.
     *
     * @param player    the target {@code Player}
     * @param attribute the attribute in question
     * @param isUsing   {@code True} if the attribute is active for the {@code Player}
     */
    public void writePlayerAttributeStatus(Player player, String attribute, boolean isUsing)
    {
        write(getFileName() + ".Players." + player.getUniqueId() + "." + attribute, "" + isUsing);
    }

    /**
     * Method to read a player's attribute using status.
     * Defaults to false if the player has no entry.
     *
     * @param player    the target {@code Player}
     * @param attribute the attribute in question
     * @return {@code True} if the attribute is active for the {@code Player}
     */
    public boolean readPlayerAttributeStatus(Player player, String attribute)
    {
        if (read(getFileName() + ".Players." + player.getUniqueId() + "." + attribute).equals(""))
        {
            write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
            write(getFileName() + ".Players." + player.getUniqueId() + "." + attribute, "" + false);
        }
        return Boolean.parseBoolean(read(getFileName() + ".Players." + player.getUniqueId() + "." + attribute));
    }
}
