package me.gigawartrex.smalladditions.main;

import me.gigawartrex.smalladditions.io.Config;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Recipes
{
    private final Config config = new Config();

    public Recipes(Plugin plugin)
    {
        boolean isEnabled = false;
        // Adding all smelting related recipes
        for (NewSmeltable current : NewSmeltable.values())
        {
            String currentInputMaterial = current.getInput().toString().toLowerCase();
            if (currentInputMaterial.contains("raw"))
            {
                isEnabled = Boolean.parseBoolean(config.read("Config.Settings.Recipes.rawOreBlockSmelting"));
            } else if (currentInputMaterial.contains("amethyst"))
            {
                isEnabled = Boolean.parseBoolean(config.read("Config.Settings.Recipes.buddingAmethystCraftable"));
            }

            if (isEnabled)
            {
                NamespacedKey nsKey = new NamespacedKey(plugin, current.getInput().getKey().toString().replace("minecraft:", ""));
                FurnaceRecipe newRecipe = new FurnaceRecipe(nsKey, current.getOutput(), current.getInput(), current.getXp(), current.getTimeToSmelt());
                plugin.getServer().addRecipe(newRecipe);
            }
        }

        if (Boolean.parseBoolean(config.read("Config.Settings.Recipes.woolRecoloring")))
        {
            // Adding recipes for advanced wool recoloring
            String[] colorList = new String[]{"WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK"};
            List<Material> woolChoicesList = Arrays.asList(
                    (Material.WHITE_WOOL), (Material.ORANGE_WOOL), (Material.MAGENTA_WOOL), (Material.LIGHT_BLUE_WOOL),
                    (Material.YELLOW_WOOL), (Material.LIME_WOOL), (Material.PINK_WOOL), (Material.GRAY_WOOL),
                    (Material.LIGHT_GRAY_WOOL), (Material.CYAN_WOOL), (Material.PURPLE_WOOL), (Material.BLUE_WOOL),
                    (Material.BROWN_WOOL), (Material.GREEN_WOOL), (Material.RED_WOOL), (Material.BLACK_WOOL));
            RecipeChoice woolChoices = new RecipeChoice.MaterialChoice(woolChoicesList);
            for (String color : colorList)
            {
                NamespacedKey nsKey = new NamespacedKey(plugin, color.toLowerCase(Locale.ROOT) + "_wool");
                ShapelessRecipe newRecipe = new ShapelessRecipe(nsKey, new ItemStack(Material.getMaterial(color + "_WOOL")));
                newRecipe.addIngredient(1, Material.getMaterial(color + "_DYE"));
                newRecipe.addIngredient(woolChoices);
                plugin.getServer().addRecipe(newRecipe);
            }
        }

        if (Boolean.parseBoolean(config.read("Config.Settings.Recipes.lightBlockCraftable")))
        {
            // Dynamically create recipes for light blocks with different light levels dependent on how many versions are specified
            Material[] lightBlockMaterials = new Material[]{Material.TORCH, Material.GLOWSTONE, Material.REDSTONE_LAMP};
            for (int i = 0; i < lightBlockMaterials.length; i++)
            {
                // Determine light level
                int lightLevel = (Math.round(15 / lightBlockMaterials.length) * (i + 1));
                lightLevel = lightLevel > 15 ? 15 : lightLevel;
                lightLevel = lightLevel < 1 ? 1 : lightLevel;
                // Create new ItemStack with appropriate light level
                ItemStack is = new ItemStack(Material.LIGHT);
                BlockDataMeta blockDataMeta = (BlockDataMeta) is.getItemMeta();
                BlockData blockData = is.getType().createBlockData();
                ((Levelled) blockData).setLevel(lightLevel);
                blockDataMeta.setBlockData(blockData);
                is.setItemMeta(blockDataMeta);
                is.setAmount(8);
                // Create actual recipe
                NamespacedKey nsKey = new NamespacedKey(plugin, "light_block_lvl_" + lightLevel);
                ShapedRecipe newRecipe = new ShapedRecipe(nsKey, is);
                newRecipe.shape("ggg", "gmg", "ggg");
                newRecipe.setIngredient('g', Material.GLASS);
                newRecipe.setIngredient('m', lightBlockMaterials[i]);
                plugin.getServer().addRecipe(newRecipe);
            }
        }

        if (Boolean.parseBoolean(config.read("Config.Settings.Recipes.stonecutterStoneConversion")))
        {
            // Add recipes to the stonecutter to convert between stone types (andesite, cobble, diorite, ...)
            // Cobble to any of the other stone types is not possible, because it would make it to easy
            Material[] interconvertibleStoneMaterials = new Material[]{Material.COBBLESTONE, Material.DIORITE, Material.ANDESITE, Material.GRANITE, Material.TUFF, Material.CALCITE};
            for (Material current : interconvertibleStoneMaterials)
            {
                for (Material newTarget : interconvertibleStoneMaterials)
                {
                    if (current == newTarget || current == Material.COBBLESTONE)
                    {
                        continue;
                    }
                    NamespacedKey nsKey = new NamespacedKey(plugin, current.toString().toLowerCase() + "_to_" + newTarget.toString().toLowerCase());
                    StonecuttingRecipe newRecipe = new StonecuttingRecipe(nsKey, new ItemStack(newTarget), current);
                    plugin.getServer().addRecipe(newRecipe);
                }
            }
            // More custom recipes
            // Smooth stone to stone
            NamespacedKey nsKey = new NamespacedKey(plugin, "smoothstone_to_stone");
            plugin.getServer().addRecipe(new StonecuttingRecipe(nsKey, new ItemStack(Material.STONE), Material.SMOOTH_STONE));
        }

        if (Boolean.parseBoolean(config.read("Config.Settings.Recipes.stonecutterUnsmoothRecipe")))
        {
            // Add recipes to the stonecutter to convert polished stone types back to their raw form (polished andesite to andesite, etc.)
            Material[] interconvertibleStoneMaterials = new Material[]{Material.POLISHED_ANDESITE, Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_TUFF, Material.POLISHED_DEEPSLATE, Material.POLISHED_BLACKSTONE, Material.POLISHED_BASALT};
            for (Material current : interconvertibleStoneMaterials)
            {
                String noPolishName = current.toString().toLowerCase().replace("polished_", "");
                NamespacedKey nsKey = new NamespacedKey(plugin, "unpolish_" + noPolishName);
                ItemStack targetItemStack = new ItemStack(Material.getMaterial(noPolishName.toUpperCase()));
                StonecuttingRecipe newRecipe = new StonecuttingRecipe(nsKey, targetItemStack, current);
                plugin.getServer().addRecipe(newRecipe);
            }
        }

        /*NamespacedKey nsKey = new NamespacedKey(plugin, "test_recipe");
        ShapedRecipe newRecipe = new ShapedRecipe(nsKey, new ItemStack(Material.EXPERIENCE_BOTTLE));
        newRecipe.shape("   ", " ss", " ss");
        newRecipe.setIngredient('s', Material.SPONGE);
        plugin.getServer().addRecipe(newRecipe);*/
    }
}
