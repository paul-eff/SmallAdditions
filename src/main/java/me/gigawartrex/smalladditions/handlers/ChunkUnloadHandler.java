package me.gigawartrex.smalladditions.handlers;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadHandler implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event)
    {
        Chunk chunk = event.getChunk();
        //System.out.println("[UNLOAD] " + chunk.toString() + " just unloaded!");

        if(chunk.getX() == 14 && chunk.getZ() == 11)
        {
            System.out.println("My chunk was unloaded - should be force loaded!");
        }

        /*new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!cc.isLoaded())
                {
                    if (cc.load())
                    {
                        System.out.println("Loaded");
                    } else
                    {
                        System.out.println("Load failed");
                    }
                } else
                {
                    System.out.println("Still loaded");
                }
            }
        }.runTaskTimer(Constants.plugin, 20L * 5, 20L * 5);*/
    }
}
