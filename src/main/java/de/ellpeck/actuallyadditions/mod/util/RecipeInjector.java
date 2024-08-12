package de.ellpeck.actuallyadditions.mod.util;

import net.minecraft.world.item.crafting.Recipe;
@SuppressWarnings("unchecked")
public class RecipeInjector<T extends Recipe<?>> {}/*implements RecipeOutput {
    private final RecipeOutput inner;
    private final Function<T, ? extends T> constructor;
    public RecipeInjector(RecipeOutput output, Function<T, ? extends T> constructorIn) {
        inner = output;
        this.constructor = constructorIn;
    }

    @Nonnull
    @Override
    public Advancement.Builder advancement() {
        return inner.advancement();
    }

    @Override
    public void accept(@Nonnull ResourceLocation resourceLocation, @Nonnull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, @Nonnull ICondition... iConditions) {
        inner.accept(resourceLocation, constructor.apply((T) recipe), advancementHolder, iConditions);
    }
}
TODO*/