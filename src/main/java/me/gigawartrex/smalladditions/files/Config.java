package me.gigawartrex.smalladditions.files;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config extends FileHelper {

    private static MessageHelper msghelp = new MessageHelper();

    public Config() {
        super("Config");
    }

    public void defaultConfig(boolean reset) {

        if (checkForFile() && reset) {
            deleteFile();
        }

        if (checkForFile()) {

            msghelp.sendConsole(getFileName() + " found and (re)loaded!", ChatColor.GREEN);
        } else {

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
            ymlFile.set(getFileName() + ".Settings.maxLumberjackSize", "25");
            ymlFile.set(getFileName() + ".Settings.maxMinerSize", "25");
            for (String mod : Constants.modsList)
            {
                ymlFile.set(getFileName() + ".Settings.Mods." + mod, "true");
            }

            for (int level = 1; level <= (Constants.modsList.size() * 5); level++)
            {
                ymlFile.set(getFileName() + ".Leveling." + level, "" + (level * 100));
            }

            for (int level = 5; level <= (Constants.modsList.size() * 5); level += 5)
            {
                ymlFile.set(getFileName() + ".Leveling.Modlevel." + Constants.modsList.get((level / 5) - 1), "" + level);
            }

            saveFile(ymlFile);

            if (reset) {
                for (Player player : Constants.console.getServer().getOnlinePlayers()) {

                    if (read("Config.Players." + player.getUniqueId()).equals("")) {

                        write(getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Level", "0");
                        write(getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
                        write(getFileName() + ".Players." + player.getUniqueId() + ".Book received?", "" + false);

                        writePlayerStatus(player, true);
                        for(String mod : Constants.modsList) {
                            writeModStatus(player, mod, false);
                        }
                    }
                }
            }

            msghelp.sendConsole("New " + getFileName() + " created!", ChatColor.GREEN);
        }
    }

    private void doRuntimeCheck() {
        if (!checkForFile()) {
            defaultConfig(false);
        }
    }

    public void write(String path, String value) {
        doRuntimeCheck();

        YamlConfiguration ymlFile = loadFile();
        ymlFile.set(path, value);

        saveFile(ymlFile);
    }

    public String read(String path) {
        doRuntimeCheck();

        YamlConfiguration ymlFile = loadFile();
        if (ymlFile.get(path) != null) {
            return ymlFile.getString(path);
        }
        return "";
    }

    public void writePlayerStatus(Player player, Boolean isUsing) {
        write(getFileName() + ".Players." + player.getUniqueId() + ".Name", player.getName());
        write(getFileName() + ".Players." + player.getUniqueId() + ".Mastering on?", "" + isUsing);
    }

    public void writeModStatus(Player player, String mod, Boolean isUsing) {
        write(getFileName() + ".Players." + player.getUniqueId() + ".Mods." + mod, "" + isUsing);
    }
}
