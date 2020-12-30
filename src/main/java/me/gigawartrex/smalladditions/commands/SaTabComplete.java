package me.gigawartrex.smalladditions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for handling "/sa" command tab completions.
 *
 * @author Paul Ferlitz
 * @version 1.0 2020-12-28 Initial Version
 * @since 1.0
 */
public class SaTabComplete implements TabCompleter
{
    // Class variables
    private final ArrayList<String> arg0List = new ArrayList<>(Arrays.asList("test", "invsee", "enderinvsee", "resetall", "book", "menu"));

    /**
     * Main method handling incoming command's tab completions.
     *
     * @param sender The command sender.
     * @param cmd    The base command, e.g. "sa".
     * @param label  N/A.
     * @param args   All following subcommands as an array.
     * @return ArrayList of Strings that can be applied to the given command.
     * @since 1.0
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof Player && !sender.isOp())
            {
                // Return only not OP commands
                return arg0List.subList(4, arg0List.size());
            }
            return arg0List;
        } else if (args.length == 2)
        {
            // Return all currently online players
            if (sender instanceof Player && !sender.isOp())
            {
                ArrayList<String> players = new ArrayList<>();
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    players.add(player.getName());
                }
                return players;
            }
        }
        return null;
    }
}
