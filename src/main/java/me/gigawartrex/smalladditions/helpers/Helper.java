package me.gigawartrex.smalladditions.helpers;

import org.bukkit.Location;

/**
 * Class housing all sorts of helpers for ingame Minecraft operations.
 *
 * @author Paul Ferlitz
 */
public class Helper
{
    /**
     * Generates a random number inclusive between min and max.
     *
     * @param min the smallest possible number
     * @param max the biggest possible number
     * @return the pseudo randomly generated number
     */
    public static int randNumFromRange(int min, int max)
    {
        return (int) (Math.random() * (max - min) + 1) + min;
    }

    /**
     * Offsets the given location by the entered values.
     *
     * @param loc the original location to be edited
     * @param x   the X offset
     * @param y   the Y offset
     * @param z   the Z offset
     * @return the new Location
     */
    public static Location getLocation(Location loc, int x, int y, int z)
    {
        return new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
    }
}
