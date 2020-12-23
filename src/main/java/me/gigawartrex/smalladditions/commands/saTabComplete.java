package me.gigawartrex.smalladditions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class saTabComplete implements TabCompleter
{
    private final ArrayList<String> arg0List = new ArrayList<>(Arrays.asList("test", "invsee", "enderinvsee", "resetall", "book", "menu"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof Player && !sender.isOp())
            {
                return arg0List.subList(4, arg0List.size());
            }
            return arg0List;
        }else if (args.length == 2)
        {
            if (sender instanceof Player && !sender.isOp())
            {
                ArrayList<String> players = new ArrayList<>();
                for(Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    players.add(player.getName());
                }
                return players;
            }
        }
        return null;
    }
}
