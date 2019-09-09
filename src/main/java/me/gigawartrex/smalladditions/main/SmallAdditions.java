package me.gigawartrex.smalladditions.main;

import me.gigawartrex.smalladditions.commands.sa;
import me.gigawartrex.smalladditions.commands.saTabComplete;
import me.gigawartrex.smalladditions.files.BookWriter;
import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.handlers.*;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmallAdditions extends JavaPlugin {

    private MessageHelper msghelp;

    //TODO: Ore multiplication (macerator)
    //TODO: Right click Wheat to harvest and replant

    @Override
    public void onEnable() {

        msghelp = new MessageHelper();

        //Command registration
        //getCommand("sa").setExecutor(new sa());
        //getCommand("sa").setTabCompleter(new saTabComplete());

        //File registration
        Config config = new Config();
        config.defaultConfig(false);
        BookWriter bookWriter = new BookWriter();
        bookWriter.defaultBook("SmallAdditions Guide", false);

        //Event registration
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new ItemDroppedHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEnteredBedHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerRightClickHandler(), this);

        msghelp.sendConsole("Successfully enabled " + Constants.name + " Version " + Constants.version, ChatColor.GREEN); // Enabled Message
    }

    @Override
    public void onDisable() {
        msghelp.sendConsole("Successfully disabled " + Constants.name + " Version " + Constants.version, ChatColor.RED); // Disabled Message
    }
}
