package me.gigawartrex.smalladditions.helpers;

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
        return new IconMenu("SmallAdditions Menu", 9, event -> {
            //Leveling leveling = new Leveling(event.getPlayer());

            // Switch for command executed/item clicked
            switch (event.getName())
            {
                case "Exit":
                    break;
                /*case "Toggle":
                    if (getConfig().readPlayerStatus(event.getPlayer()))
                    {
                        getConfig().writePlayerStatus(event.getPlayer(), false);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode turned off!", ChatColor.GOLD);
                    } else
                    {
                        getConfig().writePlayerStatus(event.getPlayer(), true);
                        getMessageHelper().sendPlayer(event.getPlayer(), "Mastering Mode turned on!", ChatColor.GOLD);
                    }
                    break;*/
                case "Status":
                    boolean magnetOn = getConfig().readModStatus(event.getPlayer(), "Magnet");
                    boolean replantOn = getConfig().readModStatus(event.getPlayer(), "Replant");

                    getMessageHelper().sendPlayer(event.getPlayer(), "Status:", ChatColor.GOLD);
                    getMessageHelper().sendPlayer(event.getPlayer(), "Magnet is " + (magnetOn ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
                    getMessageHelper().sendPlayer(event.getPlayer(), "Replant is " + (replantOn ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
                    break;
                default:
                    // Not using readModStatus as an empty String means there was an error!
                    String readFromConfig = getConfig().read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods." + event.getName());
                    if (!readFromConfig.equals(""))
                    {
                        boolean isOn = Boolean.parseBoolean(readFromConfig);
                        if (isOn)
                        {
                            getConfig().writeModStatus(event.getPlayer(), event.getName(), false);
                            getMessageHelper().sendPlayer(event.getPlayer(), event.getName() + " turned " + ChatColor.RED + "off!");
                        } else
                        {
                            getConfig().writeModStatus(event.getPlayer(), event.getName(), true);
                            getMessageHelper().sendPlayer(event.getPlayer(), event.getName() + " turned " + ChatColor.GREEN + "on!");
                        }
                    }else
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "There was an error accessing your menu. Please report this to an admin!", ChatColor.RED);
                    }
                    break;
                /*default:
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
                    }*/
            }
            event.setWillClose(true);
        }, Constants.plugin)
                // Set items
                .setOption(1, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                .setOption(7, new ItemStack(Material.BOOK, 1), "Status", "Show current status.")
                .setOption(3, new ItemStack(Material.IRON_PICKAXE, 1), "Veining", "Mine/Chop/Dig same adjacent blocks.")
                .setOption(4, new ItemStack(Material.IRON_BLOCK, 1), "Magnet", "(WIP!!!) Turn magnet on/off.")
                .setOption(5, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.");
        //.setOption(1, new ItemStack(Material.REDSTONE_BLOCK, 1), "Toggle", "Turn mastering on/off.")
        //.setOption(4, new ItemStack(Material.FURNACE, 1), "Autosmelt", "Directly smelt items that were harvested/mined.")
        //.setOption(5, new ItemStack(Material.DIAMOND, 1), "Fortune", "Get a luck multiplier on all actions.")
    }
}
