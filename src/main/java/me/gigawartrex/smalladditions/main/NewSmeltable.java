package me.gigawartrex.smalladditions.main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum NewSmeltable
{
    IRON_BLOCK(Material.RAW_IRON_BLOCK, new ItemStack(Material.IRON_BLOCK), 0.7 * 9, 10 * 8),
    COPPER_BLOCK(Material.RAW_COPPER_BLOCK, new ItemStack(Material.COPPER_BLOCK), 0.7 * 9, 10 * 8),
    GOLD_BLOCK(Material.RAW_GOLD_BLOCK, new ItemStack(Material.GOLD_BLOCK), 1 * 9, 10 * 8);


    private final Material input;
    private final ItemStack output;
    private final float xp;
    private final int timeToSmelt;

    NewSmeltable(Material input, ItemStack output, double xp, int timeToSmelt)
    {
        this.input = input;
        this.output = output;
        this.xp = (float) xp;
        this.timeToSmelt = timeToSmelt * 20;
    }

    public Material getInput()
    {
        return input;
    }

    public ItemStack getOutput()
    {
        return output;
    }

    public float getXp()
    {
        return xp;
    }

    public int getTimeToSmelt()
    {
        return timeToSmelt;
    }
}
