package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        boolean doCreeperGriefing = Boolean.parseBoolean(config.read("Config.Settings.doCreeperGriefing"));

        if (!doCreeperGriefing && event.getEntity() != null && event.getEntity().getType() == EntityType.CREEPER)
        {
            event.blockList().clear();
            Block eventBlock = event.getLocation().getBlock();
            // TODO: Message when debug levels are implemented
            //msghelp.sendConsole("Stopped a creeper from exploding at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);
        }
    }
}