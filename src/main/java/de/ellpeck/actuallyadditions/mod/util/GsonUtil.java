package de.ellpeck.actuallyadditions.mod.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class GsonUtil {
	public static ItemStack getItemStack(JsonObject json, String key) {
		return ItemStack.CODEC.parse(JsonOps.INSTANCE, json.get(key)).getOrThrow(false, e -> {
			throw new IllegalStateException(e);
		});
	}
	public static void setItemStack(JsonObject json, String key, ItemStack item) {
		json.add(key, ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, item).getOrThrow(false, e -> {
			throw new IllegalStateException(e);
		}));
	}
	public static FluidStack getFluidStack(JsonObject json, String key) {
		return FluidStack.CODEC.parse(JsonOps.INSTANCE, json.get(key)).getOrThrow(false, e -> {
			throw new IllegalStateException(e);
		});
	}
	public static void setFluidStack(JsonObject json, String key, FluidStack stack) {
		json.add(key, FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getOrThrow(false, e -> {
			throw new IllegalStateException(e);
		}));
	}
}
