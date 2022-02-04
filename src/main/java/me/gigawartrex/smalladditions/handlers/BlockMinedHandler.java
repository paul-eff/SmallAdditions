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
                ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();

                /*if (mainHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH))
                {
                    msghelp.sendPlayer(eventPlayer, "You're using a pickaxe with Silk Touch, this is not supported at the moment. Mining normally!", ChatColor.RED);
                    event.getBlock().breakNaturally();
                }*/
                //Leveling leveling = new Leveling(eventPlayer);

                boolean active = config.readModStatus(eventPlayer, "Veining");

                if (active)
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
                        //boolean autosmelt = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Autosmelt")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Autosmelt")));
                        //boolean fortune = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Fortune")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Fortune")));
                        int actualMinedBlocks = 0;
                        int totalXpToDrop = 0;
                        // Iterate over all possible blocks
                        for (Block block : validMinerBlocks)
                        {
                            mainHand = eventPlayer.getInventory().getItemInMainHand();
                            // Check if block is applicable
                            if (allowedItems.contains(mainHand.getType()))
                            {
                                // Get drops
                                for (ItemStack item : block.getDrops(mainHand))
                                {
                                    // Check if block has no fortune multiplier
                                    /*if (!noFortuneBlocks.contains(block.getType()) && ((block.getType() != item.getType()) || autosmelt))
                                    {
                                        // Check if item in hand has fortune
                                        if (mainHand.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS))
                                        {
                                            // Determine fortune level and drop accordingly
                                            int enchLevel;
                                            enchLevel = mainHand.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS);
                                            double randChance = Math.random();

                                            switch (enchLevel)
                                            {
                                                case 1:
                                                    if (randChance > 0.66) item.setAmount(item.getAmount() * 2);
                                                    break;
                                                case 2:
                                                    if (randChance > 0.5 && randChance <= 0.75) item.setAmount(item.getAmount() * 2);
                                                    else if (randChance > 0.75) item.setAmount(item.getAmount() * 3);
                                                    break;
                                                case 3:
                                                    if (randChance > 0.4 && randChance <= 0.6) item.setAmount(item.getAmount() * 2);
                                                    if (randChance > 0.6 && randChance <= 0.8) item.setAmount(item.getAmount() * 3);
                                                    else if (randChance > 0.8) item.setAmount(item.getAmount() * 4);
                                                    break;
                                                default:
                                                    msghelp.sendConsole("BlockMinedHandler.java [130:45] - Undefined Luck enchantment Level (" + enchLevel + ")", ChatColor.RED);
                                            }
                                            // No enchantment, just fortune mod
                                        } else if (fortune && ((block.getType() != item.getType()) || autosmelt))
                                        {
                                            item.setAmount(Helper.randNumFromRange(1, 3));
                                        }
                                    }*/
                                    // Drops smelted or normal form
                                    /*if (autosmelt)
                                    {
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), evaluateDrop(item));
                                    } else
                                    {
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), item);
                                    }*/
                                }
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
                                // Remove mined block
                                //block.setType(Material.AIR);
                                block.breakNaturally(eventPlayer.getInventory().getItemInMainHand());
                                damageItem(eventPlayer, eventPlayer.getInventory().getItemInMainHand());
                                actualMinedBlocks++;

                                /*mainHand = eventPlayer.getInventory().getItemInMainHand();
                                // Damage item according to durability enchantment
                                if (mainHand.getEnchantments().containsKey(Enchantment.DURABILITY))
                                {
                                    int enchLevel;
                                    enchLevel = mainHand.getEnchantments().get(Enchantment.DURABILITY);
                                    double chance = (100.0 / (enchLevel + 1) * 1.0) / 100.0;

                                    if (Math.random() > (1 - chance))
                                    {
                                        damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                    }
                                } else
                                {
                                    damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                }*/
                            }
                        }
                        if (totalXpToDrop > 0)
                        {
                            eventPlayer.getWorld().spawn(eventPlayer.getLocation(), ExperienceOrb.class).setExperience(totalXpToDrop);
                        }

                        //leveling.calcNextLevel(actualMinedBlocks);

                        msghelp.sendConsole(eventPlayer.getName() + " just mined a " + eventMaterial + " vein (Size: " + actualMinedBlocks
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
     * This method determines the item to be dropped from a mined block
     *
     * @param material The Material which's item drop is to be found
     * @return returns the item drop
     */
    @Deprecated
    private ItemStack evaluateDrop(ItemStack material)
    {
        if (material.getType().toString().contains("RAW_IRON"))
        {
            material.setType(Material.IRON_INGOT);
        } else if (material.getType().toString().contains("RAW_GOLD"))
        {
            material.setType(Material.GOLD_INGOT);
        } else if (material.getType().toString().contains("RAW_COPPER"))
        {
            material.setType(Material.COPPER_INGOT);
        }
        return material;
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
