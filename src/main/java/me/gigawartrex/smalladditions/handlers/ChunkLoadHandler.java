package me.gigawartrex.smalladditions.handlers;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadHandler implements Listener
{
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event)
    {
        Chunk chunk = event.getChunk();
        System.out.println("[LOAD] " + chunk.toString() + " just loaded!");

        if (chunk.unload())
        {
            System.out.println("[REUNLOAD] " + chunk.toString() + " loaded again");
        } else
        {
            System.out.println("[REUNLOAD] " + chunk.toString() + " load failed");
        }
    }
}
