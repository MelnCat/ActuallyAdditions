/*
 * This file ("RecipeKeepDataShapeless.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RecipeKeepDataShapeless extends ShapelessRecipe {
    public static String NAME = "copy_nbt_shapeless";
    public RecipeKeepDataShapeless(ResourceLocation id, String pGroup, CraftingBookCategory pCategory, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(id, pGroup, pCategory, pResult, pIngredients);
    }
    public RecipeKeepDataShapeless(ResourceLocation id, ShapelessRecipe recipe) {
        super(id, recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ActuallyRecipes.KEEP_DATA_SHAPELESS_RECIPE.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack result = super.assemble(pContainer, pRegistryAccess);

        TargetNBTIngredient donorIngredient = null;
        ItemStack datasource = ItemStack.EMPTY;
        NonNullList<Ingredient> ingredients = getIngredients();
        for (Ingredient ingredient : ingredients) {
            if (ingredient instanceof TargetNBTIngredient) {
                donorIngredient = (TargetNBTIngredient) ingredient;
                break;
            }
        }

        if (donorIngredient != null && !pContainer.isEmpty()) {
            for (int i = 0; i < pContainer.getContainerSize(); i++) {
                final ItemStack item = pContainer.getItem(i);
                if (!item.isEmpty() && donorIngredient.test(item)) {
                    datasource = item;
                    break;
                }
            }
        }

        if (!datasource.isEmpty() && datasource.hasTag())
            result.setTag(datasource.getTag().copy());
        else {
            ActuallyAdditions.LOGGER.info("AA.KeepDataShapeless missing TargetNBTIngredient");
            return ItemStack.EMPTY;
        }

        return result;
    }

    public static class Serializer implements RecipeSerializer<RecipeKeepDataShapeless> {
        @Override
        public RecipeKeepDataShapeless fromJson(ResourceLocation id, JsonObject jsonObject) {
            return new RecipeKeepDataShapeless(id, Objects.requireNonNull(RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, jsonObject)));
        }

        @Override
        public RecipeKeepDataShapeless fromNetwork(ResourceLocation id, FriendlyByteBuf pBuffer) {
            return new RecipeKeepDataShapeless(id, Objects.requireNonNull(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, pBuffer)));
        }

        @Override
        public void toNetwork(net.minecraft.network.FriendlyByteBuf pBuffer, RecipeKeepDataShapeless pRecipe) {
            try {
                RecipeSerializer.SHAPELESS_RECIPE.toNetwork(pBuffer, pRecipe);
            }
            catch (Exception e) {
                ActuallyAdditions.LOGGER.info("Failed to serialize " + NAME + " Recipe to packet: " + e.getMessage());
                throw e;
            }
        }
    }

    public static class Result implements FinishedRecipe {
        private final ShapelessRecipeBuilder.Result inner;
        public Result(ShapelessRecipeBuilder.Result result) {
            inner = result;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            inner.serializeRecipeData(jsonObject);
        }

        @Override
        public ResourceLocation getId() {
            return inner.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ActuallyRecipes.KEEP_DATA_SHAPELESS_RECIPE.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return inner.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return inner.getAdvancementId();
        }
    }
}
