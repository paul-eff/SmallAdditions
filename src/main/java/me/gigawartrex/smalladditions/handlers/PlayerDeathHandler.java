package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


/**
 * Class for handling player deaths.
 *
 * @author Paul Ferlitz
 */
public class PlayerDeathHandler implements Listener
{
    private final MessageHelper msghelp = new MessageHelper();
    private final int minHeight = -64;
    private final int maxHeight = 319;

    /**
     * Main event handler.
     *
     * @param event the event triggered
     */
    @EventHandler
    public void onBlockBreak(PlayerDeathEvent event)
    {
        // Set fundamentals
        Player eventPlayer = event.getEntity();
        Block origBlock = eventPlayer.getWorld().getBlockAt(eventPlayer.getLocation());
        ItemStack[] playerInventoryContents = eventPlayer.getInventory().getContents().clone();
        // Make sure deathbox is definitely over minHeight
        if (origBlock.getY() <= minHeight)
        {
            int startHeight = minHeight + 1;
            if (origBlock.getWorld().getEnvironment() == World.Environment.THE_END) startHeight = 10;
            origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), startHeight, origBlock.getZ());
        }
        // Check for free space for chest
        while (neighbouringBlockType(origBlock, Material.BEDROCK))
        {
            // Clamp min and max y levels
            if (origBlock.getY() > maxHeight)
            {
                origBlock = eventPlayer.getLocation().getBlock();
                if (origBlock.getY() <= minHeight) origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), minHeight + 5, origBlock.getZ());
                if (origBlock.getY() >= maxHeight) origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), maxHeight - 5, origBlock.getZ());
                break;
            }
            origBlock = origBlock.getRelative(BlockFace.UP);
        }
        // Calculate XP
        int temp = 7 * eventPlayer.getLevel();
        int newXP = temp <= 100 ? temp : 100;
        event.getDrops().clear();
        Block finalOrigBlock = origBlock;
        // Spawn chest with items
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            // Create double chest entity
            Block leftSide = finalOrigBlock;
            Block rightSide = finalOrigBlock.getRelative(BlockFace.WEST);
            leftSide.setType(Material.CHEST);
            rightSide.setType(Material.CHEST);

            Chest leftChestData = (Chest)leftSide.getBlockData();
            Chest rightChestData = (Chest)rightSide.getBlockData();
            leftChestData.setFacing(BlockFace.NORTH);
            leftChestData.setType(Chest.Type.RIGHT);
            rightChestData.setFacing(BlockFace.NORTH);
            rightChestData.setType(Chest.Type.LEFT);

            leftSide.setBlockData(leftChestData);
            rightSide.setBlockData(rightChestData);

            // Fill chest
            Block block = finalOrigBlock.getLocation().getBlock();
            org.bukkit.block.Chest chest = (org.bukkit.block.Chest) block.getState();
            Inventory inv = chest.getInventory();
            for (ItemStack item : playerInventoryContents)
            {
                if (item != null)
                {
                    inv.addItem(item);
                }
            }
            // Create sign
            Block sign = finalOrigBlock.getRelative(BlockFace.NORTH);
            sign.setType(Material.OAK_WALL_SIGN);
            Sign s = (Sign) sign.getState();
            String playerName = eventPlayer.getName();
            // Write one sign and linewrap player name
            if (playerName.length() > 15 - 2)
            {
                s.setLine(0, playerName.substring(0, 15));
                if (playerName.substring(15).length() > 15 - 2)
                {
                    s.setLine(1, playerName.substring(15, 30 - 2) + "..'s");
                } else
                {
                    s.setLine(1, playerName.substring(15) + "'s");
                }
                s.setLine(2, "Deathbox");
            } else
            {
                s.setLine(0, playerName + "'s");
                s.setLine(1, "Deathbox");
            }
            s.update();
            // Spawn XP
            ExperienceOrb orb = block.getWorld().spawn(finalOrigBlock.getRelative(BlockFace.UP).getLocation(), ExperienceOrb.class);
            orb.setExperience(newXP);
        }, 20 * 2);

        // Message player about deathbox location
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            msghelp.sendPlayer(eventPlayer, "A Deathbox was left behind were you died.", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Location: X: " + finalOrigBlock.getX() +
                    " Y: " + finalOrigBlock.getY() + " Z: " + finalOrigBlock.getZ() + ")", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Please remove the chest when found...", ChatColor.RED);
            msghelp.sendConsole(eventPlayer.getName() + " died at X: \"" + finalOrigBlock.getX() + "\" Y: \"" + finalOrigBlock.getY() + "\" Z: \"" + finalOrigBlock.getZ() + "\" (Deathbox created)", ChatColor.WHITE);
        }, 20 * 3);
    }

    private boolean neighbouringBlockType(Block sourceBlock, Material possibleNeighbour)
    {
        for (BlockFace face : BlockFace.values())
        {
            if (face.isCartesian() && face != BlockFace.DOWN)
            {
                if (sourceBlock.getRelative(face).getType() == possibleNeighbour) return true;
            }
        }
        return false;
    }
}
