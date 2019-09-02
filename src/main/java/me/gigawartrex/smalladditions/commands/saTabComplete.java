package me.gigawartrex.smalladditions.commands;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class saTabComplete implements TabCompleter {

    private ArrayList<String> arg0List = new ArrayList<>(Arrays.asList("test", "resetall", "book", "status", "mod", "on", "off"));
    private ArrayList<String> arg1List = new ArrayList<>(Arrays.asList("list"));
    private ArrayList<String> arg2List = new ArrayList<>(Arrays.asList("on", "off"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        arg1List.addAll(Constants.modsList);

        if(args.length == 1){
            if(sender instanceof Player && !sender.isOp()){
                return arg0List.subList(2,arg0List.size());
            }
            return arg0List;
        }else if(args.length == 2 && args[0].equals("mod")){
            return arg1List;
        }else if(args.length == 3 && args[0].equals("mod") && Constants.modsList.contains(args[1])){
            return arg2List;
        }
        return null;
    }
}
