package de.ellpeck.actuallyadditions.mod.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.config.conditions.BoolConfigCondition;
import de.ellpeck.actuallyadditions.mod.gen.modifier.BoolConfigFeatureBiomeModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ActuallyBiomeModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ActuallyAdditions.MODID);
	public static final Supplier<Codec<BoolConfigFeatureBiomeModifier>> BOOL_CONFIG_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("bool_config_feature_modifier", () ->
		RecordCodecBuilder.create(builder -> builder.group(
			Biome.LIST_CODEC.fieldOf("biomes").forGetter(BoolConfigFeatureBiomeModifier::biomes),
			PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(BoolConfigFeatureBiomeModifier::features),
			GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(BoolConfigFeatureBiomeModifier::step),
			Codec.STRING.fieldOf("boolConfig").forGetter(BoolConfigFeatureBiomeModifier::boolConfig)
		).apply(builder, BoolConfigFeatureBiomeModifier::new))
	);

	public static void init(IEventBus bus) {
		BIOME_MODIFIER_SERIALIZERS.register(bus);
	}

	protected static final ResourceKey<BiomeModifier> ADD_BLACK_QUARTZ_ORE_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
			new ResourceLocation(ActuallyAdditions.MODID, "add_black_quartz"));

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		HolderSet.Named<Biome> overworldHolder = biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD);

		context.register(ADD_BLACK_QUARTZ_ORE_MODIFIER, new BoolConfigFeatureBiomeModifier(
				overworldHolder,
				HolderSet.direct(placedGetter.getOrThrow(ActuallyPlacedFeatures.PLACED_ORE_BLACK_QUARTZ)),
				GenerationStep.Decoration.UNDERGROUND_ORES, "generateQuartz"
		));
	}
}
