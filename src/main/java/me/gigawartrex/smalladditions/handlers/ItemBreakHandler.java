package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.main.MaterialPriority;
import me.gigawartrex.smalladditions.main.ToolType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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

        ArrayList<int[]> prioItems = new ArrayList<>();
        String toolType = "";
        // Find out tool type (if it is a tool)
        for (ToolType tt : ToolType.values())
        {
            if (eventMaterial.toString().toLowerCase(Locale.ROOT).contains(tt.getMaterialTypeSubstring()))
            {
                toolType = tt.getMaterialTypeSubstring();
                break;
            }
        }
        int targetInventorySlot = -1;
        // Add all tools of same type with material priority to an array
        for (int x = 0; x < inv.getStorageContents().length; x++)
        {
            ItemStack currItem = inv.getItem(x);
            if (currItem == null) continue;
            if(currItem.equals(eventItem) && targetInventorySlot == -1){
                targetInventorySlot = x;
                continue;
            }
            if (currItem.getType().toString().toLowerCase(Locale.ROOT).contains(toolType))
                prioItems.add(new int[]{x, MaterialPriority.getPriority(currItem)});
        }
        if (prioItems.size() > 0)
        {
            // Sort array from the lowest material priority to highest
            Collections.sort(prioItems, (o1, o2) -> o1[1] > o2[1] ? 1 : -1);
            //Replace broken item
            ItemStack temp = inv.getItem(prioItems.get(0)[0]);
            inv.setItem(prioItems.get(0)[0], new ItemStack(Material.AIR));
            inv.setItem(targetInventorySlot, temp);
            eventPlayer.getWorld().playSound(eventPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        }
    }
}
