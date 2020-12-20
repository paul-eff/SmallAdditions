package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemBreakHandler implements Listener
{
    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event)
    {
        Player eventPlayer = event.getPlayer();
        ItemStack eventItem = event.getBrokenItem();
        Material eventMaterial = event.getBrokenItem().getType();

        PlayerInventory inv = eventPlayer.getInventory();
        boolean itemSwapped = false;

        for (int x = 0; x < inv.getStorageContents().length; x++)
        {
            //TODO: Implement so that armor also is affected
            //TODO: Implement that all items are affected
            ItemStack currItem = inv.getItem(x);

            if (currItem != null)
            {
                if (!currItem.equals(eventItem) && eventMaterial.equals(currItem.getType()))
                {
                    for (int y = 0; y < inv.getStorageContents().length; y++)
                    {
                        ItemStack temp = inv.getItem(y);
                        if (temp != null)
                        {
                            if (temp.equals(eventItem))
                            {
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
