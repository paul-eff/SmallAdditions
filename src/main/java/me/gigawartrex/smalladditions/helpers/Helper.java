package me.gigawartrex.smalladditions.helpers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Class housing all sorts of helpers for ingame Minecraft operations.
 *
 * @author Paul Ferlitz
 * @version 1.0 2020-12-29 Initial Version
 * @since 1.0
 */
public class Helper
{
    /**
     * Generates a random number inclusive between min and max.
     *
     * @param min Minimum Number generated.
     * @param max Maximum Number generated.
     * @return The randomly generated number.
     * @since 1.0
     */
    public static int randNumFromRange(int min, int max)
    {
        return (int) (Math.random() * (max - min) + 1) + min;
    }

    /**
     * Offsets the given location by the entered values.
     *
     * @param loc Original location to be edited.
     * @param x   X offset.
     * @param y   Y offset.
     * @param z   Z offset.
     * @return The new Location.
     * @since 1.0
     */
    public static Location getLocation(Player player, Location loc, int x, int y, int z)
    {
        return new Location(player.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
    }
}
