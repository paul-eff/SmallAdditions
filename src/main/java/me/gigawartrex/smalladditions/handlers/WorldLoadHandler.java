package me.gigawartrex.smalladditions.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadHandler implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldLoadEvent event)
    {
        try
        {
            System.out.println("Trying to load chunk X:14 Y:11!");
            Bukkit.getServer().getWorld(event.getWorld().getUID()).loadChunk(14, 11);
            Bukkit.getServer().getWorld(event.getWorld().getUID()).setChunkForceLoaded(14, 11, true);
        } catch (NullPointerException ex)
        {
            System.out.println("World '" + event.getWorld().getName() + "' doesn't exist, or isn't loaded in memory.");
        }
    }
}
