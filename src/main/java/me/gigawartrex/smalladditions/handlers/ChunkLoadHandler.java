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

        if(chunk.getX() == 14 && chunk.getZ() == 11)
        {
            System.out.println("My chunk was loaded");
        }
    }
}
