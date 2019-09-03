package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.main.Constants;
import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import org.bukkit.ChatColor;
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
