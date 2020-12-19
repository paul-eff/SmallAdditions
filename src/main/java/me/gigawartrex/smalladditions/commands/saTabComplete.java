package me.gigawartrex.smalladditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class saTabComplete implements TabCompleter
{
    private ArrayList<String> arg0List = new ArrayList<>(Arrays.asList("test", "resetall", "book", "menu"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof Player && !sender.isOp())
            {
                return arg0List.subList(2, arg0List.size());
            }
            return arg0List;
        }
        return null;
    }
}
