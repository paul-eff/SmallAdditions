package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
 * Class for handling when a player digs.
 *
 * @author Paul Ferlitz
 */
public class BlockDugHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    // Class wide important values
    private final ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL)); // Allowed Tools
    private final ArrayList<Material> allowedMaterials = new ArrayList<>(Arrays.asList(Material.GRAVEL, Material.CLAY)); // Allowed Materials
    private int maxMinerSize = 0;

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
            //Check if a allowed block was chopped
            if (allowedMaterials.contains(event.getBlock().getType()))
            {
                Player eventPlayer = event.getPlayer();
                //Leveling leveling = new Leveling(eventPlayer);

                if (config.readModStatus(eventPlayer, "Veining"))
                {
                    if (event.getPlayer().isSneaking())
                    {
                        //All needed information to proceed
                        Block eventBlock = event.getBlock();
                        Material eventMaterial = event.getBlock().getType();
                        //Structural detection of ore vein
                        int cnt = 0;
                        maxMinerSize = Integer.parseInt(config.read("Config.Settings.maxMinerSize"));
                        ArrayList<Block> validMinerBlocks = new ArrayList<>();
                        ArrayList<Block> current_search = new ArrayList<>();
                        ArrayList<Block> to_search = new ArrayList<>();

                        current_search.add(eventBlock);
                        boolean sizeReached = false;

                        while (true)
                        {
                            for (Block currSearchBlock : current_search)
                            {
                                validMinerBlocks.add(currSearchBlock);
                                cnt++;

                                if (cnt >= maxMinerSize)
                                {
                                    sizeReached = true;
                                    break;
                                }
                                for (Block newBlock : findNeighbours(eventPlayer, eventMaterial, current_search, validMinerBlocks, currSearchBlock))
                                {
                                    if (!validMinerBlocks.contains(newBlock) && !to_search.contains(newBlock))
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
                        // Get player's current activated mods
                        //boolean fortune = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Fortune")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Fortune")));
                        int actualMinedBlocks = 0;
                        // Iterate over all possible blocks
                        for (Block block : validMinerBlocks)
                        {
                            /*ItemStack item;
                            // Check if block is of specific type and apply mods / enchantments
                            if (block.getType() == Material.GRAVEL)
                            {
                                item = new ItemStack(Material.FLINT);

                                if (fortune)
                                {
                                    item.setAmount(Helper.randNumFromRange(1, 4));
                                }
                                // Remove mined block
                                block.setType(Material.AIR);

                                if (Math.random() > 0.9)
                                {
                                    block.getWorld().dropItemNaturally(eventPlayer.getLocation(), item);
                                } else
                                {
                                    block.getWorld().dropItemNaturally(eventPlayer.getLocation(), new ItemStack(Material.GRAVEL));
                                }
                            } else if (block.getType() == Material.CLAY)
                            {
                                item = new ItemStack(Material.CLAY_BALL);
                                item.setAmount(Helper.randNumFromRange(1, 4));

                                if (fortune)
                                {
                                    item.setAmount(Helper.randNumFromRange(3, 4));
                                }
                                // Remove mined block
                                block.setType(Material.AIR);
                                block.getWorld().dropItemNaturally(eventPlayer.getLocation(), item);
                            }*/
                            block.breakNaturally(eventPlayer.getInventory().getItemInMainHand());
                            damageItem(eventPlayer, eventPlayer.getInventory().getItemInMainHand());
                            actualMinedBlocks++;
                            /*ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();
                            // Damage item according to durability enchantment
                            if (mainHand.getEnchantments().containsKey(Enchantment.DURABILITY))
                            {
                                int enchLevel;
                                enchLevel = mainHand.getEnchantments().get(Enchantment.DURABILITY);
                                double chance = (100 / (enchLevel + 1) * 1.0) / 100.0;

                                if (Math.random() > (1 - chance))
                                {
                                    damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                }
                            } else
                            {
                                damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                            }*/
                        }

                        //leveling.calcNextLevel(actualMinedBlocks);

                        msghelp.sendConsole(eventPlayer.getName() + " just dug up a " + eventMaterial + " vein (Size: " + validMinerBlocks.size()
                                + ") at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);

                        validMinerBlocks.clear();
                        current_search.clear();
                        to_search.clear();
                    } else
                    {
                        //leveling.calcNextLevel(1);
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
        if (item.getEnchantments().containsKey(Enchantment.DURABILITY))
        {
            int enchLevel = item.getEnchantments().get(Enchantment.DURABILITY);
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
     * Method to find all neighbours of a block
     *
     * @param block The block all neighbours are wanted for
     * @return An ArrayList of all neighbour blocks
     */
    private ArrayList<Block> findNeighbours(Player eventPlayer, Material eventMaterial, ArrayList<Block> current_search, ArrayList<Block> validMinerBlocks, Block block)
    {
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

        ArrayList<Block> validNeighbours = new ArrayList<>(allNeighbours);

        // Iterate through all found blocks to
        for (Block b : allNeighbours)
        {
            if (b.getType() == eventMaterial)
            {
                if (!current_search.contains(b) && !validMinerBlocks.contains(b))
                {
                    continue;
                }
            }
            validNeighbours.remove(b);
        }
        return validNeighbours;
    }
}
