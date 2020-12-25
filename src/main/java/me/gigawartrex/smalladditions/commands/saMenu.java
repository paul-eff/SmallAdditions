package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class saMenu
{
    public static IconMenu createDefaultMenu(Player player)
    {
        MessageHelper msghelp = new MessageHelper();
        Config config = new Config();
        Leveling leveling = new Leveling(player);

        return new IconMenu("Mastering", 18, event -> {
            switch (event.getName())
            {
                case "Exit":
                    msghelp.sendPlayer(player, "Exiting...", ChatColor.RED);
                    break;
                case "Toggle":
                    if (config.readPlayerStatus(player))
                    {
                        config.writePlayerStatus(player, false);
                        msghelp.sendPlayer(player, "Mastering Mode turned off!", ChatColor.GOLD);
                    } else
                    {
                        config.writePlayerStatus(player, true);
                        msghelp.sendPlayer(player, "Mastering Mode turned on!", ChatColor.GOLD);
                    }
                    break;
                case "Status":
                    boolean isActive = Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Mastering on?"));

                    if (isActive)
                    {
                        msghelp.sendPlayer(player, "Mastering Mode: " + ChatColor.GREEN + "On", ChatColor.GOLD);
                    } else
                    {
                        msghelp.sendPlayer(player, "Mastering Mode: " + ChatColor.RED + "Off", ChatColor.GOLD);
                    }

                    msghelp.sendPlayer(player, "Your mods are set as the following:", ChatColor.GOLD);

                    for (String mod : Constants.modsList)
                    {
                        boolean status = Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Mods." + mod));
                        boolean statusServer = Boolean.parseBoolean(config.read("Config.Settings.Mods." + mod));
                        String statusServerString = "";

                        if (!statusServer)
                        {
                            statusServerString = ChatColor.RED + " (Off by Server)";
                        }

                        if (status)
                        {
                            msghelp.sendPlayer(player, mod + ": " + ChatColor.GREEN + "On" + statusServerString, ChatColor.GOLD);
                        } else
                        {
                            msghelp.sendPlayer(player, mod + ": " + ChatColor.RED + "Off", ChatColor.GOLD);
                        }
                    }

                    int level = leveling.getLevel();
                    int nextBlocks;
                    try
                    {
                        nextBlocks = Integer.parseInt(config.read(config.getFileName() + ".Leveling." + (level + 1)));
                    } catch (NumberFormatException e)
                    {
                        nextBlocks = -1;
                    }
                    msghelp.sendPlayer(player, "Level: " + level + " | Blocks: " + leveling.getBlocks(), ChatColor.GREEN);
                    if (nextBlocks == -1)
                    {
                        msghelp.sendPlayer(player, "Max level reached!", ChatColor.GREEN);
                    } else
                    {
                        msghelp.sendPlayer(player, "Blocks needed for next level: " + nextBlocks, ChatColor.GREEN);
                    }
                    break;
                case "Magnet":
                    if (Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Magnet")))
                    {
                        config.write("Config.Players." + player.getUniqueId() + ".Magnet", "" + false);
                        msghelp.sendPlayer(player, "Magnet turned off!", ChatColor.GOLD);
                    } else
                    {
                        config.write("Config.Players." + player.getUniqueId() + ".Magnet", "" + true);
                        msghelp.sendPlayer(player, "Magnet turned on!", ChatColor.GOLD);
                    }
                    break;
                default:
                    String modName = event.getName();
                    if (Constants.modsList.contains(modName))
                    {
                        if (!config.readModStatus(player, modName))
                        {
                            int levelNeeded = Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + modName));
                            int currentLevel = leveling.getLevel();

                            if (currentLevel >= levelNeeded)
                            {
                                config.writeModStatus(player, modName, true);
                                msghelp.sendPlayer(player, modName + " Mod was activated", ChatColor.GOLD);
                            } else
                            {
                                msghelp.sendPlayer(player, "Your level is to low. (Level " + levelNeeded + " needed)", ChatColor.RED);
                            }
                        } else
                        {
                            config.writeModStatus(player, modName, false);
                            msghelp.sendPlayer(player, modName + " Mod was deactivated", ChatColor.GOLD);
                        }
                    } else
                    {
                        msghelp.sendPlayer(player, "Error occurred! (" + modName + ")", ChatColor.RED);
                    }
            }
            event.setWillClose(true);
            event.setWillDestroy(true);
        }, Constants.plugin)
                .setOption(0, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                .setOption(1, new ItemStack(Material.REDSTONE_BLOCK, 1), "Toggle", "Turn mastering on/off.")
                .setOption(3, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.")
                .setOption(4, new ItemStack(Material.FURNACE, 1), "Autosmelt", "Directly smelt items that were harvested/mined.")
                .setOption(5, new ItemStack(Material.DIAMOND, 1), "Fortune", "Get a luck multiplier on all actions.")
                .setOption(8, new ItemStack(Material.BOOK, 1), "Status", "Show current status.")
                .setOption(10, new ItemStack(Material.IRON_BLOCK, 1), "Magnet", "(WIP!!!) Turn magnet on/off.");
    }
}
