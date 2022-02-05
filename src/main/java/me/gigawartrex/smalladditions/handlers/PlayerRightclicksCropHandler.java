package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.io.Config;
import me.gigawartrex.smalladditions.main.Constants;
import me.gigawartrex.smalladditions.main.Crop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class PlayerRightclicksCropHandler implements Listener
{
    // Class variables
    private final Config config = new Config();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event)
    {
        if (event.getHand() == EquipmentSlot.HAND)
        {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (event.getItem() != null && event.getItem().getType().toString().contains("_HOE"))
                {
                    Block clickedBlock = event.getClickedBlock();
                    for (Crop crop : Crop.values())
                    {
                        if (clickedBlock.getType() == crop.getSeed())
                        {
                            Ageable ageable = (Ageable) clickedBlock.getBlockData();
                            if (ageable.getAge() == ageable.getMaximumAge())
                            {
                                Collection<ItemStack> drops = clickedBlock.getDrops();
                                boolean replantOn = config.readModStatus(event.getPlayer(), "Replant");
                                for (ItemStack drop : drops)
                                {
                                    if (drop.getType() == crop.getSeedDrop())
                                    {
                                        if (replantOn)
                                        {
                                            drop.setAmount(drop.getAmount() - 1);
                                        }
                                        if (drop.getAmount() <= 0) continue;
                                    }
                                    clickedBlock.getWorld().dropItemNaturally(clickedBlock.getLocation(), drop);
                                }
                                clickedBlock.setType(Material.AIR);
                                if (replantOn)
                                {
                                    Bukkit.getScheduler().runTaskLater(Constants.plugin, () ->
                                    {
                                        clickedBlock.setType(crop.getSeed());
                                        ((Ageable) clickedBlock.getBlockData()).setAge(0);
                                    }, 2);
                                }
                                damageItem(event.getPlayer(), event.getItem());
                            }
                        }
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
    // TODO: Use Damageable to get rid of deprecated setDurability...
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
}