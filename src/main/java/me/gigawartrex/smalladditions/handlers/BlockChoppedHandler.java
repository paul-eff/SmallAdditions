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
    private Config config = new Config();
    private MessageHelper msghelp = new MessageHelper();

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)); // Allowed Tools
    private ArrayList<Material> validLogs = new ArrayList<>(Arrays.asList(Material.CRIMSON_STEM, Material.WARPED_STEM)); // Allowed log types
    private int maxLumberjackSize = 0;
    private int timeToReplant = 2; //in Seconds

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Check if player is using a valid tool
        if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
        {
            //Check if a allowed block was chopped
            //if (allowedBlockMats.contains(event.getBlock().getType())) {
            if (event.getBlock().getType().toString().contains("_LOG") || validLogs.contains(event.getBlock().getType()))
            {
                Player eventPlayer = event.getPlayer();
                Leveling leveling = new Leveling(eventPlayer);
                boolean active = Boolean.parseBoolean(config.read("Config.Players." + eventPlayer.getUniqueId() + ".Mastering on?"));

                if (active)
                {
                    if (event.getPlayer().isSneaking())
                    {
                        //All needed information to proceed
                        Block eventBlock = event.getBlock();
                        Material eventMaterial = event.getBlock().getType();
                        //Reset variable(s)
                        boolean firstBlockIsGrounded = false;
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
                        boolean hasLeaves = false;
                        maxLumberjackSize = Integer.parseInt(config.read("Config.Settings.maxLumberjackSize"));

                        ArrayList<Block> validLumberjackBlocks = new ArrayList<>();
                        ArrayList<Block> current_search = new ArrayList<>();
                        ArrayList<Block> to_search = new ArrayList<>();

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
                                Object[] tempFindNeighbours = findNeighbours(eventPlayer, eventMaterial, current_search, validLumberjackBlocks, currSearchBlock);
                                hasLeaves = (boolean)tempFindNeighbours[0];
                                for (Block newBlock : (ArrayList<Block>) tempFindNeighbours[1])
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

                        ArrayList<Block> saplingNeighbours = new ArrayList<>(Arrays.asList(event.getBlock()));
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
                            int actualChoppedBlocks = 0;

                            for (Block block : validLumberjackBlocks)
                            {
                                if (block.getY() >= eventBlock.getY())
                                {
                                    if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                                    {
                                        Material dropMaterial = block.getType();
                                        if (autosmelt)
                                        {
                                            dropMaterial = Material.CHARCOAL;
                                        }

                                        block.setType(Material.AIR);
                                        actualChoppedBlocks++;
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), new ItemStack(dropMaterial, 1));
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
                                    plantSapling(eventBlock, eventMaterial, saplingNeighbours, (saplingNeighbours.size() > 1));
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
    private void plantSapling(Block origBlock, Material eventMaterial, ArrayList<Block> saplingNeighbours, Boolean bigTree)
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
    private Object[] findNeighbours(Player eventPlayer, Material eventMaterial, ArrayList<Block> current_search, ArrayList<Block> validLumberjackBlocks, Block block)
    {
        boolean hasLeaves = false;
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

        return new Object[]{hasLeaves, validNeighbours};
    }
}
