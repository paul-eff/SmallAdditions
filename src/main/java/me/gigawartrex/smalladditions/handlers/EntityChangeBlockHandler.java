package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlockHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event)
    {
        boolean doEndermanGriefing = Boolean.parseBoolean(config.read("Config.Settings.doEndermanGriefing"));

        if (!doEndermanGriefing && event.getEntity() != null && event.getEntity().getType() == EntityType.ENDERMAN)
        {
            event.setCancelled(true);
            Block eventBlock = event.getBlock();
            // TODO: Message when debug levels are implemented
            //msghelp.sendConsole("An Enderman was stopped from picking up a block at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);
        }
    }
}