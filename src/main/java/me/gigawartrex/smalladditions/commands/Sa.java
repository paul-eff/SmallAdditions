package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for handling "/sa" commands.
 *
 * @author Paul Ferlitz
 */
public class Sa implements CommandExecutor
{
    // Class variables
    private final MessageHelper msghelp = new MessageHelper();
    private final Config config = new Config();

    /**
     * Main method handling incoming commands.
     *
     * @param sender the command sender
     * @param cmd    the base command, e.g. "sa"
     * @param label  N/A
     * @param args   the following subcommands as an array
     * @return {@code True} if the command was parsed and executed successfully
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Check if sender is a player/was sent from ingame chat
        if (sender instanceof Player)
        {
            Player player = ((Player) sender).getPlayer();
            boolean isOP = player.isOp();

            switch (args[0])
            {
                // Drop the executing player a new README book
                case "book":
                    // Doesn't accept any additional arguments
                    if (args.length != 1)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa book\"?", ChatColor.RED);
                    } else
                    {
                        // Create ItemStack and drop into the player's inventory if a slot is empty
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
                // Open the main SmallAdditions menu
                case "menu":
                    // Doesn't accept any additional arguments
                    if (args.length != 1)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa menu\"?", ChatColor.RED);
                    } else
                    {
                        // Open the predefined menu
                        Constants.mainMenu.open(player);
                    }
                    break;
                // Command to reset all SimpleAdditions files/settings
                case "resetall":
                    // Doesn't accept any additional arguments
                    msghelp.sendPlayer(player, "This is a console only command. Contact your system admin for help.", ChatColor.RED);
                    break;
                // Look into / interact with other players inventory
                case "invsee":
                    // Needs to have 2 argument
                    if (args.length != 2)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa invsee <playername>\"?", ChatColor.RED);
                    } else
                    {
                        // Check if player is OP, get inventory and open
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
                // Look into / interact with other players enderchest inventory
                case "enderinvsee":
                    // Needs to have 2 argument
                    if (args.length != 2)
                    {
                        msghelp.sendPlayer(player, "Did you mean \"/sa enderinvsee <playername>\"?", ChatColor.RED);
                    } else
                    {
                        // Check if player is OP, get inventory and open
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
                // A toggle to suppress the player-joined message
                case "ninjajoin":
                    if (isOP)
                    {
                        if (args.length == 1)
                        {
                            String yamlPath = "Config.Players." + player.getUniqueId() + ".Ninjajoin";
                            String newStatus = config.read(yamlPath).equals("true") ? "false" : "true";
                            config.write(yamlPath, newStatus);
                            msghelp.sendPlayer(player, "Ninjajoin status set to: " + newStatus, ChatColor.GREEN);
                        } else if (args.length == 2)
                        {
                            for (OfflinePlayer targetPlayer : Bukkit.getOfflinePlayers())
                            {
                                if (targetPlayer.getName().equals(args[1]))
                                {
                                    String yamlPath = "Config.Players." + targetPlayer.getUniqueId() + ".Ninjajoin";
                                    String newStatus = config.read(yamlPath).equals("true") ? "false" : "true";
                                    config.write(yamlPath, newStatus);
                                    msghelp.sendPlayer(player, "Toggled " + targetPlayer.getName() + "'s Ninjajoin status to: " + newStatus, ChatColor.GREEN);
                                    break;
                                }
                            }
                        }
                    } else
                    {
                        msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                    }
                    break;
                // Command to easily test new code
                case "test":
                    if (isOP)
                    {
                        // Empty for now
                    } else
                    {
                        msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                    }
                    break;
                // Default
                default:
                    msghelp.sendPlayer(player, "Not intended command usage.", ChatColor.RED);
                    msghelp.sendPlayer(player, "Type \"/help SmallAdditions\" and check out available commands.", ChatColor.WHITE);
                    break;
            }
        } else

        {
            switch (args[0])
            {
                // Command to reset all SimpleAdditions files/settings
                case "resetall":
                    // Doesn't accept any additional arguments
                    if (args.length != 1)
                    {
                        msghelp.sendConsole("Did you mean \"/sa resetall\"?", ChatColor.RED);
                    } else
                    {
                        config.defaultConfig(true);
                    }
                    break;
                case "ninjajoin":
                    if (args.length == 2)
                    {
                        for (OfflinePlayer targetPlayer : Bukkit.getOfflinePlayers())
                        {
                            if (targetPlayer.getName().equals(args[1]))
                            {
                                String yamlPath = "Config.Players." + targetPlayer.getUniqueId() + ".Ninjajoin";
                                String newStatus = config.read(yamlPath).equals("true") ? "false" : "true";
                                config.write(yamlPath, newStatus);
                                msghelp.sendConsole("Toggled " + targetPlayer.getName() + "'s Ninjajoin status to: " + newStatus, ChatColor.GREEN);
                                break;
                            }
                        }
                    } else
                    {
                        msghelp.sendConsole("Did you mean \"/sa ninjajoin <playername>\"?", ChatColor.RED);
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
