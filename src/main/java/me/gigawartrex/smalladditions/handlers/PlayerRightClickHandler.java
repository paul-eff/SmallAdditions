package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerRightClickHandler implements Listener
{

    private Config config;
    private MessageHelper msghelp;

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WHEAT));

    private Player eventPlayer;
    private Block eventBlock;

    /*
     * Method to handle dropped items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getPlayer();
        eventBlock = event.getClickedBlock();

        if (allowedItems.contains(eventBlock.getType()))
        {
            System.out.println(eventBlock.getState().getData());

            if (eventBlock.getData() == (byte) 7)
            {
                for (ItemStack item : eventBlock.getDrops())
                {
                    eventBlock.getWorld().dropItemNaturally(eventBlock.getLocation(), item);
                }
                eventBlock.getState().setRawData((byte) 0);
            }
        }
    }
}
