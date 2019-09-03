package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemDroppedHandler implements Listener {

    private Config config;
    private MessageHelper msghelp;

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.ROTTEN_FLESH, Material.STONE)); // Allowed Tools

    private int maxMinerSize = 0;

    private Player eventPlayer;
    private Block eventBlockLocation;

    /*
     * Method to handle tree felling onBlockBreak event
     */
    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event) {

        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getPlayer();

        //if (event.getPlayer().isSneaking()) {
            //Check if player is dropping valid item
            if (allowedItems.contains(event.getItemDrop().getItemStack().getType())) {

                Item drop = event.getItemDrop();
                msghelp.sendConsole(event.getItemDrop().getItemStack().getType().toString()+" dropped!", ChatColor.BLUE);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (drop.isOnGround()) {
                            eventBlockLocation = event.getItemDrop().getLocation().getBlock().getRelative(BlockFace.UP);
                            System.out.println("????");
                            this.cancel();
                        }

                    }
                }.runTaskTimer(Constants.plugin, 0, 1L);

                System.out.println(eventBlockLocation.getX());

                /*if(eventBlockLocation.getDrops().size() >= 2){
                    if(eventBlockLocation.getDrops().contains(Material.STONE) && eventBlockLocation.getDrops().contains(Material.ROTTEN_FLESH)) {
                        for (ItemStack item : eventBlockLocation.getDrops()) {
                            msghelp.sendConsole("FOUND: "+item.getType().toString(), ChatColor.BLUE);
                        }
                    }
                }*/
            }
        //}
    }

    /**
     * Damage a given item for one damage point
     *
     * @param player The player which's item should be damaged
     * @param item   The item which should be damaged
     */
    private void damageItem(Player player, ItemStack item) {

        item.setDurability((short) (item.getDurability() + 1));
        if (item.getDurability() >= item.getType().getMaxDurability()) {

            player.getInventory().removeItem(item);
        } else {

            player.getInventory().setItemInMainHand(item);
        }
    }
}
