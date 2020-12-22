package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkLoaderHandler
{
    public ChunkLoaderHandler()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (World world : Constants.plugin.getServer().getWorlds())
                {
                    if (world.getName().equals("world"))
                    {
                        Block block = world.getBlockAt(35, 63, 107);
                        System.out.println(block.getType());
                        System.out.println("Block " + block.getType() + " at " + block.getLocation().toString() + " is in chunk X:" + block.getChunk().getX() + " Z:" + block.getChunk().getZ());
                        Chunk chunk = world.getChunkAt(block.getChunk().getX(), block.getChunk().getZ());
                        if (!chunk.isLoaded())
                        {
                            boolean load = chunk.load(true);
                            if (!load)
                            {
                                System.out.println("Failed to load Chunk: " + chunk.toString());
                            } else
                            {
                                System.out.println("Chunk loaded again");
                            }
                        } else
                        {
                            System.out.println("Chunk still loaded");
                            boolean unload = chunk.unload(true);
                            if (!unload)
                            {
                                System.out.println("Failed to unload Chunk: " + chunk.toString());
                            } else
                            {
                                System.out.println("Chunk unloaded");
                            }
                        }
                        break;
                    }
                }
            }
        }.runTaskTimer(Constants.plugin, 20L * 5, 20L * 5);
    }
}
