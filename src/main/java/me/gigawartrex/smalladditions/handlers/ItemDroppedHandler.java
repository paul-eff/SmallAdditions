package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
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

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.ROTTEN_FLESH, Material.STONE));

    private int maxMinerSize = 0;

    private Player eventPlayer;
    private Block eventBlockLocation;

    /*
     * Method to handle dropped items
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

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (drop.isOnGround())
                    {
                        boolean somethingFound = false;
                        while (drop.getItemStack().getAmount() > 0)
                        {
                            if (drop.getItemStack().getType() == Material.STONE)
                            {
                                for (Entity ent : drop.getNearbyEntities(1, 1, 1))
                                {
                                    if (ent instanceof Item)
                                    {
                                        Item item = (Item) ent;
                                        if (item.getItemStack().getType() == Material.ROTTEN_FLESH)
                                        {
                                            if (item.getItemStack().getAmount() > 1)
                                            {
                                                int newAmount = item.getItemStack().getAmount() - 1;
                                                item.setItemStack(new ItemStack(Material.ROTTEN_FLESH, newAmount));
                                            } else
                                            {
                                                item.remove();
                                            }
                                            somethingFound = true;
                                            break;
                                        }
                                    }
                                }
                            } else if (drop.getItemStack().getType() == Material.ROTTEN_FLESH)
                            {
                                for (Entity ent : drop.getNearbyEntities(1, 1, 1))
                                {
                                    if (ent instanceof Item)
                                    {
                                        Item item = (Item) ent;
                                        if (item.getItemStack().getType() == Material.STONE)
                                        {
                                            if (item.getItemStack().getAmount() > 1)
                                            {
                                                int newAmount = item.getItemStack().getAmount() - 1;
                                                item.setItemStack(new ItemStack(Material.STONE, newAmount));
                                            } else
                                            {
                                                item.remove();
                                            }
                                            somethingFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (somethingFound == true)
                            {
                                drop.getItemStack().setAmount(drop.getItemStack().getAmount() - 1);
                                drop.getWorld().dropItemNaturally(drop.getLocation(), new ItemStack(Material.COOKED_BEEF));
                                somethingFound = false;
                            } else
                            {
                                break;
                            }
                        }
                        if (drop.getItemStack().getAmount() == 0)
                        {
                            drop.remove();
                        } else
                        {
                            drop.setItemStack(new ItemStack(drop.getItemStack().getType(), drop.getItemStack().getAmount()));
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(Constants.plugin, 0, 2L);
        }
    }
}
