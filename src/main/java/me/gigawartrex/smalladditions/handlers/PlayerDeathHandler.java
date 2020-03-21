package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.files.Config;
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
    private Config config;
    private MessageHelper msghelp;
    private Player eventPlayer;

    @EventHandler
    public void onBlockBreak(PlayerDeathEvent event)
    {
        config = new Config();
        msghelp = new MessageHelper();
        eventPlayer = event.getEntity();
        Block origBlock = eventPlayer.getLocation().getBlock();

        org.bukkit.block.data.type.Chest left = (org.bukkit.block.data.type.Chest)Material.CHEST.createBlockData();
        org.bukkit.block.data.type.Chest right = (org.bukkit.block.data.type.Chest)Material.CHEST.createBlockData();
        left.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
        right.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
        origBlock.setBlockData(left, false);
        origBlock.getRelative(BlockFace.WEST).setBlockData(right, false);

        Location loc = origBlock.getLocation();
        Block block = loc.getBlock();
        org.bukkit.block.Chest chest = (org.bukkit.block.Chest)block.getState();
        Inventory inv = chest.getInventory();

        for(ItemStack item : eventPlayer.getInventory().getContents())
        {
            if(item != null)
            {
                inv.addItem(item);
            }
        }

        Block sign = origBlock.getRelative(BlockFace.NORTH);
        sign.setType(Material.OAK_WALL_SIGN);
        Sign s = (Sign) sign.getState();

        String playerName = eventPlayer.getName();
        if(playerName.length() > 15-2) {
            s.setLine(0, playerName.substring(0,15));
            if(playerName.substring(15).length() > 15-2)
            {
                s.setLine(1, playerName.substring(15, 30-2)+"..'s");
            }else{
                s.setLine(1, playerName.substring(15)+"'s");
            }
            s.setLine(2, "Deathbox");
        }else{
            s.setLine(0, playerName+"'s");
            s.setLine(1, "Deathbox");
        }
        s.update();

        //ExperienceOrb orb = block.getWorld().spawn(origBlock.getRelative(BlockFace.UP).getLocation(), ExperienceOrb.class);
        //orb.setExperience(eventPlayer.getTotalExperience());

        event.getDrops().clear();

        //System.out.println(eventPlayer.getExp());
        //System.out.println(eventPlayer.getTotalExperience());
        //System.out.println(eventPlayer.getExpToLevel());
        //System.out.println(event.getNewExp());
        //System.out.println(event.getNewTotalExp());
        //System.out.println(event.getDroppedExp());
        //event.setDroppedExp(eventPlayer.getTotalExperience());

        Location deathLoc = eventPlayer.getLocation();
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
        {
            msghelp.sendPlayer(eventPlayer, "A Deathbox was left behind were you died.", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Location: X: "+deathLoc.getBlockX()+
                    " Y: "+deathLoc.getBlockY()+" Z: "+deathLoc.getBlockZ()+")", ChatColor.RED);
            msghelp.sendPlayer(eventPlayer, "Please remove the chest when found...", ChatColor.RED);
        }, 20 * 3);
    }
}
