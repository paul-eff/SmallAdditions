package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Class for handling when a player enters a bed.
 *
 * @author Paul Ferlitz
 */
public class PlayerEnteredBedHandler implements Listener
{
    // Class variables
    private final MessageHelper msghelp = new MessageHelper();
    private final Config config = new Config();

    private float percentageNeeded;
    private boolean oneWillWakeUp = false;

    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event)
    {
        Player eventPlayer = event.getPlayer();
        percentageNeeded = Float.parseFloat(config.read("Config.Settings.serverPercentageSleepingForSkip"));

        ArrayList<Player> playersInSameWorld = new ArrayList<>();
        int playersSleeping = 0;
        // Save all players in same world as sleeping player
        for (Player player : Constants.console.getServer().getOnlinePlayers())
        {
            if (player.getWorld() == eventPlayer.getWorld())
            {
                playersInSameWorld.add(player);
                if (player.isSleeping()) playersSleeping++;
            }
        }

        int finalPlayersSleeping = playersSleeping;
        // Runnable to schedule messages
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (eventPlayer.isSleeping() && !oneWillWakeUp)
                {
                    // eventPlayer needs to be added manually as he is not yet sleeping during line 31-38
                    int playersSleeping = finalPlayersSleeping + 1;
                    float percentageSleeping = (playersSleeping * 1.0f) / (playersInSameWorld.size() * 1.0f);
                    int playersNeeded = (int) Math.ceil((percentageNeeded / (percentageSleeping / playersSleeping))) - playersSleeping;
                    // Go over all players in same world (list)
                    for (Player player : playersInSameWorld)
                    {
                        // For all not event players send messages
                        if (player != eventPlayer)
                        {
                            if (percentageSleeping < percentageNeeded)
                            {
                                msghelp.sendPlayer(player, eventPlayer.getName() + " just went to bed! " + ChatColor.WHITE + "(" + ChatColor.RED + playersNeeded + ChatColor.WHITE + " more needed)", ChatColor.GOLD);
                            } else
                            {
                                msghelp.sendPlayer(player, eventPlayer.getName() + " just went to bed! Good night...", ChatColor.GOLD);
                            }
                        // And for event players also send messages
                        } else
                        {
                            if (percentageSleeping < percentageNeeded)
                            {
                                msghelp.sendPlayer(player, "Good night " + player.getName() + "! " + ChatColor.WHITE + "(" + ChatColor.RED + playersNeeded + ChatColor.WHITE + " more needed)", ChatColor.GOLD);
                            } else
                            {
                                msghelp.sendPlayer(player, "Good night " + player.getName() + "!", ChatColor.GOLD);
                            }
                        }
                    }
                    // If sleeping threshold is reached
                    if (percentageSleeping >= percentageNeeded && !oneWillWakeUp)
                    {
                        // Set oneWillWakeUp to true so that this action is only called one time
                        oneWillWakeUp = true;
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                // Set world time to morning and wake alle players
                                eventPlayer.getWorld().setTime(0);
                                for (Player player : playersInSameWorld)
                                {
                                    msghelp.sendPlayer(player, "Wakey-wakey, rise and shine!", ChatColor.GOLD);
                                }
                                oneWillWakeUp = false;
                            }
                        }.runTaskLater(Constants.plugin, 20L * 3);
                    }
                }
            }
        }.runTaskLater(Constants.plugin, 20L * 2);
    }
}
