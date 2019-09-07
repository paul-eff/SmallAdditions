package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemDroppedHandler implements Listener
{

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
    public void onItemDropped(PlayerDropItemEvent event)
    {

        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getPlayer();

        if (allowedItems.contains(event.getItemDrop().getItemStack().getType()))
        {
            Item drop = event.getItemDrop();
            System.out.println(drop.getItemStack().getType() + " was dropped");

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (drop.isOnGround())
                    {
                        //msghelp.sendPlayer(eventPlayer, drop.getName() + " is on ground at " + drop.getLocation().toString(), ChatColor.RED);
                        boolean foundFlesh = false;
                        boolean foundStone = false;
                        int fleshIndex = 0;
                        int stoneIndex = 0;
                        ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) drop.getNearbyEntities(1, 1, 1);
                        String outString = "";
                        for (Entity ent : nearbyEntities)
                        {
                            outString += ent.getType() + ", ";
                        }
                        System.out.println("On ground " + outString);
                        for (Entity nearDrop : nearbyEntities)
                        {
                            if (nearDrop.getType().equals(Material.ROTTEN_FLESH))
                            {
                                System.out.println("found flesh");
                                foundFlesh = true;
                                fleshIndex = nearbyEntities.indexOf(nearDrop);
                            } else if (nearDrop.getType().equals(Material.STONE))
                            {
                                System.out.println("found stone");
                                foundStone = true;
                                stoneIndex = nearbyEntities.indexOf(nearDrop);
                            }
                            if (foundFlesh && foundStone)
                            {
                                drop.getWorld().dropItemNaturally(drop.getLocation(), new ItemStack(Material.PORKCHOP));
                            }
                        }
                        if (foundFlesh && foundStone)
                        {
                            nearbyEntities.get(fleshIndex).remove();
                            nearbyEntities.get(stoneIndex).remove();
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(Constants.plugin, 20L, 20L);
        }
    }

    /**
     * Damage a given item for one damage point
     *
     * @param player The player which's item should be damaged
     * @param item   The item which should be damaged
     */
    private void damageItem(Player player, ItemStack item)
    {
        item.setDurability((short) (item.getDurability() + 1));
        if (item.getDurability() >= item.getType().getMaxDurability())
        {

            player.getInventory().removeItem(item);
        } else
        {

            player.getInventory().setItemInMainHand(item);
        }
    }

    private ArrayList<Item> getNearbyDroppedItems(Item item)
    {
        ArrayList<Item> nearbyDrops = new ArrayList<>();
        nearbyDrops.add(item);
        Block itemLoc = item.getLocation().getBlock();


        return nearbyDrops;
    }
}
