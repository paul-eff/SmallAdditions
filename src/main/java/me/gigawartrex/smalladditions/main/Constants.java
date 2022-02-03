package me.gigawartrex.smalladditions.main;

import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.helpers.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for holding plugin wide constants.
 *
 * @author Paul Ferlitz
 */
public class Constants
{
    // Plugin wide essentials
    public static final Plugin plugin = SmallAdditions.getPlugin(SmallAdditions.class);
    public static final CommandSender console = Bukkit.getConsoleSender();
    public static final String name = plugin.getName();
    public static final ChatColor color = ChatColor.BLUE;
    public static final String version = plugin.getDescription().getVersion();

    // Specific constants
    public static final ArrayList<String> modsList = new ArrayList<>(Arrays.asList("Magnet", "Replant"));

    // Menus
    public static final IconMenu mainMenu = new MainMenu().generateMenu();
}
