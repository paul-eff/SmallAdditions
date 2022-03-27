package me.gigawartrex.smalladditions.helpers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager
{
    private Player player;
    private Inventory inventory;

    public InventoryManager(Player player)
    {
        this.player = player;
        this.inventory = player.getInventory();
    }

    public Player getPlayer()
    {
        return player;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public int getIndex(ItemStack targetItem)
    {
        for (int i = 0; i <= inventory.getSize(); i++)
        {
            ItemStack currItem = inventory.getItem(i);
            if (currItem != null && currItem.equals(targetItem)) return i;
        }
        return -1;
    }

    public Integer[] getAllIndices(Material targetItemType)
    {
        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i <= inventory.getSize(); i++)
        {
            ItemStack currItem = inventory.getItem(i);
            if (currItem != null && currItem.getType() == targetItemType) itemIndices.add(i);
        }
        return itemIndices.toArray(new Integer[0]);
    }

    public Integer[] getAlternatives(ItemStack targetItem)
    {
        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i <= inventory.getSize(); i++)
        {
            ItemStack currItem = inventory.getItem(i);
            if (currItem != null && currItem.equals(targetItem) && currItem.getType() == targetItem.getType()) itemIndices.add(i);
        }
        return itemIndices.toArray(new Integer[0]);
    }

    public void swapItems(int a, int b)
    {
        ItemStack swap = inventory.getItem(a);
        inventory.setItem(a, inventory.getItem(b));
        inventory.setItem(b, swap);
    }
}
