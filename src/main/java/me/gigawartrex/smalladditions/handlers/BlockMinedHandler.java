package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for handling when a player mines blocks.
 *
 * @author Paul Ferlitz
 */
public class BlockMinedHandler implements Listener
{
    // Class variables
    private final Config config = new Config();
    private final MessageHelper msghelp = new MessageHelper();

    // Class wide important values
    private final ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)); // Allowed tools
    private final ArrayList<Material> validOres = new ArrayList<>(Arrays.asList(Material.GLOWSTONE, Material.BONE_BLOCK, Material.ANCIENT_DEBRIS)); // Allowed blocks
    private final ArrayList<Material> noFortuneBlocks = new ArrayList<>(Arrays.asList(Material.BONE_BLOCK, Material.ANCIENT_DEBRIS)); // Blocks where fortune effects should not be applied
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
            //Check if an allowed block was mined
            if (event.getBlock().getType().toString().contains("_ORE") || validOres.contains(event.getBlock().getType()))
            {
                Player eventPlayer = event.getPlayer();

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

                        int totalXpToDrop = 0;
                        // Iterate over all possible blocks
                        for (Block block : validMinerBlocks)
                        {
                            ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();
                            // Check if block is applicable
                            if (allowedItems.contains(mainHand.getType()))
                            {
                                block.breakNaturally(eventPlayer.getInventory().getItemInMainHand());
                                damageItem(eventPlayer, eventPlayer.getInventory().getItemInMainHand());

                                int xpToDrop = 0;
                                // Drop XP according to block type
                                switch (block.getType().toString())
                                {
                                    case "COAL_ORE":
                                    case "DEEPSLATE_COAL_ORE":
                                        xpToDrop = Helper.randNumFromRange(0, 2);
                                        break;
                                    case "NETHER_GOLD_ORE":
                                        xpToDrop = Helper.randNumFromRange(0, 1);
                                        break;
                                    case "DIAMOND_ORE":
                                    case "EMERALD_ORE":
                                    case "DEEPSLATE_DIAMOND_ORE":
                                    case "DEEPSLATE_EMERALD_ORE":
                                        xpToDrop = Helper.randNumFromRange(3, 7);
                                        break;
                                    case "LAPIS_ORE":
                                    case "DEEPSLATE_LAPIS_ORE":
                                    case "NETHER_QUARTZ_ORE":
                                        xpToDrop = Helper.randNumFromRange(2, 5);
                                        break;
                                    case "REDSTONE_ORE":
                                    case "DEEPSLATE_REDSTONE_ORE":
                                        xpToDrop = Helper.randNumFromRange(1, 5);
                                        break;
                                    case "SPAWNER":
                                        xpToDrop = Helper.randNumFromRange(15, 43);
                                        break;
                                    case "IRON_ORE":
                                    case "GOLD_ORE":
                                    case "COPPER_ORE":
                                    case "DEEPSLATE_IRON_ORE":
                                    case "DEEPSLATE_GOLD_ORE":
                                    case "DEEPSLATE_COPPER_ORE":
                                        break;
                                }
                                if (xpToDrop > 0)
                                {
                                    totalXpToDrop += xpToDrop;
                                }
                            }
                        }
                        if (totalXpToDrop > 0)
                        {
                            eventPlayer.getWorld().spawn(eventPlayer.getLocation(), ExperienceOrb.class).setExperience(totalXpToDrop);
                        }

                        msghelp.sendConsole(eventPlayer.getName() + " just mined a " + eventMaterial + " vein (Size: " + validMinerBlocks.size()
                                + ") at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);

                        validMinerBlocks.clear();
                        current_search.clear();
                        to_search.clear();
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
    private ArrayList<Block> findNeighbours(Player eventPlayer, Material
            eventMaterial, ArrayList<Block> current_search, ArrayList<Block> validMinerBlocks, Block block)
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
