package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for handling when a player chops wood.
 *
 * @author Paul Ferlitz
 */
public class BlockChoppedHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    // Class wide important values
    private final ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)); // Allowed Tools
    private final ArrayList<Material> validLogs = new ArrayList<>(Arrays.asList(Material.CRIMSON_STEM, Material.WARPED_STEM)); // Allowed log types
    private final int timeToReplant = 2; //in seconds
    private int maxLumberjackSize = 0;

    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Check if player is using a valid tool
        if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
        {
            //Check if an allowed block was chopped
            if (event.getBlock().getType().toString().contains("_LOG") || validLogs.contains(event.getBlock().getType()))
            {
                Player eventPlayer = event.getPlayer();

                if (config.readModStatus(eventPlayer, "Veining"))
                {
                    if (event.getPlayer().isSneaking())
                    {
                        //All needed information to proceed
                        Block eventBlock = event.getBlock();
                        Material eventMaterial = event.getBlock().getType();

                        //Reset variable(s)
                        boolean firstBlockIsGrounded = false;
                        int cnt = 0;
                        boolean hasLeaves = false;
                        maxLumberjackSize = Integer.parseInt(config.read("Config.Settings.maxLumberjackSize"));

                        ArrayList<Block> validLumberjackBlocks = new ArrayList<>();
                        ArrayList<Block> current_search = new ArrayList<>();
                        ArrayList<Block> to_search = new ArrayList<>();

                        //Check if chopped block is grounded to later evaluate replant conditions
                        Block blockUnderOrig = Helper.getLocation(event.getBlock().getLocation(), 0, -1, 0).getBlock();

                        if (!blockUnderOrig.isLiquid() && !blockUnderOrig.isPassable() && !blockUnderOrig.isEmpty()) firstBlockIsGrounded = true;
                        //Structural detection of tree
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
                                hasLeaves = (boolean) tempFindNeighbours[0];
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
                        // Sapling position detection
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

                        // If a real tree was detected proceed with felling
                        if (hasLeaves)
                        {
                            // Iterate over all valid blocks
                            for (Block block : validLumberjackBlocks)
                            {
                                if (block.getY() >= eventBlock.getY())
                                {
                                    // Player still holding valid tool?
                                    if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                                    {
                                        block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                                        damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                    }
                                }
                            }
                            // Replant sapling if all conditions met
                            if (firstBlockIsGrounded && allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                            {
                                boolean replant = config.readModStatus(eventPlayer, "Replant") && Boolean.parseBoolean(config.read("Config.Settings.Mods.Replant"));

                                if (replant)
                                {
                                    plantSapling(eventBlock, eventMaterial, saplingNeighbours, (saplingNeighbours.size() > 1));
                                }
                            }
                            msghelp.sendConsole(eventPlayer.getName() + " just chopped down a tree at X:" + eventBlock.getX() +
                                    " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);
                        }
                        validLumberjackBlocks.clear();
                        current_search.clear();
                        to_search.clear();
                    }
                }
            }
        }
    }

    /**
     * Damage a given item for one damage point.
     *
     * @param player the {@code Player} whose item should be damaged
     * @param item   the item which should be damaged
     */
    @SuppressWarnings("deprecation")
    private void damageItem(Player player, ItemStack item)
    {
        if (item.getEnchantments().containsKey(Enchantment.UNBREAKING))
        {
            int enchLevel = item.getEnchantments().get(Enchantment.UNBREAKING);
            double chance = (100.0 / (enchLevel + 1) * 1.0) / 100.0;

            if (Math.random() > chance)
            {
                return;
            }
        }
        item.setDurability((short) (item.getDurability() + 1));
        if (item.getDurability() >= item.getType().getMaxDurability())
        {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
            player.getInventory().removeItem(item);
        } else
        {
            player.getInventory().setItemInMainHand(item);
        }
    }

    /**
     * Method to plant the appropriate sapling where tree was felled.
     *
     * @param origBlock the firstly hit block of the tree
     * @param bigTree   is it a "big tree" -> is the stem 2x2
     */
    private void plantSapling(Block origBlock, Material eventMaterial, ArrayList<Block> saplingNeighbours, boolean bigTree)
    {
        // TODO: Does not support nether saplings
        //Get Material needed from eventBlock for sapling material
        Material saplingMaterial = Material.getMaterial(eventMaterial.toString().substring(0, eventMaterial.toString().length() - 4) + "_SAPLING");
        if (saplingMaterial == null) return;
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            if (bigTree)
            {
                Location lastCornerBlockLocation = origBlock.getLocation();
                for (Block block : saplingNeighbours)
                {
                    block.setType(saplingMaterial);
                    lastCornerBlockLocation.setX(lastCornerBlockLocation.getX() + (block.getX() - origBlock.getX()));
                    lastCornerBlockLocation.setZ(lastCornerBlockLocation.getZ() + (block.getZ() - origBlock.getZ()));
                }
                lastCornerBlockLocation.getBlock().setType(saplingMaterial);
            }
            origBlock.setType(saplingMaterial);
        }, 20 * timeToReplant);
    }

    /**
     * Method to find all neighbours of a block.
     *
     * @param block the block all neighbours are wanted for
     * @return the {@code Object} array with {@code hasLeaves} flag and all valid neighbours in an {@code ArrayList<Block>}
     */
    private Object[] findNeighbours(Player eventPlayer, Material eventMaterial, ArrayList<Block> current_search, ArrayList<Block> validLumberjackBlocks, Block block)
    {
        boolean hasLeaves = false;
        ArrayList<Block> allNeighbours = new ArrayList<>();
        int[] blockCord = {block.getX(), block.getY(), block.getZ()};

        // Iterate through all neighbour blocks
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int k = -1; k <= 1; k++)
                {
                    allNeighbours.add(eventPlayer.getWorld().getBlockAt(new Location(eventPlayer.getWorld(), blockCord[0] + j, blockCord[1] + i, blockCord[2] + k)));
                }
            }
        }

        ArrayList<Block> validNeighbours = new ArrayList<>(allNeighbours);

        // Iterate through all found blocks to
        for (Block b : allNeighbours)
        {
            // Determine if it is a tree (has leaves)
            if (!hasLeaves && b.getType().toString().contains("_LEAVES") || b.getType().toString().contains("_WART_BLOCK"))
            {
                hasLeaves = true;
                // All block types are accepted for felling
            } else if ((b.getType().toString().contains("_LOG") || b.getType().toString().contains("_STEM")) && b.getType() == eventMaterial)
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
