package me.gigawartrex.smalladditions.main;

import me.gigawartrex.smalladditions.commands.Sa;
import me.gigawartrex.smalladditions.commands.SaTabComplete;
import me.gigawartrex.smalladditions.handlers.*;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.BookWriter;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The plugin's main class/entry point.
 *
 * @author Paul Ferlitz
 */
public final class SmallAdditions extends JavaPlugin
{
    // Class variables
    private MessageHelper msghelp;

    /**
     * Method that fires on every "load" of the plugin (e.g. server start/reload)
     */
    @Override
    public void onEnable()
    {
        msghelp = new MessageHelper();

        //Command registration
        getCommand("sa").setExecutor(new Sa());
        getCommand("sa").setTabCompleter(new SaTabComplete());

        //File registration
        Config config = new Config();
        config.defaultConfig(false);
        BookWriter bookWriter = new BookWriter();
        bookWriter.defaultBook("SmallAdditions Guide", false);

        //Event registration
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new ItemBreakHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockChoppedHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockMinedHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockDugHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEnteredBedHandler(), this);

        //getServer().getPluginManager().registerEvents(new ChunkUnloadHandler(), this);
        //getServer().getPluginManager().registerEvents(new ChunkLoadHandler(), this);
        //getServer().getPluginManager().registerEvents(new WorldUnloadHandler(), this);
        //getServer().getPluginManager().registerEvents(new WorldLoadHandler(), this);

        //Other Handler registration
        new ItemMagnetHandler();
        //new ChunkLoaderHandler();

        msghelp.sendConsole("Successfully enabled " + Constants.name + " Version " + Constants.version, ChatColor.GREEN); // Enabled Message
    }

    /**
     * Method that fires on every "unload" of the plugin (e.g. server stop/reload)
     */
    @Override
    public void onDisable()
    {
        msghelp.sendConsole("Successfully disabled " + Constants.name + " Version " + Constants.version, ChatColor.RED); // Disabled Message
    }
}
