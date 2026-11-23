
package net.chixozhmix.chilib.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class BrewingRecipe implements IBrewingRecipe {

    private final Potion input;
    private final Item ingredient;
    private final Potion output;

    public BrewingRecipe(Potion input, Item ingredient, Potion output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);

        return contents != null && contents.potion().map(Holder::value).orElse(null) == input;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return stack.getItem() == ingredient;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient))
            return ItemStack.EMPTY;

        ItemStack result = new ItemStack(input.getItem());
        result.set(DataComponents.POTION_CONTENTS, new PotionContents(BuiltInRegistries.POTION.wrapAsHolder(output)));

        return result;
    }

    public Potion getInputPotion() {
        return input;
    }

    public Item getIngredient() {
        return ingredient;
    }

    public Potion getOutputPotion() {
        return output;
    }
}
