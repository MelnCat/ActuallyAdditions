package de.ellpeck.actuallyadditions.mod.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EmpowererRecipe implements Recipe<Container> {
    public static String NAME = "empowering";
    protected final Ingredient input;
    protected final ItemStack output;

    protected final NonNullList<Ingredient> modifiers;

    protected final int energyPerStand;
    protected final int particleColor;
    protected final int time;

    public EmpowererRecipe(ItemStack output, Ingredient input, NonNullList<Ingredient> modifiers, int energyPerStand, int particleColor, int time) {
        this.input = input;
        this.output = output;
        this.modifiers = modifiers;
        this.energyPerStand = energyPerStand;
        this.particleColor = particleColor;
        this.time = time;
    }

    public boolean matches(ItemStack base, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4) {
        if (!input.test(base) || stand1.isEmpty() || stand2.isEmpty() || stand3.isEmpty() || stand4.isEmpty())
            return false;
        List<Ingredient> matches = new ArrayList<>();
        ItemStack[] stacks = {stand1, stand2, stand3, stand4};
        boolean[] unused = {true, true, true, true};
        for (ItemStack s : stacks) {
            if (unused[0] && this.modifiers.get(0).test(s)) {
                matches.add(this.modifiers.get(0));
                unused[0] = false;
            } else if (unused[1] && this.modifiers.get(1).test(s)) {
                matches.add(this.modifiers.get(1));
                unused[1] = false;
            } else if (unused[2] && this.modifiers.get(2).test(s)) {
                matches.add(this.modifiers.get(2));
                unused[2] = false;
            } else if (unused[3] && this.modifiers.get(3).test(s)) {
                matches.add(this.modifiers.get(3));
                unused[3] = false;
            }
        }

        return matches.size() == 4;
    }

    @Override
    public boolean matches(@Nonnull Container pInv, @Nonnull Level pLevel) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ActuallyRecipes.EMPOWERING_RECIPE.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return ActuallyRecipes.Types.EMPOWERING.get();
    }

    public Ingredient getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public Ingredient getStandOne() {
        return this.modifiers.get(0);
    }

    public Ingredient getStandTwo() {
        return this.modifiers.get(1);
    }

    public Ingredient getStandThree() {
        return this.modifiers.get(2);
    }

    public Ingredient getStandFour() {
        return this.modifiers.get(3);
    }

    public int getTime() {
        return this.time;
    }

    public int getEnergyPerStand() {
        return this.energyPerStand;
    }

    public int getParticleColors() {
        return this.particleColor;
    }

    public static class Serializer implements RecipeSerializer<EmpowererRecipe> {
        private static final Codec<EmpowererRecipe> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                                Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(recipe -> recipe.input),
                                Ingredient.CODEC_NONEMPTY
                                        .listOf()
                                        .fieldOf("modifiers")
                                        .flatXmap(
                                                list -> {
                                                    Ingredient[] aingredient = list
                                                            .toArray(Ingredient[]::new);
                                                    if (aingredient.length == 0) {
                                                        return DataResult.error(() -> "No modifiers for Empowering recipe");
                                                    } else {
                                                        return aingredient.length != 4
                                                                ? DataResult.error(() -> "Must have exactly 4 modifiers. has: %s".formatted(aingredient.length))
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(recipe -> recipe.modifiers),
                                        Codec.INT.fieldOf("energy").forGetter(recipe -> recipe.energyPerStand),
                                        Codec.INT.fieldOf("color").forGetter(recipe -> recipe.particleColor),
                                        Codec.INT.fieldOf("time").forGetter(recipe -> recipe.time)
                        )
                        .apply(instance, EmpowererRecipe::new)
        );

        @Override
        public Codec<EmpowererRecipe> codec() {
            return CODEC;
        }

        //        @Override
//        @Nonnull
//        public EmpowererRecipe fromJson(@Nonnull ResourceLocation pRecipeId, @Nonnull JsonObject pJson) {
//            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "base"));
//
//            JsonArray modifiers = GsonHelper.getAsJsonArray(pJson, "modifiers");
//            if (modifiers.size() != 4)
//                throw new IllegalStateException(pRecipeId.toString() + ": Must have exactly 4 modifiers, has: " + modifiers.size());
//
//            Ingredient mod1 = Ingredient.fromJson(modifiers.get(0));
//            Ingredient mod2 = Ingredient.fromJson(modifiers.get(1));
//            Ingredient mod3 = Ingredient.fromJson(modifiers.get(2));
//            Ingredient mod4 = Ingredient.fromJson(modifiers.get(3));
//            int energy = GsonHelper.getAsInt(pJson, "energy");
//            int color = GsonHelper.getAsInt(pJson, "color");
//            int time = GsonHelper.getAsInt(pJson, "time");
//            JsonObject resultObject = GsonHelper.getAsJsonObject(pJson, "result");
//            ItemStack result = new ItemStack(GsonHelper.getAsItem(resultObject, "item"));
//
//            return new EmpowererRecipe(pRecipeId, result, base, mod1, mod2, mod3, mod4, energy, color, time);
//        }

        @Nullable
        @Override
        public EmpowererRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            ItemStack result = pBuffer.readItem();
            Ingredient input = Ingredient.fromNetwork(pBuffer);

            int i = pBuffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(pBuffer));
            }

            int energy = pBuffer.readInt();
            int color = pBuffer.readInt();
            int time = pBuffer.readInt();

            return new EmpowererRecipe(result, input, nonnulllist, energy, color, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EmpowererRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.output);
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeVarInt(pRecipe.modifiers.size());
            for (Ingredient modifier : pRecipe.modifiers) {
                modifier.toNetwork(pBuffer);
            }
            pBuffer.writeInt(pRecipe.energyPerStand);
            pBuffer.writeInt(pRecipe.particleColor);
            pBuffer.writeInt(pRecipe.time);
        }
    }
}
