package de.ellpeck.actuallyadditions.mod.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class RecipeInjector<T extends FinishedRecipe> implements Consumer<FinishedRecipe> {
    private final Consumer<FinishedRecipe> inner;
    private final Function<T, FinishedRecipe> constructor;
    public RecipeInjector(Consumer<FinishedRecipe> consumer, Function<T, FinishedRecipe> constructorIn) {
        inner = consumer;
        this.constructor = constructorIn;
    }

	@Override
	public void accept(FinishedRecipe finishedRecipe) {
		inner.accept(constructor.apply((T) finishedRecipe));
	}
}