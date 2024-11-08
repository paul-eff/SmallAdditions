package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player eventPlayer = event.getPlayer();
        eventPlayer.setSleepingIgnored(false);

        // Set default values for players that are first time joining
        if (config.read("Config.Players." + eventPlayer.getUniqueId()).equals(""))
        {
            config.write(config.getFileName() + ".Players." + eventPlayer.getUniqueId() + ".Name", eventPlayer.getName());

            config.writePlayerAttributeStatus(eventPlayer, "Book received", false);
            config.writePlayerAttributeStatus(eventPlayer, "Ninjajoin", false);
            config.writePlayerAttributeStatus(eventPlayer, "Hide", false);

            for (String mod : Constants.modsList)
            {
                if (mod.equals("Veining") || mod.equals("Replant"))
                {
                    config.writeModStatus(eventPlayer, mod, true);
                } else
                {
                    config.writeModStatus(eventPlayer, mod, false);
                }
            }
        }

        if (config.readPlayerAttributeStatus(eventPlayer, "Ninjajoin")) event.setJoinMessage("");
        if (config.readPlayerAttributeStatus(eventPlayer, "Hide"))
        {
            Constants.console.getServer().dispatchCommand(Constants.console, "sa hide " + eventPlayer.getName());
        }

        // Check if he already received book, if not add
        if (!config.readPlayerAttributeStatus(eventPlayer, "Book received"))
        {
            Book refBook = new Book("SmallAdditions Guide", "GigaWarTr3x", "README");
            boolean add = true;
            boolean freeSlot = false;

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
            // If all conditions are met, spawn a book in player's inventory
            if (add && freeSlot)
            {
                eventPlayer.getInventory().addItem(refBook);
                config.writePlayerAttributeStatus(eventPlayer, "Book received", true);
            }
        }
    }
}
