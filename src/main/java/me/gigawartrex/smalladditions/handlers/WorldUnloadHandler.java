package me.gigawartrex.smalladditions.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldUnloadHandler implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent e)
    {
        e.setCancelled(true);
        System.out.println("Canceled world unloading!");
    }
}
