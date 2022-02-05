package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Class for handling when a player joins the server.
 *
 * @author Paul Ferlitz
 */
public class PlayerQuitHandler implements Listener
{
    // Class variables
    private final Config config = new Config();

    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)    {
        Player eventPlayer = event.getPlayer();
        eventPlayer.setSleepingIgnored(false);

        if (config.readPlayerAttributeStatus(eventPlayer, "Ninjajoin")) event.setQuitMessage("");
        if (config.readPlayerAttributeStatus(eventPlayer, "Hide"))
        {
            Constants.console.getServer().dispatchCommand(Constants.console, "sa hide " + eventPlayer.getName());
        }
        if (!eventPlayer.isOp())
        {
            config.writePlayerAttributeStatus(eventPlayer, "Ninjajoin", false);
            config.writePlayerAttributeStatus(eventPlayer, "Hide", false);
        }
    }
}
