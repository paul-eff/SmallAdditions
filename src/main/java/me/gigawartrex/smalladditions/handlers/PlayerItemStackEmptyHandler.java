package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.InventoryManager;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for handling player deaths.
 *
 * @author Paul Ferlitz
 */
public class PlayerItemStackEmptyHandler implements Listener
{
    private final List<String> toolKeywords = Arrays.asList("shovel", "axe", "hoe", "shears", "steel", "charge", "brush", "compass");
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

        if (eventPlayer.getGameMode() != GameMode.SURVIVAL) return;

        // If current stack size = 1 -> when block is placed the stack will be empty
        if (eventItem.getAmount() == 1)
        {
            Material eventMat = eventItem.getType();
            if (toolKeywords.stream().anyMatch(s -> eventMat.toString().toLowerCase().contains(s))) return;
            InventoryManager im = new InventoryManager(eventPlayer);
            Integer[] replacementItems = im.getAllIndices(eventItem.getType());

            if (replacementItems.length > 1)
            {
                int a = im.getIndex(eventItem);
                int b = a;
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
                }, 1);
            }
        }
    }
}
