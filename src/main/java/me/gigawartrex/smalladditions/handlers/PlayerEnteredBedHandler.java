package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerEnteredBedHandler implements Listener
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
    public void onBedEnter(PlayerBedEnterEvent event)
    {

        System.out.println("Init");
        //Load needed classes
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getPlayer();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (eventPlayer.isSleeping())
                {
                    int playersSleeping = 0;
                    ArrayList<Player> playersInSameWorld = new ArrayList<>();

                    for (Player player : Constants.console.getServer().getOnlinePlayers())
                    {
                        if (player.getWorld() == eventPlayer.getWorld())
                        {
                            playersInSameWorld.add(player);
                            if (player.isSleeping())
                            {
                                playersSleeping++;
                            }
                        }
                    }

                    double percentageSleeping = ((playersSleeping * 1.0) / (playersInSameWorld.size() * 1.0));

                    for (Player player : playersInSameWorld)
                    {
                        msghelp.sendPlayer(player, playersSleeping + "/" + playersInSameWorld.size() + " (" + (((int) (percentageSleeping * 10000)) * 1.0) / 100 + "%) Players in this World are sleeping.", ChatColor.GOLD);
                    }

                    if (percentageSleeping >= 0.5)
                    {
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                eventPlayer.getWorld().setTime(0);
                                for (Player player : playersInSameWorld)
                                {
                                    msghelp.sendPlayer(player, "Wakey wakey, rise and shine!", ChatColor.GOLD);
                                }
                            }
                        }.runTaskLater(Constants.plugin, 20L * 2);
                    }
                }
            }
        }.runTaskLater(Constants.plugin, 20L * 6);
    }
}
