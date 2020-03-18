package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.itemmenu.IconMenu;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
    private Leveling leveling;
    private Action action;

    /*
     * Method to handle dropped items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //Load needed classes
        if(eventPlayer == null)
        {
            config = new Config();
            msghelp = new MessageHelper();
            eventPlayer = event.getPlayer();
            leveling = new Leveling(eventPlayer);
            action = event.getAction();
        }

        boolean validItem = false;

        for(String str : Arrays.asList("_SHOVEL", "_AXE", "_PICKAXE"))
        {
            if(eventPlayer.getInventory().getItemInMainHand().getType().toString().contains(str))
            {
                validItem = true;
                break;
            }
        }

        if(validItem)
        {
            if(eventPlayer.isSneaking() && action.equals(Action.RIGHT_CLICK_AIR))
            {
                //Code again in sa.java
            }
        }
        eventPlayer = null;
    }
}
