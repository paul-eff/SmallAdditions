package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
    private ArrayList<Player> hiddenPlayers = new ArrayList<>();

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
            if (args.length < 1)
            {
                msghelp.sendPlayer(player, "Wrong command usage. Type \"/help SmallAdditions\" for more details.", ChatColor.RED);
                return true;
            }
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

                                if (!Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Book received")))
                                {
                                    config.write("Config.Players." + player.getUniqueId() + ".Book received", "" + true);
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
                        boolean playerFound = false;
                        if (args.length == 1)
                        {
                            boolean newStatus = config.readPlayerAttributeStatus(player, "Ninjajoin");
                            config.writePlayerAttributeStatus(player, "Ninjajoin", !newStatus);
                            if (newStatus)
                            {
                                msghelp.sendPlayer(player, "Ninjajoin turned " + ChatColor.RED + "OFF!");
                            } else
                            {
                                msghelp.sendPlayer(player, "Ninjajoin turned " + ChatColor.GREEN + "ON!");
                            }
                            playerFound = true;
                        } else if (args.length == 2)
                        {
                            for (OfflinePlayer targetPlayer : Bukkit.getOfflinePlayers())
                            {
                                if (targetPlayer.getName().equals(args[1]))
                                {
                                    boolean newStatus = Boolean.parseBoolean(config.read(config.getFileName() + ".Players." + targetPlayer.getUniqueId() + ".Ninjajoin"));
                                    config.write(config.getFileName() + ".Players." + targetPlayer.getUniqueId() + ".Ninjajoin", "" + !newStatus);
                                    if (newStatus)
                                    {
                                        msghelp.sendPlayer(player, "Ninjajoin (for player " + targetPlayer.getName() + ") turned " + ChatColor.RED + "OFF!");
                                    } else
                                    {
                                        msghelp.sendPlayer(player, "Ninjajoin (for player " + targetPlayer.getName() + ") turned " + ChatColor.GREEN + "ON!");
                                    }
                                    playerFound = true;
                                    break;
                                }
                            }
                        }
                        if (!playerFound)
                        {
                            msghelp.sendPlayer(player, "The target player does not exist or never joined this server!", ChatColor.RED);
                        }
                    } else
                    {
                        msghelp.sendPlayer(player, "This is an OP only command!", ChatColor.RED);
                    }
                    break;
                // Command to hide an admin from everybody
                case "hide":
                    if (isOP)
                    {
                        boolean playerFound = false;
                        Player hideablePlayer = null;
                        if (args.length == 1)
                        {
                            boolean newStatus = config.readPlayerAttributeStatus(player, "Hide");
                            config.writePlayerAttributeStatus(player, "Hide", !newStatus);
                            if (newStatus)
                            {
                                msghelp.sendPlayer(player, "Hide turned " + ChatColor.RED + "OFF!");
                            } else
                            {
                                msghelp.sendPlayer(player, "Hide turned " + ChatColor.GREEN + "ON!");
                            }
                            hideablePlayer = player;
                            playerFound = true;
                        } else if (args.length == 2)
                        {
                            for (OfflinePlayer targetPlayer : Bukkit.getOfflinePlayers())
                            {
                                if (targetPlayer.getName().equals(args[1]))
                                {
                                    boolean newStatus = Boolean.parseBoolean(config.read(config.getFileName() + ".Players." + targetPlayer.getUniqueId() + ".Hide"));
                                    config.write(config.getFileName() + ".Players." + targetPlayer.getUniqueId() + ".Hide", "" + !newStatus);
                                    if (newStatus)
                                    {
                                        msghelp.sendPlayer(player, "Hide (for player " + targetPlayer.getName() + ") turned " + ChatColor.RED + "OFF!");
                                    } else
                                    {
                                        msghelp.sendPlayer(player, "Hide (for player " + targetPlayer.getName() + ") turned " + ChatColor.GREEN + "ON!");
                                    }
                                    hideablePlayer = targetPlayer.getPlayer();
                                    playerFound = true;
                                    break;
                                }
                            }
                        }
                        if (hideablePlayer != null && hideablePlayer.isOnline())
                        {
                            boolean inList = hiddenPlayers.contains(hideablePlayer);

                            for (Player p : Bukkit.getOnlinePlayers())
                            {
                                if (p == hideablePlayer) continue;
                                if (inList)
                                {
                                    p.showPlayer(Constants.plugin, hideablePlayer);
                                } else
                                {
                                    p.hidePlayer(Constants.plugin, hideablePlayer);
                                }
                            }
                            if (inList)
                            {
                                hiddenPlayers.remove(hideablePlayer);
                            } else
                            {
                                hiddenPlayers.add(hideablePlayer);
                            }
                        }
                        if (!playerFound)
                        {
                            msghelp.sendPlayer(player, "The target player does not exist or never joined this server!", ChatColor.RED);
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
                        // Code for testing here
                        for (int i = 0; i <= player.getInventory().getSize(); i++)
                        {
                            if (player.getInventory().getItem(i) != null)
                            {
                                msghelp.sendPlayer(player, i + ": " + player.getInventory().getItem(i).getType());
                            }
                        }
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
            if (args.length < 1)
            {
                msghelp.sendConsole("Wrong command usage. Type \"/help SmallAdditions\" for more details.", ChatColor.RED);
                return true;
            }
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
                case "hide":
                    if (args.length == 2)
                    {
                        Player player = Bukkit.getPlayer(args[1]);
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            if (p.getName().equals(args[1])) continue;
                            p.hidePlayer(Constants.plugin, player);
                            if (!hiddenPlayers.contains(p)) hiddenPlayers.add(p);
                        }
                    } else
                    {
                        msghelp.sendConsole("Did you mean \"/sa hide <playername>\"?", ChatColor.RED);
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
