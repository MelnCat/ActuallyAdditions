package de.ellpeck.actuallyadditions.mod.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CrushingRecipe implements Recipe<Container> {
    public static String NAME = "crushing";
    protected Ingredient input;
    protected NonNullList<CrushingResult> outputs;
    private final ResourceLocation id;

    public CrushingRecipe(ResourceLocation id, Ingredient input, NonNullList<CrushingResult> outputList) {
        this.id = id;
        this.input = input;
        this.outputs = outputList;
    }

    public CrushingRecipe(ResourceLocation id, Ingredient input, ItemStack outputOne, float chance1, ItemStack outputTwo, float chance2) {
        this(id, input, createList(new CrushingResult(outputOne, chance1), new CrushingResult(outputTwo, chance2)));
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    private static NonNullList<CrushingResult> createList(CrushingResult... results) {
        NonNullList<CrushingResult> list = NonNullList.create();
        for (CrushingResult result : results) {
            list.add(result);
        }
        return list;
    }

    @Override
    public boolean matches(Container pInv, Level pLevel) {
        return input.test(pInv.getItem(0));
    }

    public boolean matches (ItemStack stack) {
        return input.test(stack);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.outputs.get(0).stack.copy();
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ActuallyRecipes.CRUSHING_RECIPE.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return ActuallyRecipes.Types.CRUSHING.get();
    }

    public ItemStack getOutputOne() {
        return this.outputs.get(0).stack;
    }

    public ItemStack getOutputTwo() {
        return this.outputs.get(1).stack;
    }

    public float getFirstChance() {
        return this.outputs.get(0).chance;
    }
    public float getSecondChance() {
        return this.outputs.get(1).chance;
    }

    public Ingredient getInput() {
        return this.input;
    }


    public record CrushingResult(ItemStack stack, float chance) {
        public static final CrushingResult EMPTY = new CrushingResult(ItemStack.EMPTY, 0.0F);
    };

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {
        @Override
        @Nonnull
        public CrushingRecipe fromJson(@Nonnull ResourceLocation pRecipeId, @Nonnull JsonObject pJson) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));

            JsonArray resultList = GsonHelper.getAsJsonArray(pJson, "result");
            if (resultList.isEmpty())
                throw new IllegalStateException(pRecipeId.toString() + ": Recipe must contain at least 1 result item");

            NonNullList<CrushingResult> results = NonNullList.create();

            resultList.asList().stream().map(x ->
                new CrushingResult(ItemStack.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(x.getAsJsonObject(), "result")).getOrThrow(false, e -> {
                    throw new IllegalStateException(e);
                }).getFirst(), GsonHelper.getAsFloat(x.getAsJsonObject(), "chance", 1.0f))
                ).forEach(results::add);

            return new CrushingRecipe(pRecipeId, ingredient, results);
        }

        @Override
        public CrushingRecipe fromNetwork(@NotNull ResourceLocation id, @Nonnull FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);

            int i = pBuffer.readVarInt();

            NonNullList<CrushingResult> nonnulllist = NonNullList.withSize(i, CrushingResult.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, new CrushingResult(pBuffer.readItem(), pBuffer.readFloat()));
            }

            return new CrushingRecipe(id, ingredient, nonnulllist);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf pBuffer, CrushingRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeVarInt(pRecipe.outputs.size());
            for (CrushingResult result : pRecipe.outputs) {
                pBuffer.writeItem(result.stack);
                pBuffer.writeFloat(result.chance);
            }
        }
    }

    public static class Result implements FinishedRecipe {
        public static String NAME = "crushing";
        protected Ingredient input;
        protected NonNullList<CrushingResult> outputs;
        private final ResourceLocation id;

        public Result(ResourceLocation id, Ingredient input, NonNullList<CrushingResult> outputList) {
            this.id = id;
            this.input = input;
            this.outputs = outputList;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", input.toJson());
            JsonArray results = new JsonArray();
            for (CrushingResult result : outputs) {
                JsonObject resultJson = new JsonObject();
                resultJson.add("result", ItemStack.CODEC.encode(result.stack(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, e -> {
                    throw new IllegalStateException(e);
                }));
                resultJson.addProperty("chance", result.chance);
                results.add(resultJson);
            }
            pJson.add("result", results);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ActuallyRecipes.CRUSHING_RECIPE.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}