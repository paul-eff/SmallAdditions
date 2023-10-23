package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.InventoryManager;
import me.gigawartrex.smalladditions.main.Constants;
import me.gigawartrex.smalladditions.main.MaterialPriority;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Priority;
import java.util.Arrays;

/**
 * Class for handling when an item breaks.
 *
 * @author Paul Ferlitz
 */
public class ItemBreakHandler implements Listener
{
    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onItemBreak(PlayerItemBreakEvent event)
    {
        // Initialize common needed variables
        Player eventPlayer = event.getPlayer();
        ItemStack eventItem = event.getBrokenItem();

        if (eventPlayer.getGameMode() != GameMode.SURVIVAL) return;

        InventoryManager im = new InventoryManager(eventPlayer);
        Integer[] replacementItems = im.getAllIndicesFuzzy(eventItem.getType());

        if (replacementItems.length > 1)
        {
            int a = im.getIndex(eventItem);
            int b = a;

            if (replacementItems.length > 2)
            {
                Arrays.sort(replacementItems, (a1, b1) -> {
                    Inventory inv = eventPlayer.getInventory();
                    return MaterialPriority.getPriority(inv.getItem(a1)) > MaterialPriority.getPriority(inv.getItem(b1)) ? 1 : -1;
                });
            }

            int iterator = 0;
            while (b == a)
            {
                b = replacementItems[iterator];
                iterator++;
            }
            if (a == -1 || b == -1) return;
            int finalB = b;
            Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
            {
                im.swapItems(a, finalB);
                eventPlayer.getWorld().playSound(eventPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
            }, 2);
        }
    }
}
