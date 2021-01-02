package me.gigawartrex.smalladditions.iconmenus;

import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Class to create the standard README book.
 *
 * @author Paul Ferlitz
 */
public class MainMenu extends MenuTemplate
{
    /**
     * Main method to generate the book object.
     *
     * @return the {@link IconMenu} as configured
     */
    public IconMenu generateMenu()
    {
        // Setup
        return new IconMenu("Mastering", 18, event -> {
            Leveling leveling = new Leveling(event.getPlayer());

            // Switch for command executed/item clicked
            switch (event.getName())
            {
                case "Exit":
                    break;
                case "Toggle":
                    if (getConfig().readPlayerStatus(event.getPlayer()))
                    {
                        getConfig().writePlayerStatus(event.getPlayer(), false);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode turned off!", ChatColor.GOLD);
                    } else
                    {
                        getConfig().writePlayerStatus(event.getPlayer(), true);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode turned on!", ChatColor.GOLD);
                    }
                    break;
                case "Status":
                    boolean isActive = Boolean.parseBoolean(getConfig().read("Config.Players." + event.getPlayer().getUniqueId() + ".Mastering on?"));

                    if (isActive)
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode: " + ChatColor.GREEN + "On", ChatColor.GOLD);
                    } else
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode: " + ChatColor.RED + "Off", ChatColor.GOLD);
                    }

                    getMessageHelper().sendPlayer(event.getPlayer(), "Your mods are set as the following:", ChatColor.GOLD);

                    for (String mod : Constants.modsList)
                    {
                        boolean status = Boolean.parseBoolean(getConfig().read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods." + mod));
                        boolean statusServer = Boolean.parseBoolean(getConfig().read("Config.Settings.Mods." + mod));
                        String statusServerString = "";

                        if (!statusServer)
                        {
                            statusServerString = ChatColor.RED + " (Off by Server)";
                        }

                        if (status)
                        {
                            getMessageHelper().sendPlayer(event.getPlayer(), mod + ": " + ChatColor.GREEN + "On" + statusServerString, ChatColor.GOLD);
                        } else
                        {
                            getMessageHelper().sendPlayer(event.getPlayer(), mod + ": " + ChatColor.RED + "Off", ChatColor.GOLD);
                        }
                    }

                    int level = leveling.getLevel();
                    int nextBlocks;
                    try
                    {
                        nextBlocks = Integer.parseInt(getConfig().read("Config.Leveling." + (level + 1)));
                    } catch (NumberFormatException e)
                    {
                        nextBlocks = -1;
                    }
                    getMessageHelper().sendPlayer(event.getPlayer(), "Level: " + level + " | Blocks: " + leveling.getBlocks(), ChatColor.GREEN);
                    if (nextBlocks == -1)
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "Max level reached!", ChatColor.GREEN);
                    } else
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "Blocks needed for next level: " + nextBlocks, ChatColor.GREEN);
                    }
                    break;
                case "Magnet":
                    if (Boolean.parseBoolean(getConfig().read("Config.Players." + event.getPlayer().getUniqueId() + ".Magnet")))
                    {
                        getConfig().write("Config.Players." + event.getPlayer().getUniqueId() + ".Magnet", "" + false);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Magnet turned off!", ChatColor.GOLD);
                    } else
                    {
                        getConfig().write("Config.Players." + event.getPlayer().getUniqueId() + ".Magnet", "" + true);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Magnet turned on!", ChatColor.GOLD);
                    }
                    break;
                default:
                    String modName = event.getName();
                    if (Constants.modsList.contains(modName))
                    {
                        if (!getConfig().readModStatus(event.getPlayer(), modName))
                        {
                            int levelNeeded = Integer.parseInt(getConfig().read("Config.Leveling.Modlevel." + modName));
                            int currentLevel = leveling.getLevel();

                            if (currentLevel >= levelNeeded)
                            {
                                getConfig().writeModStatus(event.getPlayer(), modName, true);
                                getMessageHelper().sendPlayer(event.getPlayer(), modName + " Mod was activated", ChatColor.GOLD);
                            } else
                            {
                                getMessageHelper().sendPlayer(event.getPlayer(), "Your level is to low. (Level " + levelNeeded + " needed)", ChatColor.RED);
                            }
                        } else
                        {
                            getConfig().writeModStatus(event.getPlayer(), modName, false);
                            getMessageHelper().sendPlayer(event.getPlayer(), modName + " Mod was deactivated", ChatColor.GOLD);
                        }
                    } else
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "Error occurred! (" + modName + ")", ChatColor.RED);
                    }
            }
            event.setWillClose(true);
        }, Constants.plugin)
                // Set items
                .setOption(0, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                .setOption(1, new ItemStack(Material.REDSTONE_BLOCK, 1), "Toggle", "Turn mastering on/off.")
                .setOption(3, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.")
                .setOption(4, new ItemStack(Material.FURNACE, 1), "Autosmelt", "Directly smelt items that were harvested/mined.")
                .setOption(5, new ItemStack(Material.DIAMOND, 1), "Fortune", "Get a luck multiplier on all actions.")
                .setOption(8, new ItemStack(Material.BOOK, 1), "Status", "Show current status.")
                .setOption(10, new ItemStack(Material.IRON_BLOCK, 1), "Magnet", "(WIP!!!) Turn magnet on/off.");
    }
}
