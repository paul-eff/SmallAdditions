package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.Constants;
import me.gigawartrex.smalladditions.files.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Leveling
{

    private Player player;
    private Config config;

    public Leveling(Player player)
    {
        this.player = player;
        this.config = new Config();
    }

    private int getDryLevel()
    {
        int holder;
        try
        {
            holder = Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Level"));
        } catch (NumberFormatException e)
        {
            holder = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Level", "0");
        }

        if (holder < 0)
        {
            holder = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Level", "0");
        }
        return holder;
    }

    private int getDryBlocks()
    {
        int holder;
        try
        {
            holder = Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Blocks"));
        } catch (NumberFormatException e)
        {
            holder = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
        }
        if (holder < 0)
        {
            holder = 0;
            config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "0");
        }
        return holder;
    }

    public int getLevel()
    {
        recalcValues();
        return Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Level"));
    }

    public int getBlocks()
    {
        recalcValues();
        return Integer.parseInt(config.read("Config.Players." + player.getUniqueId() + ".Leveling.Blocks"));
    }

    private void addBlocks(int amount)
    {
        int blocks = getBlocks();
        blocks += amount;
        config.write("Config.Players." + player.getUniqueId() + ".Leveling.Blocks", "" + blocks);
    }

    private void recalcValues()
    {
        for (int cnt = 1; cnt <= 2; cnt++)
        {
            int currLevel = getDryLevel();
            int currBlocks = getDryBlocks();
            int maxLevel = 0;

            for (int temp = 0; temp < Constants.modsList.size(); temp++)
            {
                if (maxLevel < Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + Constants.modsList.get(temp))))
                {
                    maxLevel = Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + Constants.modsList.get(temp)));
                }
            }

            //Adjust Level
            int blocksForLevel;
            if (currLevel == 0)
            {
                blocksForLevel = 0;
            } else
            {
                blocksForLevel = Integer.parseInt(config.read(config.getFileName() + ".Leveling." + currLevel));
            }

            if (currBlocks > blocksForLevel)
            {
                int i = 1;
                if ((currLevel + i) <= maxLevel)
                {
                    while (currBlocks >= Integer.parseInt(config.read("Config.Leveling." + (currLevel + i))))
                    {
                        i++;
                        if ((currLevel + i) > maxLevel)
                        {
                            break;
                        }
                    }
                    i--;
                    config.write(config.getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Level", "" + (currLevel + i));
                    currLevel = currLevel + i;
                }
            }

            //Adjust Blocks
            int blocksForCurrLevel;
            if (currLevel == 0)
            {
                blocksForCurrLevel = 0;
            } else
            {
                blocksForCurrLevel = Integer.parseInt(config.read(config.getFileName() + ".Leveling." + currLevel));
            }

            if (blocksForCurrLevel > currBlocks)
            {
                config.write(config.getFileName() + ".Players." + player.getUniqueId() + ".Leveling.Blocks", "" + blocksForCurrLevel);
                currBlocks = blocksForCurrLevel;
            }
        }
    }

    public void calcNextLevel(int amount)
    {

        MessageHelper mh = new MessageHelper();

        while (true)
        {
            int currLevel = getLevel();
            int currBlocks = getBlocks();
            if (!config.read("Config.Leveling." + (currLevel + 1)).equals(""))
            {
                int nextLevelBlocks = Integer.parseInt(config.read("Config.Leveling." + (currLevel + 1)));
                int blocksDiff = nextLevelBlocks - currBlocks;
                if (amount > blocksDiff)
                {
                    addBlocks(blocksDiff);
                    recalcValues();
                    amount -= blocksDiff;
                    mh.sendPlayer(player, "Level Up! You are now Level " + getLevel(), ChatColor.GOLD);
                    for (int temp = 0; temp < Constants.modsList.size(); temp++)
                    {
                        if (getLevel() == Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + Constants.modsList.get(temp))))
                        {
                            mh.sendPlayer(player, "You unlocked the " + Constants.modsList.get(temp) + " Mod!", ChatColor.GOLD);
                        }
                    }
                } else if (amount == blocksDiff)
                {
                    addBlocks(amount);
                    recalcValues();
                    mh.sendPlayer(player, "Level Up! You are now Level " + getLevel(), ChatColor.GOLD);
                    for (int temp = 0; temp < Constants.modsList.size(); temp++)
                    {
                        if (getLevel() == Integer.parseInt(config.read(config.getFileName() + ".Leveling.Modlevel." + Constants.modsList.get(temp))))
                        {
                            mh.sendPlayer(player, "You unlocked the " + Constants.modsList.get(temp) + " Mod!", ChatColor.GOLD);
                        }
                    }
                    break;
                } else
                {
                    addBlocks(amount);
                    break;
                }
            }else
            {
                addBlocks(amount);
                break;
            }
        }
    }
}
