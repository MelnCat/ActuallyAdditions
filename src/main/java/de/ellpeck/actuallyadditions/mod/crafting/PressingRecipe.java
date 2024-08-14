package de.ellpeck.actuallyadditions.mod.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.ellpeck.actuallyadditions.mod.util.GsonUtil;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PressingRecipe implements Recipe<Container> {
    public static final String NAME = "pressing";
    private final ResourceLocation ID;
    private final Ingredient input;
    private final FluidStack output;

    public PressingRecipe(ResourceLocation ID, Ingredient input, FluidStack output) {
        this.ID = ID;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(@Nonnull Container pInv, @Nullable Level pLevel) {
        return input.test(pInv.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public FluidStack getOutput() {
        return this.output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ActuallyRecipes.PRESSING_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ActuallyRecipes.Types.PRESSING.get();
    }

    public static class Serializer implements RecipeSerializer<PressingRecipe> {
        @Nonnull
        @Override
        public PressingRecipe fromJson(@Nonnull ResourceLocation pRecipeId, @Nonnull JsonObject pJson) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));
            FluidStack output = GsonUtil.getFluidStack(pJson, "fluid");

            return new PressingRecipe(pRecipeId, ingredient, output);
        }

        @Nullable
        @Override
        public PressingRecipe fromNetwork(@Nonnull ResourceLocation pRecipeId, @Nonnull FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ResourceLocation fluidRes = new ResourceLocation(pBuffer.readUtf());
            int fluidAmount = pBuffer.readInt();
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidRes);
            if(fluid == null)
                throw new JsonParseException("Unknown fluid '" + fluidRes + "'");
            FluidStack output = new FluidStack(fluid, fluidAmount);

            return new PressingRecipe(pRecipeId, ingredient, output);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf pBuffer, @Nonnull PressingRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeUtf(ForgeRegistries.FLUIDS.getKey(pRecipe.output.getFluid()).toString());
            pBuffer.writeInt(pRecipe.output.getAmount());
        }
    }
    public static class Result implements FinishedRecipe {
        private final ResourceLocation ID;
        private final Ingredient input;
        private final FluidStack output;

        public Result(ResourceLocation ID, Ingredient input, FluidStack output) {
            this.ID = ID;
            this.input = input;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", input.toJson());
            GsonUtil.setFluidStack(pJson, "fluid", output);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ActuallyRecipes.PRESSING_RECIPE.get();
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
