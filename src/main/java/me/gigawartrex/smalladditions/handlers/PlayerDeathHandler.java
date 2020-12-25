package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathHandler implements Listener
{
    private final MessageHelper msghelp = new MessageHelper();

    @EventHandler
    public void onBlockBreak(PlayerDeathEvent event)
    {
        Player eventPlayer = event.getEntity();
        ItemStack[] playerInventoryContents = eventPlayer.getInventory().getContents().clone();
        Block origBlock = eventPlayer.getLocation().getBlock();
        if (origBlock.getY() < 10) origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), 10, origBlock.getZ());

        while (origBlock.getType() != Material.AIR || origBlock.getRelative(BlockFace.WEST).getType() != Material.AIR)
        {
            if (origBlock.getRelative(BlockFace.UP).getType() == Material.BEDROCK ||
                    Math.abs(eventPlayer.getLocation().getY() - origBlock.getLocation().getY()) >= 50 ||
                    origBlock.getY() > 250)
            {
                origBlock = eventPlayer.getLocation().getBlock();
                if (origBlock.getY() < 10) origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), 10, origBlock.getZ());
                if (origBlock.getY() > 250) origBlock = eventPlayer.getWorld().getBlockAt(origBlock.getX(), 250, origBlock.getZ());
                break;
            }
            origBlock = origBlock.getRelative(BlockFace.UP);
        }

        int newXP = event.getNewTotalExp();
        event.getDrops().clear();
        Block finalOrigBlock = origBlock;

        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            org.bukkit.block.data.type.Chest left = (org.bukkit.block.data.type.Chest) Material.CHEST.createBlockData();
            org.bukkit.block.data.type.Chest right = (org.bukkit.block.data.type.Chest) Material.CHEST.createBlockData();
            left.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
            right.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
            finalOrigBlock.setBlockData(left, false);
            finalOrigBlock.getRelative(BlockFace.WEST).setBlockData(right, false);

            Location loc = finalOrigBlock.getLocation();
            Block block = loc.getBlock();
            org.bukkit.block.Chest chest = (org.bukkit.block.Chest) block.getState();
            Inventory inv = chest.getInventory();

            for (ItemStack item : playerInventoryContents)
            {
                if (item != null)
                {
                    inv.addItem(item);
                }
            }

            Block sign = finalOrigBlock.getRelative(BlockFace.NORTH);
            sign.setType(Material.OAK_WALL_SIGN);
            Sign s = (Sign) sign.getState();
            String playerName = eventPlayer.getName();

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

            ExperienceOrb orb = block.getWorld().spawn(finalOrigBlock.getRelative(BlockFace.UP).getLocation(), ExperienceOrb.class);
            orb.setExperience(newXP);
        }, 20 * 2);

        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            msghelp.sendPlayer(eventPlayer, "A Deathbox was left behind were you died.", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Location: X: " + finalOrigBlock.getX() +
                    " Y: " + finalOrigBlock.getY() + " Z: " + finalOrigBlock.getZ() + ")", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Please remove the chest when found...", ChatColor.RED);
            msghelp.sendConsole(eventPlayer.getName() + " died at X: \"" + finalOrigBlock.getX() + "\" Y: \"" + finalOrigBlock.getY() + "\" Z: \"" + finalOrigBlock.getZ() + "\" (Deathbox created)", ChatColor.WHITE);
        }, 20 * 3);
    }
}
