
package net.chixozhmix.chilib.compat;

import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.chixozhmix.chilib.utils.BrewingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class JEIBrewingRecipes implements IJeiBrewingRecipe {

    private final BrewingRecipe recipe;

    public JEIBrewingRecipes(BrewingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public List<ItemStack> getPotionInputs() {
        return List.of(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(recipe.getInputPotion())));
    }

    @Override
    public List<ItemStack> getIngredients() {
        return List.of(new ItemStack(recipe.getIngredient()));
    }

    @Override
    public ItemStack getPotionOutput() {
        return PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(recipe.getOutputPotion()));
    }

    @Override
    public int getBrewingSteps() {
        return 1;
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }
}
