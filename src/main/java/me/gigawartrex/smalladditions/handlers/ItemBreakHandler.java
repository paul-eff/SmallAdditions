package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event)
    {
        // Initialize common needed variables
        Player eventPlayer = event.getPlayer();
        ItemStack eventItem = event.getBrokenItem();
        Material eventMaterial = event.getBrokenItem().getType();

        PlayerInventory inv = eventPlayer.getInventory();
        boolean itemSwapped = false;

        // Loop over player inventory
        for (int x = 0; x < inv.getStorageContents().length; x++)
        {
            ItemStack currItem = inv.getItem(x);
            // If slot not empty
            if (currItem != null)
            {
                // Check if item type is same
                if (!currItem.equals(eventItem) && eventMaterial.equals(currItem.getType()))
                {
                    // Iterate over items until broken item (slot) is found
                    for (int y = 0; y < inv.getStorageContents().length; y++)
                    {
                        ItemStack temp = inv.getItem(y);
                        if (temp != null)
                        {
                            if (temp.equals(eventItem))
                            {
                                // Replace broken item with new one
                                inv.setItem(x, new ItemStack(Material.AIR));
                                int finalY = y;
                                //If errors, before: Bukkit.getScheduler().scheduleSyncDelayedTask(Constants.plugin, new Runnable()
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Constants.plugin, () -> {
                                    inv.setItem(finalY, currItem);
                                    eventPlayer.getWorld().playSound(eventPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                }, 5L);
                                itemSwapped = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (itemSwapped)
            {
                break;
            }
        }
    }
}
