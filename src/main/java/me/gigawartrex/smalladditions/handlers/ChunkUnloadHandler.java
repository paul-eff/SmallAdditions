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

        if(chunk.getX() == 14 && chunk.getZ() == 11)
        {
            System.out.println("My chunk was unloaded - should be force loaded!");
        }
    }
}
