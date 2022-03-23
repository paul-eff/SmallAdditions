package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityLaunchHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    @EventHandler
    public void onEntityExplode(ProjectileHitEvent event)
    {
        boolean doGhastGriefing = Boolean.parseBoolean(config.read("Config.Settings.doGhastGriefing"));

        if (!doGhastGriefing && event.getEntity() != null)
        {
            ProjectileSource shooter = event.getEntity().getShooter();
            if (shooter != null)
            {
                if (shooter instanceof Ghast && event.getEntity().getType() == EntityType.FIREBALL)
                {
                    ((Fireball)event.getEntity()).setYield(0);
                    Block eventBlock = event.getHitBlock();
                    // TODO: Message when debug levels are implemented
                    //msghelp.sendConsole("Stopped a Ghast from doing explosion damage at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);
                }
            }
        }
    }
}