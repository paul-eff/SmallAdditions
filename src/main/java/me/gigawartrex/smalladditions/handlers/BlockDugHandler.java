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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockDugHandler implements Listener
{
    private Config config;
    private Leveling leveling;
    private MessageHelper msghelp;
    private ArrayList<Block> validMinerBlocks;
    private ArrayList<Block> current_search;
    private ArrayList<Block> to_search;
    private ArrayList<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL)); // Allowed Tools
    private ArrayList<Material> allowedMaterials = new ArrayList<>(Arrays.asList(Material.GRAVEL, Material.CLAY)); // Allowed Materials
    private int maxMinerSize = 0;
    private Player eventPlayer;
    private Block eventBlock;
    private Material eventMaterial;

    /*
     * Method to handle tree felling onBlockBreak event
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
                config = new Config();
                msghelp = new MessageHelper();
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

                        //Structural detection of ore vein
                        int cnt = 0;
                        maxMinerSize = Integer.parseInt(config.read("Config.Settings.maxMinerSize"));
                        validMinerBlocks = new ArrayList<>();
                        current_search = new ArrayList<>();
                        to_search = new ArrayList<>();
                        current_search.add(eventBlock);

                        while (cnt < maxMinerSize)
                        {

                            for (Block currSearchBlock : current_search)
                            {

                                validMinerBlocks.add(currSearchBlock);

                                for (Block newBlock : findNeighbours(currSearchBlock))
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
                            cnt++;
                        }

                        boolean fortune = Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Mods.Fortune"));
                        int actualMinedBlocks = 0;

                        for (Block block : validMinerBlocks)
                        {
                            if(block.getType() == Material.GRAVEL)
                            {
                                ItemStack flintDrop = new ItemStack(Material.FLINT);
                                if (fortune)
                                {
                                    flintDrop.setAmount(Helper.randNumFromRange(1, 4));
                                }

                                block.setType(Material.AIR);
                                if (Math.random() >= 0.9)
                                {
                                    block.getWorld().dropItemNaturally(eventPlayer.getLocation(), flintDrop);
                                } else
                                {
                                    block.getWorld().dropItemNaturally(eventPlayer.getLocation(), new ItemStack(Material.GRAVEL));
                                    if (fortune && Math.random() >= 0.9)
                                    {
                                        block.getWorld().dropItemNaturally(eventPlayer.getLocation(), flintDrop);
                                    }
                                }
                            }else if(block.getType() == Material.CLAY)
                            {
                                ItemStack clayDrop = new ItemStack(Material.CLAY_BALL);
                                clayDrop.setAmount(Helper.randNumFromRange(1, 4));

                                if (fortune)
                                {
                                    clayDrop.setAmount(Helper.randNumFromRange(3, 6));
                                }
                                block.setType(Material.AIR);
                                block.getWorld().dropItemNaturally(eventPlayer.getLocation(), clayDrop);
                            }
                            actualMinedBlocks++;

                            ItemStack mainHand = eventPlayer.getInventory().getItemInMainHand();
                            if(mainHand.getEnchantments().containsKey(Enchantment.DURABILITY))
                            {
                                int enchLevel = 0;
                                enchLevel = mainHand.getEnchantments().get(Enchantment.DURABILITY);
                                double chance = (100 / (enchLevel+1)*1.0) / 100.0;

                                if(Math.random() > (1-chance))
                                {
                                    damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                                }
                            }else{
                                damageItem(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                            }
                        }

                        leveling.calcNextLevel(actualMinedBlocks);

                        msghelp.sendConsole(eventPlayer.getName() + " just dug up a " + eventMaterial + " vein (Size: " + validMinerBlocks.size()
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