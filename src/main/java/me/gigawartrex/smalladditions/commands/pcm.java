package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class pcm implements CommandExecutor
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

                            /*
                            //Get all block drops
                            Block origBlock = player.getTargetBlock(null, 200);
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

                            //Get some data
                            player.sendMessage("Main Hand: " + player.getInventory().getItemInMainHand().getType());
                            player.sendMessage("Looking at: " + player.getTargetBlock(null, 200).getType().toString());
                            player.sendMessage("Possible Drops: " + player.getTargetBlock(null, 200).getDrops());

                            /*
                            //Generate a YxY ore vein
                            Block origBlock = player.getTargetBlock(null, 200);

                            for(int xOffset = 0; xOffset < 5; xOffset++){
                                for(int yOffset = 0; yOffset < 4; yOffset++){
                                    for(int zOffset = 0; zOffset < 7; zOffset++){
                                        double rand = Math.random();
                                        if(rand <= 0.3){
                                            origBlock.getLocation().add(xOffset, yOffset, zOffset).getBlock().setType(Material.IRON_ORE);
                                        }else{
                                            origBlock.getLocation().add(xOffset, yOffset, zOffset).getBlock().setType(Material.STONE);
                                        }
                                    }
                                }
                            }
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
                                player.getInventory().addItem(new Book("PracticeCreatesMasters Guide", "GigaWarTr3x", "Rules"));
                                //player.getWorld().dropItemNaturally(player.getLocation(), new Book("PracticeCreatesMasters Guide", "GigaWarTr3x", "Rules"));

                                if (!Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Book received?")))
                                {
                                    config.write("Config.Players." + player.getUniqueId() + ".Book received?", "" + true);
                                }
                                break;
                            }
                        }
                        return true;
                    case "status":

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
                    case "on":
                        config.writePlayerStatus(player, true);
                        msghelp.sendPlayer(player, "Mastering Mode turned on!", ChatColor.GOLD);
                        return true;
                    case "off":
                        config.writePlayerStatus(player, false);
                        msghelp.sendPlayer(player, "Mastering Mode turned off!", ChatColor.GOLD);
                        return true;
                    default:
                        msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                        msghelp.sendPlayer(player, "Type \"/pcm\" + Spacebar and check the options.", ChatColor.WHITE);
                        return true;
                }
            } else if (args.length == 2)
            {

                switch (args[1])
                {
                    case "list":

                        StringBuilder modsString = new StringBuilder();

                        for (String mod : Constants.modsList)
                        {
                            if (modsString.toString().equals(""))
                            {
                                modsString = new StringBuilder("Mods: " + mod);
                            } else
                            {
                                modsString.append(", ").append(mod);
                            }
                        }

                        msghelp.sendPlayer(player, modsString.toString(), ChatColor.GOLD);

                        return true;
                    //more cases here
                    default:
                        msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                        msghelp.sendPlayer(player, "Type \"/pcm mod\" + Spacebar and check the options.", ChatColor.WHITE);
                        return true;
                }

            } else if (args.length == 3)
            {

                if (args[0].equals("mod") && Constants.modsList.contains(args[1]))
                {

                    if (args[2].equals("on"))
                    {

                        int levelNeeded = Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + args[1]));
                        int currentLevel = leveling.getLevel();

                        if (currentLevel >= levelNeeded)
                        {
                            config.writeModStatus(player, args[1], true);
                            msghelp.sendPlayer(player, args[1] + " Mod was activated", ChatColor.GOLD);
                        } else
                        {
                            msghelp.sendPlayer(player, "Your level is to low. (Level " + levelNeeded + " needed)", ChatColor.RED);
                        }
                        return true;
                    } else if (args[2].equals("off"))
                    {
                        config.writeModStatus(player, args[1], false);
                        msghelp.sendPlayer(player, args[1] + " Mod was deactivated", ChatColor.GOLD);
                        return true;
                    }
                }
                msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                msghelp.sendPlayer(player, "Type \"/pcm mod\" + Spacebar and check the options.", ChatColor.WHITE);
                return true;
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
