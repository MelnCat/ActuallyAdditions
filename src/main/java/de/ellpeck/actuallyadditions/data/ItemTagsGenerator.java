package de.ellpeck.actuallyadditions.data;

import de.ellpeck.actuallyadditions.api.ActuallyTags;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagsGenerator extends ItemTagsProvider {
    public ItemTagsGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
        TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTagProvider.contentsGetter(), ActuallyAdditions.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ActuallyTags.Items.COFFEE_BEANS)
            .add(ActuallyItems.COFFEE_BEANS.get());
        tag(ActuallyTags.Items.TINY_COALS)
            .add(ActuallyItems.TINY_COAL.get())
            .add(ActuallyItems.TINY_CHARCOAL.get());
        tag(ActuallyTags.Items.DRILLS).add(
            ActuallyItems.DRILL_MAIN.get(),
            ActuallyItems.DRILL_BLACK.get(), ActuallyItems.DRILL_BLACK.get(), ActuallyItems.DRILL_BLUE.get(), ActuallyItems.DRILL_BROWN.get(),
            ActuallyItems.DRILL_CYAN.get(), ActuallyItems.DRILL_GRAY.get(), ActuallyItems.DRILL_GREEN.get(), ActuallyItems.DRILL_LIGHT_GRAY.get(),
            ActuallyItems.DRILL_LIME.get(), ActuallyItems.DRILL_MAGENTA.get(), ActuallyItems.DRILL_ORANGE.get(), ActuallyItems.DRILL_PINK.get(),
            ActuallyItems.DRILL_PURPLE.get(), ActuallyItems.DRILL_RED.get(), ActuallyItems.DRILL_WHITE.get(), ActuallyItems.DRILL_YELLOW.get()
        );
        tag(ActuallyTags.Items.CRYSTALS)
                .add(ActuallyItems.RESTONIA_CRYSTAL.get(), ActuallyItems.PALIS_CRYSTAL.get(),
                        ActuallyItems.DIAMATINE_CRYSTAL.get(), ActuallyItems.VOID_CRYSTAL.get(),
                        ActuallyItems.EMERADIC_CRYSTAL.get(), ActuallyItems.ENORI_CRYSTAL.get());
        this.tag(ActuallyTags.Items.CRYSTAL_BLOCKS)
                .add(ActuallyBlocks.RESTONIA_CRYSTAL.getItem(), ActuallyBlocks.PALIS_CRYSTAL.getItem(),
                        ActuallyBlocks.DIAMATINE_CRYSTAL.getItem(), ActuallyBlocks.VOID_CRYSTAL.getItem(),
                        ActuallyBlocks.EMERADIC_CRYSTAL.getItem(), ActuallyBlocks.ENORI_CRYSTAL.getItem());

        tag(Tags.Items.SLIMEBALLS)
                .add(ActuallyItems.RICE_SLIMEBALL.get());

        tag(ActuallyTags.Items.CROPS_RICE).add(ActuallyItems.RICE.get());
        tag(ActuallyTags.Items.CROPS_COFFEE).add(ActuallyItems.COFFEE_BEANS.get());
        tag(ActuallyTags.Items.CROPS_CANOLA).add(ActuallyItems.CANOLA.get());
        tag(ActuallyTags.Items.CROPS_FLAX).add(ActuallyItems.FLAX_SEEDS.get());
        tag(Tags.Items.CROPS).addTags(ActuallyTags.Items.CROPS_RICE, ActuallyTags.Items.CROPS_COFFEE, ActuallyTags.Items.CROPS_CANOLA, ActuallyTags.Items.CROPS_FLAX);

        tag(ActuallyTags.Items.SEEDS_RICE).add(ActuallyItems.RICE_SEEDS.get());
        tag(ActuallyTags.Items.SEEDS_COFFEE).add(ActuallyItems.COFFEE_BEANS.get());
        tag(ActuallyTags.Items.SEEDS_CANOLA).add(ActuallyItems.CANOLA_SEEDS.get());
        tag(ActuallyTags.Items.SEEDS_FLAX).add(ActuallyItems.FLAX_SEEDS.get());
        tag(Tags.Items.SEEDS).addTags(ActuallyTags.Items.SEEDS_RICE, ActuallyTags.Items.SEEDS_COFFEE, ActuallyTags.Items.SEEDS_CANOLA, ActuallyTags.Items.SEEDS_FLAX);

        tag(ActuallyTags.Items.GEMS_BLACK_QUARTZ).add(ActuallyItems.BLACK_QUARTZ.get());
        tag(ActuallyTags.Items.ORES_BLACK_QUARTZ).add(ActuallyBlocks.BLACK_QUARTZ_ORE.getItem());
        tag(Tags.Items.ORES).addTags(ActuallyTags.Items.ORES_BLACK_QUARTZ);
        tag(Tags.Items.ORES_IN_GROUND_STONE).add(ActuallyBlocks.BLACK_QUARTZ_ORE.getItem());
        tag(ActuallyTags.Items.STORAGE_BLOCKS_BLACK_QUARTZ).add(ActuallyBlocks.BLACK_QUARTZ.getItem());
    }
}
