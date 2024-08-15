package de.ellpeck.actuallyadditions.mod.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
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
	public static ItemStack getItemWithCount(JsonObject json, String key) {
		JsonObject obj = json.get(key).getAsJsonObject();
		Item item = GsonHelper.getAsItem(obj, "item");
		int count = GsonHelper.getAsInt(obj, "count", 1);
		return new ItemStack(item, count);
	}
	public static void setItemWithCount(JsonObject json, String key, ItemStack stack) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
		if (stack.getCount() != 1) obj.addProperty("count", stack.getCount());
		json.add(key, obj);
	}
}
