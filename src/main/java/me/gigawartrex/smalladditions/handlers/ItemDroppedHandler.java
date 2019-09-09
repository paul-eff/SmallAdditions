package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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

    /*
     * Method to handle dropped items
     */
    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event)
    {

        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();

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
                        int stackSize = drop.getItemStack().getAmount();
                        while (stackSize > 0)
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
                                            } else if (item.getItemStack().getAmount() == 1)
                                            {
                                                item.getItemStack().setAmount(0);
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
                                            } else if (item.getItemStack().getAmount() == 1)
                                            {
                                                item.getItemStack().setAmount(0);
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
                            if (somethingFound)
                            {
                                stackSize--;
                                drop.getWorld().dropItemNaturally(drop.getLocation(), new ItemStack(Material.COOKED_BEEF));
                                somethingFound = false;
                            } else
                            {
                                break;
                            }
                        }
                        if (stackSize == 0)
                        {
                            drop.remove();
                        } else
                        {
                            drop.setItemStack(new ItemStack(drop.getItemStack().getType(), stackSize));
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(Constants.plugin, 0, 2L);
        }
    }
}
