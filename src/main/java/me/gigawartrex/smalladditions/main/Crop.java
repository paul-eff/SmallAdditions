package me.gigawartrex.smalladditions.main;

import org.bukkit.Material;

public enum Crop
{
    WHEAT(Material.WHEAT, Material.WHEAT_SEEDS),
    CARROT(Material.CARROTS, Material.CARROT),
    POTATO(Material.POTATOES, Material.POTATO),
    BEETROOT(Material.BEETROOTS, Material.BEETROOT_SEEDS),
    COCOA_BEANS(Material.COCOA, Material.COCOA_BEANS),
    NETHER_WART(Material.NETHER_WART, Material.NETHER_WART);

    private final Material seedToPlant;
    private final Material seedToDrop;

    Crop(Material seedToPlant, Material seedToDrop)
    {
        this.seedToPlant = seedToPlant;
        this.seedToDrop = seedToDrop;
    }

    public Material getSeed()
    {
        return this.seedToPlant;
    }

    public Material getSeedDrop()
    {
        return this.seedToDrop;
    }
}
