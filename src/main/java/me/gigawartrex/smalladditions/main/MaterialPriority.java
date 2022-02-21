package me.gigawartrex.smalladditions.main;

import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public enum MaterialPriority
{
    LEATHER("leather", 0),
    GOLD("gold", 1),
    WOOD("wood", 2),
    COBBLESTONE("stone", 3),
    CHAINMAIL("chainmail", 4),
    IRON("iron", 5),
    TURTLE("turtle", 6),
    DIAMOND("diamond", 7),
    NETHERITE("netherite", 8);

    private final String type;
    private final int priority;

    MaterialPriority(String type, int priority)
    {
        this.type = type;
        this.priority = priority;
    }

    public static int getPriority(ItemStack item)
    {
        String itemMat = item.getType().toString().toLowerCase(Locale.ROOT);
        for (MaterialPriority matPrio : values())
        {
            if(itemMat.contains(matPrio.type))
            {
                return matPrio.priority;
            }
        }
        return -1;
    }
}
