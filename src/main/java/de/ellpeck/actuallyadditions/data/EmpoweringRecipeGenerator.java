package de.ellpeck.actuallyadditions.data;

import com.google.gson.JsonObject;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.crafting.EmpowererRecipe;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.items.metalists.Crystals;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EmpoweringRecipeGenerator extends RecipeProvider {
    public EmpoweringRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public String getName() {
        return "Empowering " + super.getName();
    }

    @Override
    protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
        return null; //Nope...
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_RESTONIA_CRYSTAL.get(), ActuallyItems.RESTONIA_CRYSTAL.get(), 5000, 50, Crystals.REDSTONE.conversionColorParticles)
                .addModifier(Tags.Items.DYES_RED)
                .addModifier(Items.NETHER_BRICK)
                .addModifier(Tags.Items.DUSTS_REDSTONE)
                .addModifier(Items.BRICK).save(consumer, "restonia");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_RESTONIA_CRYSTAL.get(), ActuallyBlocks.RESTONIA_CRYSTAL.get(), 50000, 500, Crystals.REDSTONE.conversionColorParticles)
                .addModifier(Tags.Items.DYES_RED)
                .addModifier(Items.NETHER_BRICK)
                .addModifier(Tags.Items.DUSTS_REDSTONE)
                .addModifier(Items.BRICK).save(consumer, "restonia_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_PALIS_CRYSTAL.get(), ActuallyItems.PALIS_CRYSTAL.get(), 5000, 50, Crystals.LAPIS.conversionColorParticles)
                .addModifier(Tags.Items.DYES_CYAN)
                .addModifier(Items.PRISMARINE_SHARD)
                .addModifier(Items.PRISMARINE_SHARD)
                .addModifier(Items.PRISMARINE_SHARD).save(consumer, "palis");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_PALIS_CRYSTAL.get(), ActuallyBlocks.PALIS_CRYSTAL.get(), 50000, 500, Crystals.LAPIS.conversionColorParticles)
                .addModifier(Tags.Items.DYES_CYAN)
                .addModifier(Items.PRISMARINE_SHARD)
                .addModifier(Items.PRISMARINE_SHARD)
                .addModifier(Items.PRISMARINE_SHARD).save(consumer, "palis_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_DIAMATINE_CRYSTAL.get(), ActuallyItems.DIAMATINE_CRYSTAL.get(), 5000, 50, Crystals.DIAMOND.conversionColorParticles)
                .addModifier(Tags.Items.DYES_LIGHT_BLUE)
                .addModifier(Items.CLAY_BALL)
                .addModifier(Items.CLAY_BALL)
                .addModifier(Items.CLAY).save(consumer, "diamatine");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_DIAMATINE_CRYSTAL.get(), ActuallyBlocks.DIAMATINE_CRYSTAL.get(), 50000, 500, Crystals.DIAMOND.conversionColorParticles)
                .addModifier(Tags.Items.DYES_LIGHT_BLUE)
                .addModifier(Items.CLAY_BALL)
                .addModifier(Items.CLAY_BALL)
                .addModifier(Items.CLAY).save(consumer, "diamatine_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_ENORI_CRYSTAL.get(), ActuallyItems.ENORI_CRYSTAL.get(), 5000, 50, Crystals.IRON.conversionColorParticles)
                .addModifier(Tags.Items.DYES_GRAY)
                .addModifier(Items.SNOWBALL)
                .addModifier(Items.STONE_BUTTON)
                .addModifier(Tags.Items.COBBLESTONE).save(consumer, "enori");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_ENORI_CRYSTAL.get(), ActuallyBlocks.ENORI_CRYSTAL.get(), 50000, 500, Crystals.IRON.conversionColorParticles)
                .addModifier(Tags.Items.DYES_GRAY)
                .addModifier(Items.SNOWBALL)
                .addModifier(Items.STONE_BUTTON)
                .addModifier(Tags.Items.COBBLESTONE).save(consumer, "enori_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_VOID_CRYSTAL.get(), ActuallyItems.VOID_CRYSTAL.get(), 5000, 50, Crystals.COAL.conversionColorParticles)
                .addModifier(Tags.Items.DYES_BLACK)
                .addModifier(ItemTags.COALS)
                .addModifier(Items.FLINT)
                .addModifier(Tags.Items.STONE).save(consumer, "void");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_VOID_CRYSTAL.get(), ActuallyBlocks.VOID_CRYSTAL.get(), 50000, 500, Crystals.COAL.conversionColorParticles)
                .addModifier(Tags.Items.DYES_BLACK)
                .addModifier(ItemTags.COALS)
                .addModifier(Items.FLINT)
                .addModifier(Tags.Items.STONE).save(consumer, "void_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_EMERADIC_CRYSTAL.get(), ActuallyItems.EMERADIC_CRYSTAL.get(), 5000, 50, Crystals.EMERALD.conversionColorParticles)
                .addModifier(Tags.Items.DYES_LIME)
                .addModifier(Items.GRASS)
                .addModifier(ItemTags.SAPLINGS)
                .addModifier(Tags.Items.SLIMEBALLS).save(consumer, "emeradic");
        EmpoweringBuilder.builder(ActuallyBlocks.EMPOWERED_EMERADIC_CRYSTAL.get(), ActuallyBlocks.EMERADIC_CRYSTAL.get(), 50000, 500, Crystals.EMERALD.conversionColorParticles)
                .addModifier(Tags.Items.DYES_LIME)
                .addModifier(Items.GRASS)
                .addModifier(ItemTags.SAPLINGS)
                .addModifier(Tags.Items.SLIMEBALLS).save(consumer, "emeradic_block");

        EmpoweringBuilder.builder(ActuallyItems.EMPOWERED_CANOLA_SEED.get(), ActuallyItems.CRYSTALLIZED_CANOLA_SEED.get(), 1000, 30, 0xFF5B4C)
                .addModifier(ActuallyItems.CANOLA_SEEDS.get())
                .addModifier(ActuallyItems.CANOLA_SEEDS.get())
                .addModifier(ActuallyItems.CANOLA_SEEDS.get())
                .addModifier(ActuallyItems.CANOLA_SEEDS.get()).save(consumer, "empowered_canola");
    }


    public static class EmpoweringBuilder {
        private final Item result;
        private final Ingredient base;
        private final int energy;
        private final int time;
        private final int color;
        private final List<Ingredient> modifiers = new ArrayList<>();

        public EmpoweringBuilder(ItemLike resultIn, Ingredient baseIn, int energyIn, int timeIn, int colorIn) {
            result = resultIn.asItem();
            base = baseIn;
            energy = energyIn;
            time = timeIn;
            color = colorIn;
        }

        public static EmpoweringBuilder builder(ItemLike resultIn, ItemLike base, int energyIn, int timeIn, int colorIn) {
            return new EmpoweringBuilder(resultIn, Ingredient.of(base), energyIn, timeIn, colorIn);
        }

        public EmpoweringBuilder addModifier(ItemLike input) {
            if (modifiers.size() >= 4)
                throw new IllegalStateException("too many modifiers for empowering recipe, input: " + ForgeRegistries.ITEMS.getKey(input.asItem()));
            modifiers.add(Ingredient.of(input));
            return this;
        }

        public EmpoweringBuilder addModifier(ItemStack input) {
            if (modifiers.size() >= 4)
                throw new IllegalStateException("too many modifiers for empowering recipe, input: " + ForgeRegistries.ITEMS.getKey(input.getItem()));
            modifiers.add(Ingredient.of(input));
            return this;
        }

        public EmpoweringBuilder addModifier(TagKey<Item> input) {
            if (modifiers.size() >= 4)
                throw new IllegalStateException("too many modifiers for empowering recipe, input: " + input.toString());
            modifiers.add(Ingredient.of(input));
            return this;
        }

        public void save(Consumer<FinishedRecipe> consumer, ResourceLocation name) {
            if (modifiers.size() != 4)
                throw new IllegalStateException("invalid modifier count: " + modifiers.size() + ", recipe: " + name.toString());
            consumer.accept(new EmpowererRecipe.Result(name, result, base, modifiers.get(0), modifiers.get(1), modifiers.get(2), modifiers.get(3), energy, color, time));
        }

        public void save(Consumer<FinishedRecipe> consumer, String name) {
            ResourceLocation res = new ResourceLocation(ActuallyAdditions.MODID, "empowering/" + name);
            if (modifiers.size() != 4)
                throw new IllegalStateException("invalid modifier count: " + modifiers.size() + ", recipe: " + res);
            consumer.accept(new EmpowererRecipe.Result(res, result, base, modifiers.get(0), modifiers.get(1), modifiers.get(2), modifiers.get(3), energy, color, time));
        }
    }
}
