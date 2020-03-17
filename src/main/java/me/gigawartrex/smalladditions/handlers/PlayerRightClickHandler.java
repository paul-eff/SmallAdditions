package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.itemmenu.IconMenu;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerRightClickHandler implements Listener
{

    private Config config;
    private MessageHelper msghelp;

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WHEAT));

    private Player eventPlayer;
    private Block eventBlock;
    private Leveling leveling;
    private Action action;

    /*
     * Method to handle dropped items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getPlayer();
        eventBlock = event.getClickedBlock();
        leveling = new Leveling(eventPlayer);
        action = event.getAction();

        boolean validItem = false;

        for(String str : Arrays.asList("_SHOVEL", "_AXE", "_PICKAXE"))
        {
            if(eventPlayer.getInventory().getItemInMainHand().getType().toString().contains(str))
            {
                validItem = true;
                break;
            }
        }

        if(validItem)
        {
            if(eventPlayer.isSneaking() && action.equals(Action.RIGHT_CLICK_AIR))
            {
                IconMenu menu = new IconMenu("My Fancy Menu", 9, new IconMenu.OptionClickEventHandler()
                {
                    @Override
                    public void onOptionClick(IconMenu.OptionClickEvent event)
                    {
                        switch (event.getName())
                        {
                            case "Exit":
                                msghelp.sendPlayer(eventPlayer, "Exiting...", ChatColor.RED);
                                break;
                            case "Toggle":
                                if (config.readPlayerStatus(eventPlayer))
                                {
                                    config.writePlayerStatus(eventPlayer, false);
                                    msghelp.sendPlayer(eventPlayer, "Mastering Mode turned off!", ChatColor.GOLD);
                                } else
                                {
                                    config.writePlayerStatus(eventPlayer, true);
                                    msghelp.sendPlayer(eventPlayer, "Mastering Mode turned on!", ChatColor.GOLD);
                                }
                                break;
                            case "Status":
                                boolean isActive = Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Mastering on?"));

                                if (isActive)
                                {
                                    msghelp.sendPlayer(eventPlayer, "Mastering Mode: " + ChatColor.GREEN + "On", ChatColor.GOLD);
                                } else
                                {
                                    msghelp.sendPlayer(eventPlayer, "Mastering Mode: " + ChatColor.RED + "Off", ChatColor.GOLD);
                                }

                                msghelp.sendPlayer(eventPlayer, "Your mods are set as the following:", ChatColor.GOLD);

                                for (String mod : Constants.modsList)
                                {
                                    boolean status = Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Mods." + mod));
                                    boolean statusServer = Boolean.parseBoolean(config.read("Config.Settings.Mods." + mod));
                                    String statusServerString = "";

                                    if (!statusServer)
                                    {
                                        statusServerString = ChatColor.RED + " (Off by Server)";
                                    }

                                    if (status)
                                    {
                                        msghelp.sendPlayer(eventPlayer, mod + ": " + ChatColor.GREEN + "On" + statusServerString, ChatColor.GOLD);
                                    } else
                                    {
                                        msghelp.sendPlayer(eventPlayer, mod + ": " + ChatColor.RED + "Off", ChatColor.GOLD);
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
                                msghelp.sendPlayer(eventPlayer, "Level: " + level + " | Blocks: " + leveling.getBlocks(), ChatColor.GREEN);
                                if (nextBlocks == -1)
                                {
                                    msghelp.sendPlayer(eventPlayer, "Max level reached!", ChatColor.GREEN);
                                } else
                                {
                                    msghelp.sendPlayer(eventPlayer, "Blocks needed for next level: " + nextBlocks, ChatColor.GREEN);
                                }
                                break;
                            default:
                                String modName = event.getName();
                                if (Constants.modsList.contains(modName))
                                {

                                    if (!config.readModStatus(eventPlayer, modName))
                                    {
                                        int levelNeeded = Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + modName));
                                        int currentLevel = leveling.getLevel();

                                        if (currentLevel >= levelNeeded)
                                        {
                                            config.writeModStatus(eventPlayer, modName, true);
                                            msghelp.sendPlayer(eventPlayer, modName + " Mod was activated", ChatColor.GOLD);
                                        } else
                                        {
                                            msghelp.sendPlayer(eventPlayer, "Your level is to low. (Level " + levelNeeded + " needed)", ChatColor.RED);
                                        }
                                    } else
                                    {
                                        config.writeModStatus(eventPlayer, modName, false);
                                        msghelp.sendPlayer(eventPlayer, modName + " Mod was deactivated", ChatColor.GOLD);
                                    }
                                } else
                                {
                                    msghelp.sendPlayer(eventPlayer, "Error occurred! (" + modName + ")", ChatColor.RED);
                                }
                        }
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                    }
                }, Constants.plugin)
                        .setOption(0, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                        .setOption(1, new ItemStack(Material.REDSTONE_BLOCK, 1), "Toggle", "Turn mastering on/off.")
                        .setOption(3, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.")
                        .setOption(4, new ItemStack(Material.FURNACE, 1), "Autosmelt", "Directly smelt items that were harvested/mined.")
                        .setOption(5, new ItemStack(Material.DIAMOND, 1), "Fortune", "Get a luck multiplier on all actions.")
                        .setOption(8, new ItemStack(Material.BOOK, 1), "Status", "Show current status.");

                menu.open(eventPlayer);
            }
        }
        /*
        if (allowedItems.contains(eventBlock.getType()))
        {
            System.out.println(eventBlock.getState().getData());

            if (eventBlock.getData() == (byte) 7)
            {
                for (ItemStack item : eventBlock.getDrops())
                {
                    eventBlock.getWorld().dropItemNaturally(eventBlock.getLocation(), item);
                }
                eventBlock.getState().setRawData((byte) 0);
            }
        }
        */
    }
}
