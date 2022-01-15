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
 */
public class SaTabComplete implements TabCompleter
{
    // Class variables
    private final ArrayList<String> arg0List = new ArrayList<>(Arrays.asList("test", "ninjajoin","invsee", "enderinvsee", "resetall", "book", "menu"));

    /**
     * Main method handling incoming command's tab completions.
     *
     * @param sender the command sender
     * @param cmd    the base command, e.g. "sa"
     * @param label  N/A
     * @param args   the following subcommands as an array
     * @return a {@code ArrayList<String>} holding possible command options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof Player && !sender.isOp())
            {
                // Return only not OP commands
                return arg0List.subList(5, arg0List.size());
            }
            return arg0List;
        } else if (args.length == 2)
        {
            // Return all currently online players
            if (sender instanceof Player)
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
