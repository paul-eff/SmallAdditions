package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
import me.gigawartrex.smalladditions.helpers.Helper;
import me.gigawartrex.smalladditions.helpers.Leveling;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockMinedHandler implements Listener
{
    private Config config = new Config();
    private MessageHelper msghelp = new MessageHelper();

    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE)); // Allowed Tools
    private int maxMinerSize = 0;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Check if player is using a valid tool
        if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
        {
            //Check if a allowed block was chopped
            //if (allowedBlockMats.contains(event.getBlock().getType())) {
            if (event.getBlock().getType().toString().contains("_ORE") || event.getBlock().getType() == Material.GLOWSTONE)
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

                        boolean autosmelt = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Autosmelt")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Autosmelt")));
                        boolean fortune = (Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Fortune")) && Boolean.parseBoolean(config.read("Config.Settings.Mods.Fortune")));
                        int actualMinedBlocks = 0;

                        for (Block block : validMinerBlocks)
                        {
                            if (allowedItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
                            {
                                for (ItemStack item : block.getDrops(new ItemStack(event.getPlayer().getInventory().getItemInMainHand().getType())))
                                {
                                    ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();

                                    if (mainHand.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS))
                                    {
                                        int enchLevel = 0;
                                        enchLevel = mainHand.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS);
                                        double randChance = Math.random();

                                        switch (enchLevel)
                                        {
                                            case 1:
                                                if(randChance > 0.66) item.setAmount(item.getAmount() * 2);
                                                break;
                                            case 2:
                                                if(randChance > 0.5 && randChance <= 0.75) item.setAmount(item.getAmount() * 2);
                                                else if(randChance > 0.75) item.setAmount(item.getAmount() * 3);
                                                break;
                                            case 3:
                                                if(randChance > 0.4 && randChance <= 0.6) item.setAmount(item.getAmount() * 2);
                                                if(randChance > 0.6 && randChance <= 0.8) item.setAmount(item.getAmount() * 3);
                                                else if(randChance > 0.8) item.setAmount(item.getAmount() * 4);
                                                break;
                                            default:
                                                System.out.println("[SmallAdditions] BlockMinedHandler.java [130:45] - Undefined Luck enchantment Level ("+enchLevel+")");
                                        }
                                    } else if (fortune && ((block.getType() != item.getType()) || autosmelt))
                                    {
                                        item.setAmount(Helper.randNumFromRange(1, 3));
                                    }
                                    if (autosmelt)
                                    {
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), evaluateDrop(item));
                                    } else
                                    {
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), item);
                                    }
                                    int xpToDrop = 0;
                                    switch (block.getType().toString())
                                    {
                                        case "COAL_ORE":
                                            xpToDrop = Helper.randNumFromRange(0, 2);
                                            break;
                                        case "DIAMOND_ORE":
                                        case "EMERALD_ORE":
                                            xpToDrop = Helper.randNumFromRange(3, 7);
                                            break;
                                        case "LAPIS_ORE":
                                        case "NETHER_QUARTZ_ORE":
                                            xpToDrop = Helper.randNumFromRange(2, 5);
                                            break;
                                        case "REDSTONE_ORE":
                                            xpToDrop = Helper.randNumFromRange(1, 5);
                                            break;
                                        case "IRON_ORE":
                                        case "GOLD_ORE":
                                            if (autosmelt)
                                            {
                                                xpToDrop = 1;
                                            }
                                            break;
                                        case "SPAWNER":
                                            xpToDrop = Helper.randNumFromRange(15, 43);
                                            break;
                                        /* TODO: When plugin goes 1.16.X update to accept nether gold ore
                                        case "NETHER_GOLD_ORE":
                                            xpToDrop = Helper.randNumFromRange(0, 1);
                                            break;*/
                                    }
                                    if (xpToDrop > 0)
                                    {
                                        block.getWorld().spawn(eventPlayer.getLocation(), ExperienceOrb.class).setExperience(xpToDrop);
                                    }
                                }

                                block.setType(Material.AIR);
                                actualMinedBlocks++;

                                ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();

                                if (mainHand.getEnchantments().containsKey(Enchantment.DURABILITY))
                                {
                                    int enchLevel = 0;
                                    enchLevel = mainHand.getEnchantments().get(Enchantment.DURABILITY);
                                    double chance = (100.0 / (enchLevel + 1) * 1.0) / 100.0;

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

                        leveling.calcNextLevel(actualMinedBlocks);

                        msghelp.sendConsole(eventPlayer.getName() + " just mined a " + eventMaterial + " vein (Size: " + validMinerBlocks.size()
                                + ") at X:" + eventBlock.getX() + " Y:" + eventBlock.getY() + " Z:" + eventBlock.getZ(), ChatColor.WHITE);

                        validMinerBlocks.clear();
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
     * This method determines the item to be dropped from a mined block
     *
     * @param material The Material which's item drop is to be found
     * @return returns the item drop
     */
    private ItemStack evaluateDrop(ItemStack material)
    {
        if (material.getType() == Material.IRON_ORE)
        {
            material.setType(Material.IRON_INGOT);
        } else if (material.getType() == Material.GOLD_ORE)
        {
            material.setType(Material.GOLD_INGOT);
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
     * Method to find all neighbours of a block
     *
     * @param block The block all neighbours are wanted for
     * @return An ArrayList of all neighbour blocks
     */
    private ArrayList<Block> findNeighbours(Player eventPlayer, Material eventMaterial, ArrayList<Block> current_search, ArrayList<Block> validMinerBlocks, Block block)
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
