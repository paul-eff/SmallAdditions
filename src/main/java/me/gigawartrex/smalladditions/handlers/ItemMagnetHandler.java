package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemMagnetHandler
{
    Config config = new Config();

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
                    if (Boolean.parseBoolean(config.read("Config.Players." + player.getUniqueId() + ".Magnet")) && !player.isSneaking())
                    {
                        for (Entity ent : player.getNearbyEntities(8, 4, 8))
                        {
                            if (ent instanceof Item)
                            {
                                if (player.getLocation().distance(ent.getLocation()) >= 1.5)
                                {
                                    double volume = 0.5;
                                    double sourceVolume = Math.max(0.0, Math.min(volume, 1.0));
                                    double rolloffDistance = Math.max(16, 16 * volume);
                                    double distance = player.getLocation().distance(ent.getLocation());
                                    double volumeOfSoundAtPlayer = sourceVolume * (1 - distance / rolloffDistance) * 1.0;

                                    player.getInventory().addItem(((Item) ent).getItemStack()).forEach((slotNum, itemStack) -> {
                                        player.getLocation().getBlock().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                                    });
                                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) volumeOfSoundAtPlayer, 1.0F);
                                    ent.remove();
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Constants.plugin, 20L * 5, 20L * 5);
    }
}
