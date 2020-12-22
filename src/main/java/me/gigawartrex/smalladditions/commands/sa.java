package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class sa implements CommandExecutor
{
    private MessageHelper msghelp = new MessageHelper();
    private Config config;
    private Leveling leveling;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        config = new Config();

        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            boolean isOP = player.isOp();
            leveling = new Leveling(player);

            if (args.length == 1)
            {
                switch (args[0])
                {
                    case "test":
                        if (isOP)
                        {
                            Chunk chunk = player.getWorld().getChunkAt(player.getLocation());

                            if (chunk.isForceLoaded())
                            {
                                System.out.println(chunk.toString() + " is force loaded.");
                            } else
                            {
                                System.out.println(chunk.toString() + " is normally loaded.");
                                Bukkit.getServer().getWorld(player.getWorld().getUID()).setChunkForceLoaded(chunk.getX(), chunk.getZ(), true);
                            }


                            /*if (world.getChunkAt(-14, 2).isLoaded())
                            {
                                System.out.println("Chunk is loaded");
                                if (world.getChunkAt(-14, 2).isForceLoaded())
                                {
                                    System.out.println("Chunk is force loaded");
                                    world.setChunkForceLoaded(-14, 2, false);
                                    System.out.println("Chunk was un force loaded");
                                } else
                                {
                                    System.out.println("Chunk is not force loaded");
                                }
                            } else
                            {
                                System.out.println("Chunk is unloaded");

                            }*/


                            //Block origBlock = player.getTargetBlock(null, 200);

                            /*
                            //Get all block drops
                            String dropsString = "";
                            String dropsMeta = "";
                            String dropsElse = "";
                            for (ItemStack drop : origBlock.getDrops())
                            {
                                dropsString += ", " + drop.toString();
                                dropsMeta += ", " + drop.getData().toString();
                                dropsElse += ", " + drop.getItemMeta().toString();
                            }
                            player.sendMessage("Block's Drops: " + dropsString);
                            player.sendMessage("Block's Meta: " + dropsMeta);
                            player.sendMessage("Block's Else: " + dropsElse);
                            */
                        } else
                        {
                            msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                        }
                        return true;
                    case "book":
                        for (ItemStack stack : player.getInventory().getStorageContents())
                        {
                            if (stack == null)
                            {
                                player.getInventory().addItem(new Book("SmallAdditions Guide", "GigaWarTr3x", "Rules"));
                                //player.getWorld().dropItemNaturally(player.getLocation(), new Book("PracticeCreatesMasters Guide", "GigaWarTr3x", "Rules"));

                                if (!Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Book received?")))
                                {
                                    config.write("Config.Players." + player.getUniqueId() + ".Book received?", "" + true);
                                }
                                break;
                            }
                        }
                        return true;
                    case "menu":
                        //If created errors, before: IconMenu menu = new IconMenu("Mastering", 9, new IconMenu.OptionClickEventHandler()
                        IconMenu menu = new IconMenu("Mastering", 18, event -> {
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

                        menu.open(player);

                        return true;
                    case "resetall":
                        if (isOP)
                        {
                            config.defaultConfig(true);
                        } else
                        {
                            msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                        }
                        return true;
                    default:
                        msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                        msghelp.sendPlayer(player, "Type \"/sa\" + Spacebar and check the options.", ChatColor.WHITE);
                        return true;
                }
            }
        } else
        {
            if (args.length == 1)
            {
                if (args[0].equals("resetall"))
                {
                    config.defaultConfig(true);
                    return true;
                }
            } else
            {
                msghelp.sendConsole("This is not a console Command!", ChatColor.RED);
                return true;
            }
        }
        return false;
    }
}
