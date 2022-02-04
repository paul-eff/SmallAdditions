package me.gigawartrex.smalladditions.main;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Recipes
{
    public Recipes(Plugin plugin)
    {
        // Adding all smelting related recipes
        for (NewSmeltable current : NewSmeltable.values())
        {
            NamespacedKey nsKey = new NamespacedKey(plugin, current.getInput().getKey().toString().replace("minecraft:", ""));
            FurnaceRecipe newRecipe = new FurnaceRecipe(nsKey, current.getOutput(), current.getInput(), current.getXp(), current.getTimeToSmelt());
            plugin.getServer().addRecipe(newRecipe);
        }

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

        /*NamespacedKey nsKey = new NamespacedKey(plugin, "test_recipe");
        ShapedRecipe newRecipe = new ShapedRecipe(nsKey, new ItemStack(Material.EXPERIENCE_BOTTLE));
        newRecipe.shape("   ", " ss", " ss");
        newRecipe.setIngredient('s', Material.SPONGE);
        plugin.getServer().addRecipe(newRecipe);*/
    }
}
