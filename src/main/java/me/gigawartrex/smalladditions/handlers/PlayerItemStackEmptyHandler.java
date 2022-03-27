package me.gigawartrex.smalladditions.handlers;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Class for handling player deaths.
 *
 * @author Paul Ferlitz
 */
public class PlayerItemStackEmptyHandler implements Listener
{
    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event)
    {
        // Initialize common needed variables
        Player eventPlayer = event.getPlayer();
        ItemStack eventItem = event.getItemInHand();

        if(eventPlayer.getGameMode() != GameMode.SURVIVAL) return;
        if(eventItem.getType() == Material.POWDER_SNOW_BUCKET) return;

        // If current stack size = 1 -> when block is placed the stack will be empty
        if (eventItem.getAmount() == 1)
        {
            // Get inventory
            Inventory inv = eventPlayer.getInventory();
            int targetInventorySlot = -1;
            int sourceInventorySlot = -1;
            // Iterate over inventory to determine target slot and where to get new ItemStack from
            for (int x = 0; x < inv.getStorageContents().length; x++)
            {
                if (targetInventorySlot != -1 && sourceInventorySlot != -1) break;

                ItemStack currItem = inv.getItem(x);
                if (currItem == null) continue;
                if (currItem.equals(eventItem) && targetInventorySlot == -1)
                {
                    targetInventorySlot = x;
                    continue;
                }
                if (currItem.getType().equals(eventItem.getType()) && sourceInventorySlot == -1 && targetInventorySlot != x)
                {
                    sourceInventorySlot = x;
                    continue;
                }
            }
            if (targetInventorySlot != -1 && sourceInventorySlot != -1)
            {
                // Do the replace
                ItemStack temp = inv.getItem(sourceInventorySlot);
                inv.setItem(sourceInventorySlot, new ItemStack(Material.AIR));
                inv.setItem(targetInventorySlot, temp);
                eventPlayer.getWorld().playSound(eventPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
            }
        }
    }
}
