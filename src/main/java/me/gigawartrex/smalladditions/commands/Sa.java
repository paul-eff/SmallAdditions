package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Sa implements CommandExecutor
{
    private final MessageHelper msghelp = new MessageHelper();
    private final Config config = new Config();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = ((Player) sender).getPlayer();
            boolean isOP = player.isOp();

            switch (args[0])
            {
                case "book":
                    if (args.length != 1)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa book\"?", ChatColor.RED);
                    } else
                    {
                        for (ItemStack stack : player.getInventory().getStorageContents())
                        {
                            if (stack == null)
                            {
                                player.getInventory().addItem(new Book("SmallAdditions Guide", "GigaWarTr3x", "Rules"));

                                if (!Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Book received?")))
                                {
                                    config.write("Config.Players." + player.getUniqueId() + ".Book received?", "" + true);
                                }
                                break;
                            }
                        }
                    }
                    break;
                case "menu":
                    if (args.length != 1)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa menu\"?", ChatColor.RED);
                    } else
                    {
                        Constants.mainMenu.open(player);
                    }
                    break;
                case "resetall":
                    if (args.length != 1)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa resetall\"?", ChatColor.RED);
                    } else
                    {
                        msghelp.sendPlayer(player, "This is a console only command. Contact your system admin for help.", ChatColor.RED);
                    }
                    break;
                case "invsee":
                    if (args.length != 2)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa invsee <playername>\"?", ChatColor.RED);
                    } else
                    {
                        if (isOP)
                        {
                            for (Player targetPlayer : Bukkit.getOnlinePlayers())
                            {
                                if (targetPlayer.getName().equals(args[1]))
                                {
                                    player.openInventory(targetPlayer.getInventory());
                                    return true;
                                }
                            }
                            msghelp.sendPlayer(player, "Player not found, is he online?", ChatColor.RED);
                        } else
                        {
                            msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                        }
                    }
                    break;
                case "enderinvsee":
                    if (args.length != 2)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa enderinvsee <playername>\"?", ChatColor.RED);
                    } else
                    {
                        if (isOP)
                        {
                            for (Player targetPlayer : Bukkit.getOnlinePlayers())
                            {
                                if (targetPlayer.getName().equals(args[1]))
                                {
                                    player.openInventory(targetPlayer.getEnderChest());
                                    return true;
                                }
                            }
                            msghelp.sendPlayer(player, "Player not found, is he online?", ChatColor.RED);
                        } else
                        {
                            msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                        }
                    }
                    break;
                case "test":
                    if (isOP)
                    {
                        // Empty for now
                    } else
                    {
                        msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                    }
                    break;
                default:
                    msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                    msghelp.sendPlayer(player, "Type \"/help SmallAdditions\" and check out available commands.", ChatColor.WHITE);
                    break;
            }
        } else
        {
            switch (args[0])
            {
                case "resetall":
                    if (args.length != 1)
                    {
                        msghelp.sendConsole("Did you mean \"/sa resetall\"?", ChatColor.RED);
                    } else
                    {
                        config.defaultConfig(true);
                    }
                    break;
                default:
                    msghelp.sendConsole("This is not a console Command!", ChatColor.RED);
                    break;
            }
        }
        return true;
    }
}
