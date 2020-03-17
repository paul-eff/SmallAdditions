package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemBreakHandler implements Listener
{

    private Config config;
    private MessageHelper msghelp;

    private Player eventPlayer;
    private ItemStack eventItem;
    private Material eventMaterial;

    /*
     * Method to handle tree felling onBlockBreak event
     */
    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event)
    {
        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();

        eventPlayer = event.getPlayer();
        eventItem = event.getBrokenItem();
        eventMaterial = event.getBrokenItem().getType();

        PlayerInventory inv = eventPlayer.getInventory();
        boolean itemSwapped = false;
        for(int x = 0; x < inv.getStorageContents().length; x++)
        {
            //TODO: Implent so that armor also is affected
            ItemStack currItem = inv.getItem(x);
            if(currItem != null)
            {
                if (!currItem.equals(eventItem) && eventMaterial.equals(currItem.getType()))
                {
                    for(int y = 0; y < inv.getStorageContents().length; y++)
                    {
                        ItemStack temp = inv.getItem(y);
                        if(temp != null)
                        {
                            if (temp.equals(eventItem))
                            {
                                inv.setItem(x, new ItemStack(Material.AIR));
                                int finalY = y;
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Constants.plugin, new Runnable() {
                                    public void run() {
                                        inv.setItem(finalY, currItem);
                                        eventPlayer.getWorld().playSound(eventPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    }
                                }, 5L);
                                itemSwapped = true;
                                break;
                            }
                        }
                    }
                }
            }
            if(itemSwapped)
            {
                break;
            }
        }
    }
}
