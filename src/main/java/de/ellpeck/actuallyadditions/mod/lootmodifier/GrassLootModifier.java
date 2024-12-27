package de.ellpeck.actuallyadditions.mod.lootmodifier;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.config.CommonConfig;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class GrassLootModifier extends LootModifier {
	public static final Supplier<Codec<GrassLootModifier>> CODEC = Suppliers.memoize(() ->
		RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, GrassLootModifier::new)));

	public GrassLootModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}
	
	private static final List<RegistryObject<Item>> seeds = List.of(
		ActuallyItems.RICE_SEEDS,
		ActuallyItems.COFFEE_BEANS,
		ActuallyItems.FLAX_SEEDS,
		ActuallyItems.CANOLA_SEEDS
	);

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		RandomSource random = context.getRandom();
		if (CommonConfig.Other.DO_SEEDS_DROPS.get()) {
			int looting = context.getLootingModifier();
			for (RegistryObject<Item> seed : seeds) {
				if (random.nextInt(18) <= looting * 2) {
					generatedLoot.add(new ItemStack(seed.get(), random.nextInt(1 + looting) + 1));
				}
			}
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
