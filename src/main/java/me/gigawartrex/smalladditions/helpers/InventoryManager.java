package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.ArmorType;
import me.gigawartrex.smalladditions.main.ToolType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public Integer[] getAllIndicesFuzzy(Material targetItemType)
    {
        String itemType = "";
        // Find out tool type (if it is a tool)
        for (ToolType tt : ToolType.values())
        {
            if (targetItemType.toString().toLowerCase(Locale.ROOT).contains(tt.getMaterialTypeSubstring()))
            {
                itemType = tt.getMaterialTypeSubstring();
                break;
            }
        }
        if (itemType.equals(""))
        {
            // Find out armor type (if it is an armor)
            for (ArmorType at : ArmorType.values())
            {
                if (targetItemType.toString().toLowerCase(Locale.ROOT).contains(at.getMaterialTypeSubstring()))
                {
                    itemType = at.getMaterialTypeSubstring();
                    break;
                }
            }
        }

        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i <= inventory.getSize(); i++)
        {
            ItemStack currItem = inventory.getItem(i);
            if (currItem != null && currItem.getType().toString().toLowerCase(Locale.ROOT).contains(itemType)) itemIndices.add(i);
        }
        return itemIndices.toArray(new Integer[0]);
    }

    public Integer[] getAlternatives(ItemStack targetItem)
    {
        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i <= inventory.getSize(); i++)
        {
            ItemStack currItem = inventory.getItem(i);
            if (currItem != null && !currItem.equals(targetItem) && currItem.getType() == targetItem.getType()) itemIndices.add(i);
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
