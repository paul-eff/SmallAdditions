package me.gigawartrex.smalladditions.main;

import me.gigawartrex.smalladditions.commands.sa;
import me.gigawartrex.smalladditions.commands.saTabComplete;
import me.gigawartrex.smalladditions.files.BookWriter;
import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.handlers.BlockChoppedHandler;
import me.gigawartrex.smalladditions.handlers.BlockMinedHandler;
import me.gigawartrex.smalladditions.handlers.ItemDroppedHandler;
import me.gigawartrex.smalladditions.handlers.PlayerJoinHandler;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmallAdditions extends JavaPlugin {

    private MessageHelper msghelp;

    //TODO: Rotten Flesh to Meat
    //TODO: Ore multiplication (macerator)
    //TODO: Not all in bed (50% -> Daytime)
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
        //getServer().getPluginManager().registerEvents(new BlockChoppedHandler(), this);
        //getServer().getPluginManager().registerEvents(new BlockMinedHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new ItemDroppedHandler(), this);

        msghelp.sendConsole("Successfully enabled " + Constants.name + " Version " + Constants.version, ChatColor.GREEN); // Enabled Message
    }

    @Override
    public void onDisable() {
        msghelp.sendConsole("Successfully disabled " + Constants.name + " Version " + Constants.version, ChatColor.RED); // Disabled Message
    }
}
