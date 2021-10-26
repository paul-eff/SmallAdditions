package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Class for handling item magnet functionality.
 *
 * @author Paul Ferlitz
 */
public class ItemMagnetHandler
{
    // Class variables
    Config config = new Config();

    /**
     * Class constructor.
     */
    public ItemMagnetHandler()
    {
        // Magnet Loop, picks up items for all players that have it activated.
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player player : Constants.plugin.getServer().getOnlinePlayers())
                {
                    // Sneaking disables magnet
                    if (Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Magnet")) && !player.isSneaking())
                    {
                        boolean playedSound = false;
                        boolean changesInWorld = false;

                        for (Entity ent : player.getNearbyEntities(8, 4, 8))
                        {
                            if (ent instanceof Item)
                            {
                                // Security distance so that players can still drop items reliably
                                if (player.getLocation().distance(ent.getLocation()) >= 1.5)
                                {
                                    ItemStack item = ((Item) ent).getItemStack();
                                    int amountBeforeAdd = item.getAmount();
                                    boolean onlyWholeStacks = true;

                                    for (Map.Entry<Integer, ItemStack> entry : player.getInventory().addItem(((Item) ent).getItemStack()).entrySet())
                                    {
                                        ItemStack reducedItem = new ItemStack(entry.getValue());

                                        if (amountBeforeAdd != reducedItem.getAmount())
                                        {
                                            ((Item) ent).setItemStack(reducedItem);
                                            changesInWorld = true;
                                        }
                                        onlyWholeStacks = false;
                                    }
                                    if (onlyWholeStacks)
                                    {
                                        ent.remove();
                                        changesInWorld = true;
                                    }
                                }
                                // Play sound on pickup
                                if (!playedSound && changesInWorld)
                                {
                                    double volume = 0.5;
                                    double sourceVolume = Math.max(0.0, Math.min(volume, 1.0));
                                    double rolloffDistance = Math.max(16, 16 * volume);
                                    double distance = player.getLocation().distance(ent.getLocation());
                                    double volumeOfSoundAtPlayer = sourceVolume * (1 - distance / rolloffDistance) * 1.0;
                                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) volumeOfSoundAtPlayer, 1.0F);
                                    playedSound = true;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Constants.plugin, 20L * 5, 20L * 3);
    }
}
