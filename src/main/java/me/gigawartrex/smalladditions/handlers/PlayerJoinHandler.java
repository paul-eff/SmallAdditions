package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Class for handling when a player joins the server.
 *
 * @author Paul Ferlitz
 */
public class PlayerJoinHandler implements Listener
{
    // Class variables
    private final Config config = new Config();

    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player eventPlayer = event.getPlayer();
        eventPlayer.setSleepingIgnored(false);

        // Set default values for players that are first time joining
        if (config.read("Config.Players." + eventPlayer.getUniqueId()).equals(""))
        {
            config.write(config.getFileName() + ".Players." + eventPlayer.getUniqueId() + ".Name", eventPlayer.getName());
            config.write(config.getFileName() + ".Players." + eventPlayer.getUniqueId() + ".Book received", "" + false);
            config.write(config.getFileName() + ".Players." + eventPlayer.getUniqueId() + ".Ninjajoin", "" + false);
            config.write(config.getFileName() + ".Players." + eventPlayer.getUniqueId() + ".Hide", "" + false);

            for (String mod : Constants.modsList)
            {
                config.writeModStatus(eventPlayer, mod, false);
            }
        }

        // For ninjajoin
        if (Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Ninjajoin"))) event.setJoinMessage("");
        if (Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Hide")))
        {
            Constants.console.getServer().dispatchCommand(Constants.console, "sa hide " + eventPlayer.getName());
        }
        if (!eventPlayer.isOp())
        {
            config.write("Config.Players." + eventPlayer.getUniqueId() + ".Ninjajoin", "" + false);
            // TODO: Implement LeaveHandler to remove from list if an admin forgets!
            config.write("Config.Players." + eventPlayer.getUniqueId() + ".Hide", "" + false);
        }

        boolean add = true;
        boolean freeSlot = false;
        Book refBook = new Book("SmallAdditions Guide", "GigaWarTr3x", "README");

        // Check if player has a free slot for starter book
        for (ItemStack stack : eventPlayer.getInventory().getStorageContents())
        {
            if (stack != null)
            {
                if (stack.getType().equals(Material.WRITTEN_BOOK))
                {

                    BookMeta meta = (BookMeta) stack.getItemMeta();

                    if (meta.getTitle().equals(refBook.getMeta().getTitle()))
                    {
                        if (meta.getAuthor().equals(refBook.getMeta().getAuthor()))
                        {
                            if (meta.getPages().equals(refBook.getMeta().getPages()))
                            {
                                add = false;
                            }
                        }
                    }
                }
            } else if (!freeSlot)
            {
                freeSlot = true;
            }
        }

        // Check if he already received book
        boolean receivedBook = Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Book received"));

        // If all conditions are met, spawn a book in player's inventory
        if (add && freeSlot && !receivedBook)
        {
            eventPlayer.getInventory().addItem(refBook);
            config.write("Config.Players." + eventPlayer.getUniqueId() + ".Book received", "" + true);
        }
    }
}
