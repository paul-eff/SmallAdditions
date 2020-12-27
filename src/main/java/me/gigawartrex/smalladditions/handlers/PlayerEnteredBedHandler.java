package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class PlayerEnteredBedHandler implements Listener
{
    private MessageHelper msghelp;
    private final float percentageNeeded = 1f;

    private boolean oneWillWakeUp = false;

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event)
    {
        //Load needed classes
        msghelp = new MessageHelper();
        Player eventPlayer = event.getPlayer();

        ArrayList<Player> playersInSameWorld = new ArrayList<>();
        int playersSleeping = 0;

        for (Player player : Constants.console.getServer().getOnlinePlayers())
        {
            if (player.getWorld() == eventPlayer.getWorld())
            {
                playersInSameWorld.add(player);
                if (player.isSleeping()) playersSleeping++;
            }
        }

        int finalPlayersSleeping = playersSleeping;
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

                    for (Player player : playersInSameWorld)
                    {
                        if (player != eventPlayer)
                        {
                            if (percentageSleeping < percentageNeeded)
                            {
                                msghelp.sendPlayer(player, eventPlayer.getName() + " just went to bed! " + ChatColor.WHITE + "(" + ChatColor.RED + playersNeeded + ChatColor.WHITE + " more needed)", ChatColor.GOLD);
                            } else
                            {
                                msghelp.sendPlayer(player, eventPlayer.getName() + " just went to bed! Good night...", ChatColor.GOLD);
                            }
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
                    if (percentageSleeping >= percentageNeeded && !oneWillWakeUp)
                    {
                        oneWillWakeUp = true;
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
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
