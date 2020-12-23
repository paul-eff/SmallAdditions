package me.gigawartrex.smalladditions.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

  public static final Plugin plugin = SmallAdditions.getPlugin(SmallAdditions.class);
  public static final CommandSender console = Bukkit.getConsoleSender();
  public static final String name = plugin.getName();
  public static final ChatColor color = ChatColor.BLUE;
  public static final String version = plugin.getDescription().getVersion();

  public static final ArrayList<String> modsList = new ArrayList<>(Arrays.asList("Replant", "Autosmelt", "Fortune"));
}
