package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.itemmenu.IconMenu;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
                            Block origBlock = player.getTargetBlock(null, 200);

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
                    case "magnet":
                        msghelp.sendPlayer(player, "Type \\sa magnet on/off", ChatColor.RED);
                        return true;
                    case "menu":

                        IconMenu menu = new IconMenu("Mastering", 9, new IconMenu.OptionClickEventHandler()
                        {
                            @Override
                            public void onOptionClick(IconMenu.OptionClickEvent event)
                            {
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
                            }
                        }, Constants.plugin)
                                .setOption(0, new ItemStack(Material.DIRT, 1), "Exit", "Click to exit options.")
                                .setOption(1, new ItemStack(Material.REDSTONE_BLOCK, 1), "Toggle", "Turn mastering on/off.")
                                .setOption(3, new ItemStack(Material.OAK_SAPLING, 1), "Replant", "Replant cut down trees.")
                                .setOption(4, new ItemStack(Material.FURNACE, 1), "Autosmelt", "Directly smelt items that were harvested/mined.")
                                .setOption(5, new ItemStack(Material.DIAMOND, 1), "Fortune", "Get a luck multiplier on all actions.")
                                .setOption(8, new ItemStack(Material.BOOK, 1), "Status", "Show current status.");

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
            } else if (args.length == 2 && args[0].equals("magnet"))
            {
                // Turn off for now

                switch (args[1])
                {
                    case "on":
                        config.write("Config.Players." + player.getUniqueId() + ".Magnet", "" + true);
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                for (Player player : Constants.plugin.getServer().getOnlinePlayers())
                                {
                                    if (Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Magnet")))
                                    {
                                        System.out.println("Iterating Players");
                                        for (Entity ent : player.getNearbyEntities(8, 4, 8))
                                        {
                                            System.out.println("Ent found");
                                            if (ent instanceof Item)
                                            {
                                                System.out.println("Item found and ported");
                                                player.getWorld().dropItemNaturally(player.getLocation(), ((Item) ent).getItemStack());
                                                ent.remove();
                                            }
                                        }
                                    } else
                                    {
                                        System.out.println("Not on");
                                    }
                                }
                            }
                        }.runTaskTimer(Constants.plugin, 20L * 5, 20L * 2);
                        break;
                    case "off":
                        config.write("Config.Players." + player.getUniqueId() + ".Magnet", "" + false);
                        break;
                    default:
                        break;
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
