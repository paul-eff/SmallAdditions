package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockChoppedHandler implements Listener
{
    private Config config;
    private MessageHelper msghelp;
    private Leveling leveling;

    private ArrayList<Block> validLumberjackBlocks;
    private ArrayList<Block> current_search;
    private ArrayList<Block> to_search;

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE)); // Allowed Tools

    private boolean hasLeaves = false;
    private boolean firstBlockIsGrounded = false;
    private int maxLumberjackSize = 0;
    private ArrayList<Block> saplingNeighbours;

    private int timeToReplant = 2; //in Seconds

    private Player eventPlayer;
    private Block eventBlock;
    private Material eventMaterial;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Check if player is using a valid tool
        if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
        {
            //Load needed classes
            config = new Config();
            msghelp = new MessageHelper();
            //Check if a allowed block was chopped
            //if (allowedBlockMats.contains(event.getBlock().getType())) {
            if (event.getBlock().getType().toString().contains("_LOG"))
            {
                eventPlayer = event.getPlayer();
                leveling = new Leveling(eventPlayer);

                boolean active = Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Mastering on?"));

                if (active)
                {
                    if (event.getPlayer().isSneaking())
                    {
                        //All needed information to proceed
                        eventBlock = event.getBlock();
                        eventMaterial = event.getBlock().getType();
                        //Reset variable(s)
                        firstBlockIsGrounded = false;
                        //Check if chopped block is grounded to later evaluate replant conditions
                        Location locUnderOrigBlock = Helper.getLocation(eventPlayer, event.getBlock().getLocation(), 0, -1, 0);
                        boolean isDirt = locUnderOrigBlock.getBlock().getType().equals(Material.DIRT);
                        boolean isGrass = locUnderOrigBlock.getBlock().getType().equals(Material.GRASS);

                        if (isDirt || isGrass)
                        {
                            firstBlockIsGrounded = true;
                        }
                        //Structural detection of tree
                        int cnt = 0;
                        hasLeaves = false;
                        maxLumberjackSize = Integer.parseInt(config.read("Config.Settings.maxLumberjackSize"));

                        validLumberjackBlocks = new ArrayList<>();
                        current_search = new ArrayList<>();
                        to_search = new ArrayList<>();

                        current_search.add(eventBlock);

                        boolean sizeReached = false;

                        while (true)
                        {
                            for (Block currSearchBlock : current_search)
                            {
                                validLumberjackBlocks.add(currSearchBlock);
                                cnt++;

                                if (cnt >= maxLumberjackSize)
                                {
                                    sizeReached = true;
                                    break;
                                }
                                for (Block newBlock : findNeighbours(currSearchBlock))
                                {
                                    if (!validLumberjackBlocks.contains(newBlock) && !to_search.contains(newBlock))
                                    {
                                        to_search.add(newBlock);
                                    }
                                }
                            }
                            if (to_search.isEmpty())
                            {
                                break;
                            } else
                            {
                                current_search.clear();
                                current_search.addAll(to_search);
                                to_search.clear();
                            }
                            if (sizeReached) break;
                        }

                        saplingNeighbours = new ArrayList<>(Arrays.asList(event.getBlock()));
                        saplingNeighbours.clear();

                        if (eventBlock.getRelative(BlockFace.NORTH).getType() == eventMaterial)
                        {
                            saplingNeighbours.add(event.getBlock().getRelative(BlockFace.NORTH));
                        }
                        if (eventBlock.getRelative(BlockFace.EAST).getType() == eventMaterial)
                        {
                            saplingNeighbours.add(event.getBlock().getRelative(BlockFace.EAST));
                        }
                        if (eventBlock.getRelative(BlockFace.SOUTH).getType() == eventMaterial)
                        {
                            saplingNeighbours.add(event.getBlock().getRelative(BlockFace.SOUTH));
                        }
                        if (eventBlock.getRelative(BlockFace.WEST).getType() == eventMaterial)
                        {
                            saplingNeighbours.add(event.getBlock().getRelative(BlockFace.WEST));
                        }
                        if (hasLeaves)
                        {
                            boolean autosmelt = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Autosmelt")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Autosmelt")));
                            boolean fortune = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Fortune")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Fortune")));
                            int actualChoppedBlocks = 0;

                            for (Block block : validLumberjackBlocks)
                            {
                                if (block.getY() >= eventBlock.getY())
                                {
                                    if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                                    {
                                        int randNum = 1;
                                        Material dropMaterial = block.getType();

                                        if (fortune)
                                        {
                                            randNum = Helper.randNumFromRange(1, 4);
                                        }
                                        if (autosmelt)
                                        {
                                            dropMaterial = Material.CHARCOAL;
                                        }

                                        block.setType(Material.AIR);
                                        actualChoppedBlocks++;
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), new ItemStack(dropMaterial, randNum));
                                        ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();

                                        if (mainHand.getEnchantments().containsKey(Enchantment.DURABILITY))
                                        {
                                            int enchLevel = 0;
                                            enchLevel = mainHand.getEnchantments().get(Enchantment.DURABILITY);
                                            double chance = (100 / (enchLevel + 1) * 1.0) / 100.0;

                                            if (Math.random() > (1 - chance))
                                            {
                                                damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                            }
                                        } else
                                        {
                                            damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                        }
                                    }
                                }
                            }
                            if (firstBlockIsGrounded && allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                            {
                                boolean replant = Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Replant"));
                                boolean replantConfig = Boolean.parseBoolean(config.read("Config.Settings.Mods.Replant"));

                                if (replant && replantConfig)
                                {
                                    plantSapling(eventBlock, (saplingNeighbours.size() > 1));
                                }
                            }

                            leveling.calcNextLevel(actualChoppedBlocks);
                            msghelp.sendConsole(eventPlayer.getName() + " just chopped down a tree at X:" + eventBlock.getX() +
                                    " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);
                        }
                        validLumberjackBlocks.clear();
                        current_search.clear();
                        to_search.clear();
                    } else
                    {
                        leveling.calcNextLevel(1);
                    }
                }
            }
        }
    }

    /**
     * Damage a given item for one damage point
     *
     * @param player The player which's item should be damaged
     * @param item   The item which should be damaged
     */
    @SuppressWarnings("deprecation")
    private void damageItem(Player player, ItemStack item)
    {
        item.setDurability((short) (item.getDurability() + 1));
        if (item.getDurability() >= item.getType().getMaxDurability())
        {
            player.getInventory().removeItem(item);
        } else
        {
            player.getInventory().setItemInMainHand(item);
        }
    }

    /**
     * Method to plant the apropriate sapling where tree was felled
     *
     * @param origBlock The firstly hit block of the tree
     * @param bigTree   Is it a "big tree" -> is the stem 2x2
     */
    private void plantSapling(Block origBlock, Boolean bigTree)
    {
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            //Get Material needed from eventBlock for sapling material
            String matExtract = eventMaterial.toString().substring(0, eventMaterial.toString().length() - 4);

            if (bigTree)
            {
                Location lastCornerBlockLocation = origBlock.getLocation();
                for (Block block : saplingNeighbours)
                {
                    block.setType(Material.getMaterial(matExtract + "_SAPLING"));
                    lastCornerBlockLocation.setX(lastCornerBlockLocation.getX() + (block.getX() - origBlock.getX()));
                    lastCornerBlockLocation.setZ(lastCornerBlockLocation.getZ() + (block.getZ() - origBlock.getZ()));
                }
                lastCornerBlockLocation.getBlock().setType(Material.getMaterial(matExtract + "_SAPLING"));
            }
            origBlock.setType(Material.getMaterial(matExtract + "_SAPLING"));
        }, 20 * timeToReplant);
    }

    /**
     * Method to find all neighbours of a block
     *
     * @param block The block all neighbours are wanted for
     * @return An ArrayList of all neighbour blocks
     */
    private ArrayList<Block> findNeighbours(Block block)
    {
        ArrayList<Block> validNeighbours = new ArrayList<>();
        ArrayList<Block> allNeighbours = new ArrayList<>();
        int[] blockCord = {block.getX(), block.getY(), block.getZ()};

        for (int i = -1; i <= 1; i++)
        { // Iterate through all neighbour blocks
            for (int j = -1; j <= 1; j++)
            {
                for (int k = -1; k <= 1; k++)
                {
                    allNeighbours.add(eventPlayer.getWorld().getBlockAt(new Location(eventPlayer.getWorld(), blockCord[0] + j, blockCord[1] + i, blockCord[2] + k)));
                }
            }
        }

        validNeighbours.addAll(allNeighbours);

        // Iterate through all found blocks to
        for (Block b : allNeighbours)
        {
            // Determine if it is a tree (has leaves)
            if (!hasLeaves && b.getType().toString().contains("_LEAVES"))
            {
                hasLeaves = true;
                // All block types are accepted for felling
            } else if (b.getType().toString().contains("_LOG") && b.getType() == eventMaterial)
            {
                if (!current_search.contains(b) && !validLumberjackBlocks.contains(b))
                {
                    continue;
                }
            }
            validNeighbours.remove(b);
        }
        return validNeighbours;
    }
}
