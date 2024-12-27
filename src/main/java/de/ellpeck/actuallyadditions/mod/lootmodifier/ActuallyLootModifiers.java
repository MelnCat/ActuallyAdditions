package de.ellpeck.actuallyadditions.mod.lootmodifier;

import com.mojang.serialization.Codec;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ActuallyLootModifiers {
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ActuallyAdditions.MODID);

	public static final Supplier<Codec<? extends IGlobalLootModifier>> BAT_LOOT = GLM.register("bat_loot", BatLootModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> DUNGEON_LOOT = GLM.register("dungeon_loot", DungeonLootModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> SOLID_XP_LOOT = GLM.register("solid_xp_loot", SolidXpLootModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> GRASS_LOOT = GLM.register("grass_loot", GrassLootModifier.CODEC);

	public static void init(IEventBus evt) {
		GLM.register(evt);
	}
}
