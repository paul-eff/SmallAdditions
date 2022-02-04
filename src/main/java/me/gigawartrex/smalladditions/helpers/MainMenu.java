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
            // Switch for command executed/item clicked
            switch (event.getName())
            {
                case "Exit":
                    break;
                case "Status":
                    getMessageHelper().sendPlayer(event.getPlayer(), "Status:", ChatColor.GOLD);
                    for (String mod : Constants.modsList)
                    {
                        boolean currentMod = getConfig().readModStatus(event.getPlayer(), mod);
                        getMessageHelper().sendPlayer(event.getPlayer(), mod + " is " + (currentMod ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
                    }
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
                            getMessageHelper().sendPlayer(event.getPlayer(), event.getName() + " turned " + ChatColor.RED + "OFF!");
                        } else
                        {
                            getConfig().writeModStatus(event.getPlayer(), event.getName(), true);
                            getMessageHelper().sendPlayer(event.getPlayer(), event.getName() + " turned " + ChatColor.GREEN + "ON!");
                        }
                    } else
                    {
                        getMessageHelper().sendPlayer(event.getPlayer(), "There was an error accessing your menu. Please report this to an admin!", ChatColor.RED);
                    }
                    break;
            }
            event.setWillClose(true);
        }, Constants.plugin)
                // Set items
                .setOption(1, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                .setOption(7, new ItemStack(Material.BOOK, 1), "Status", "Show current status.")
                .setOption(3, new ItemStack(Material.IRON_PICKAXE, 1), "Veining", "Mine/Chop/Dig same adjacent blocks.")
                .setOption(4, new ItemStack(Material.IRON_BLOCK, 1), "Magnet", "(WIP!!!) Turn magnet on/off.")
                .setOption(5, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.");
    }
}
