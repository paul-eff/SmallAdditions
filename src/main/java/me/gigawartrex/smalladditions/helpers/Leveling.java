package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class for handling a player specific leveling e.g. how my block mined and which level reached.
 *
 * @author Paul Ferlitz
 */
@Deprecated
public class Leveling
{
    // Class variables
    private final Player player;
    private final Config config;

    /**
     * Class constructor.
     *
     * @param player the target player
     */
    public Leveling(Player player)
    {
        this.player = player;
        this.config = new Config();
    }

    /**
     * Method to get a player's current level. Will write 0 on null or error.
     *
     * @return the players current level.
     */
    private int getPlainLevel()
    {
        int currLevel;
        try
        {
            currLevel = Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Level"));
        } catch (NumberFormatException e)
        {
            currLevel = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Level", "0");
        }

        if (currLevel < 0)
        {
            currLevel = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Level", "0");
        }
        return currLevel;
    }

    /**
     * Method to get a player's current blocks mined. Will write 0 on null or error.
     *
     * @return the player's current blocks mined
     */
    private int getPlainBlocks()
    {
        int currBlocksMined;
        try
        {
            currBlocksMined = Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Blocks"));
        } catch (NumberFormatException e)
        {
            currBlocksMined = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
        }
        if (currBlocksMined < 0)
        {
            currBlocksMined = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
        }
        return currBlocksMined;
    }

    /**
     * Method that recalculates, writes and returns the current player's level.
     *
     * @return the play's current and final level
     */
    public int getLevel()
    {
        recalcValues();
        return Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Level"));
    }

    /**
     * Method that recalculates, writes and returns the current player's mined blocks count.
     *
     * @return the play's current and final mined blocks count
     */
    public int getBlocks()
    {
        recalcValues();
        return Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Blocks"));
    }

    /**
     * Method that adds x to the player's mined blocks count.
     *
     * @param amount the amount of blocks mined
     */
    private void addBlocks(int amount)
    {
        int blocks = getBlocks();
        blocks += amount;
        config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "" + blocks);
    }

    /**
     * Method that compares blocks mined count and level to adjust them accordingly (e.g. adjust blocks mined if admin sets player's level).
     */
    private void recalcValues()
    {
        /*
         * I know... this for-loop makes no sense. I don't know either why this is here.
         * But at this point I am too afraid to debug it.
         * Also, I don't want to if it works. Deal with it :)
         */
        for (int i = 1; i <= 2; i++)
        {
            int currLevel = getPlainLevel();
            int currBlocks = getPlainBlocks();
            int maxLevel = 0;

            // Get highest possible level of all mods
            for (int j = 0; j < Constants.modsList.size(); j++)
            {
                if (maxLevel < Integer.parseInt(config.read("Config.Leveling.Modlevel." + Constants.modsList.get(j))))
                {
                    maxLevel = Integer.parseInt(config.read("Config.Leveling.Modlevel." + Constants.modsList.get(j)));
                }
            }

            // Adjust player's level
            int blocksForLevel;
            if (currLevel == 0)
            {
                blocksForLevel = 0;
            } else
            {
                blocksForLevel = Integer.parseInt(config.read("Config.Leveling." + currLevel));
            }

            if (currBlocks > blocksForLevel)
            {
                int counter = 1;
                if ((currLevel + counter) <= maxLevel)
                {
                    while (currBlocks >= Integer.parseInt(config.read("Config.Leveling." + (currLevel + counter))))
                    {
                        counter++;
                        if ((currLevel + counter) > maxLevel)
                        {
                            break;
                        }
                    }
                    counter--;
                    config.write("Config.Players." + player.getUniqueId() + ".Leveling.Level", "" + (currLevel + counter));
                    currLevel = currLevel + counter;
                }
            }

            // Adjust player's mined blocks
            int blocksForCurrLevel;
            if (currLevel == 0)
            {
                blocksForCurrLevel = 0;
            } else
            {
                blocksForCurrLevel = Integer.parseInt(config.read("Config.Leveling." + currLevel));
            }

            if (blocksForCurrLevel > currBlocks)
            {
                config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "" + blocksForCurrLevel);
            }
        }
    }

    /**
     * Method that calculates if a new level was reached.
     *
     * @param amount the amount of blocks mined
     */
    public void calcNextLevel(int amount)
    {
        MessageHelper mh = new MessageHelper();

        // Loop until it breaks. Happens when amount was fully distributed
        while (true)
        {
            // Get current values
            int currLevel = getLevel();
            int currBlocks = getBlocks();
            if (!config.read("Config.Leveling." + (currLevel + 1)).equals(""))
            {
                // Calculate difference to next level
                int nextLevelBlocks = Integer.parseInt(config.read("Config.Leveling." + (currLevel + 1)));
                int blocksDiff = nextLevelBlocks - currBlocks;
                // Level Up handling - with rest
                if (amount > blocksDiff)
                {
                    addBlocks(blocksDiff);
                    recalcValues();
                    amount -= blocksDiff;
                    mh.sendPlayer(player, "Level Up! You are now Level " + getLevel(), ChatColor.GOLD);
                    for (int temp = 0; temp < Constants.modsList.size(); temp++)
                    {
                        if (getLevel() == Integer.parseInt(config.read("Config.Leveling.Modlevel." + Constants.modsList.get(temp))))
                        {
                            mh.sendPlayer(player, "You unlocked the " + Constants.modsList.get(temp) + " Mod!", ChatColor.GOLD);
                        }
                    }
                    // Level Up handling - no rest
                } else if (amount == blocksDiff)
                {
                    addBlocks(amount);
                    recalcValues();
                    mh.sendPlayer(player, "Level Up! You are now Level " + getLevel(), ChatColor.GOLD);
                    for (int temp = 0; temp < Constants.modsList.size(); temp++)
                    {
                        if (getLevel() == Integer.parseInt(config.read("Config.Leveling.Modlevel." + Constants.modsList.get(temp))))
                        {
                            mh.sendPlayer(player, "You unlocked the " + Constants.modsList.get(temp) + " Mod!", ChatColor.GOLD);
                        }
                    }
                    break;
                    // No level Up handling
                } else
                {
                    addBlocks(amount);
                    break;
                }
                // Just add blocks amount if max level was already reached
            } else
            {
                addBlocks(amount);
                break;
            }
        }
    }
}
