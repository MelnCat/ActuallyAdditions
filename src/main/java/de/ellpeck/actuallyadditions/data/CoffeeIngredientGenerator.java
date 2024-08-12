package de.ellpeck.actuallyadditions.data;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.crafting.CoffeeIngredientRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

public class CoffeeIngredientGenerator extends RecipeProvider {
    public CoffeeIngredientGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public String getName() {
        return "Coffee Ingredient " + super.getName();
    }

    @Override
    protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        buildIngredient(consumer, Items.MILK_BUCKET, 0, "jei." + ActuallyAdditions.MODID + ".coffee.extra.milk");

        //Pam's puts milk in a tag, so we'll use that
        TagKey<Item> milkTag = ItemTags.create(new ResourceLocation("forge", "milk"));
        ConditionalRecipe.builder()
            .addCondition(new NotCondition(new TagEmptyCondition(milkTag.location())))
            .addRecipe(new CoffeeIngredientRecipe.Result(
                new ResourceLocation(ActuallyAdditions.MODID, "coffee_ingredient/milk_tagged"),
                Ingredient.of(milkTag),
                new ArrayList<>(),
                0,
                "jei." + ActuallyAdditions.MODID + ".coffee.extra.milk"
            ))
            .generateAdvancement().build(consumer, new ResourceLocation(ActuallyAdditions.MODID, "coffee_ingredient/milk_tagged"));

        buildIngredient(consumer, Items.SUGAR, 4, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 0));
        buildIngredient(consumer, Items.MAGMA_CREAM, 2, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0));
        buildIngredient(consumer, Items.PUFFERFISH, 2, new MobEffectInstance(MobEffects.WATER_BREATHING, 10, 0));
        buildIngredient(consumer, Items.GOLDEN_CARROT, 2, new MobEffectInstance(MobEffects.NIGHT_VISION, 30, 0));
        buildIngredient(consumer, Items.GHAST_TEAR, 3, new MobEffectInstance(MobEffects.REGENERATION, 5, 0));
        buildIngredient(consumer, Items.BLAZE_POWDER, 4, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 15, 0));
        buildIngredient(consumer, Items.FERMENTED_SPIDER_EYE, 2, new MobEffectInstance(MobEffects.INVISIBILITY, 25, 0));
    }

    private void buildIngredient(Consumer<FinishedRecipe> consumer, ItemLike ingredient, int maxAmplifier, MobEffectInstance... effects) {
        buildIngredient(consumer, ingredient, maxAmplifier, "", effects);
    }

    private void buildIngredient(Consumer<FinishedRecipe> consumer, ItemLike ingredient, int maxAmplifier, String extraText, MobEffectInstance... effects) {
        ResourceLocation id = new ResourceLocation(ActuallyAdditions.MODID, "coffee_ingredient/" + getItemName(ingredient));
        NonNullList<CoffeeIngredientRecipe.EffectInstance> instances = NonNullList.create();
        for (MobEffectInstance effect : effects) {
            instances.add(new CoffeeIngredientRecipe.EffectInstance(effect));
        }
        consumer.accept(new CoffeeIngredientRecipe.Result(id, Ingredient.of(ingredient), instances, maxAmplifier, extraText));
    }

    private void buildIngredient(Consumer<FinishedRecipe> consumer, ResourceLocation id, Ingredient ingredient, int maxAmplifier, MobEffectInstance... effects) {
        buildIngredient(consumer, id, ingredient, maxAmplifier, "", effects);
    }

    private void buildIngredient(Consumer<FinishedRecipe> consumer, ResourceLocation id, Ingredient ingredient, int maxAmplifier, String extraText, MobEffectInstance... effects) {
        NonNullList<CoffeeIngredientRecipe.EffectInstance> instances = NonNullList.create();
        for (MobEffectInstance effect : effects) {
            instances.add(new CoffeeIngredientRecipe.EffectInstance(effect));
        }
        consumer.accept(new CoffeeIngredientRecipe.Result(id, ingredient, instances, maxAmplifier, extraText));
    }

}
