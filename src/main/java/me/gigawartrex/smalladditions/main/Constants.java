package me.gigawartrex.smalladditions.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

  public static Plugin plugin = SmallAdditions.getPlugin(SmallAdditions.class);
  public static CommandSender console = Bukkit.getConsoleSender();
  public static String name = plugin.getName();
  public static ChatColor color = ChatColor.BLUE;
  public static String version = plugin.getDescription().getVersion();

  public static ArrayList<String> modsList = new ArrayList<>(Arrays.asList("Replant", "Autosmelt", "Fortune"));
}
