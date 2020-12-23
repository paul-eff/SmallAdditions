package me.gigawartrex.smalladditions.helpers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Helper {

    /**
     * Generates a random number inclusive between min and max
     *
     * @param min Minimum Number generated
     * @param max Maximum Number generated
     * @return Returns the random number
     */
    public static int randNumFromRange(int min, int max) {
        return (int) (Math.random() * (max - min) + 1) + min;
    }

    /**
     * Offsets the given location by the entered values
     *
     * @param loc Location to be edited
     * @param x X offset
     * @param y Y offset
     * @param z Z offset
     *
     * @return Returns the new Location in the overworld
     */
    public static Location getLocation(Player player, Location loc, int x, int y, int z) {
        return new Location(player.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
    }
}
