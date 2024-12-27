/*
 * This file ("CustomEnergyStorage.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

import java.util.Optional;

public class CustomItemEnergyStorage extends EnergyStorage {
	private ItemStack item;
	public CustomItemEnergyStorage(ItemStack item, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.item = item;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		energy = getEnergyStored();
		int received = super.receiveEnergy(maxReceive, simulate);
		item.getOrCreateTag().putInt("Energy", energy);
		return received;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		energy = getEnergyStored();
		int extracted = super.extractEnergy(maxExtract, simulate);
		item.getOrCreateTag().putInt("Energy", energy);
		return extracted;
	}

	public void setEnergyStored(int energy) {
		this.energy = energy;
		item.getOrCreateTag().putInt("Energy", energy);
	}

	@Override
	public int getEnergyStored() {
		return Optional.ofNullable(item.getTag()).map(tag -> tag.getInt("Energy")).orElse(0);
	}

	public int extractEnergyInternal(int maxExtract, boolean simulate) {
		int before = this.maxExtract;
		this.maxExtract = Integer.MAX_VALUE;

		int toReturn = this.extractEnergy(maxExtract, simulate);

		this.maxExtract = before;
		return toReturn;
	}

	public int receiveEnergyInternal(int maxReceive, boolean simulate) {
		int before = this.maxReceive;
		this.maxReceive = Integer.MAX_VALUE;

		int toReturn = this.receiveEnergy(maxReceive, simulate);

		this.maxReceive = before;
		return toReturn;
	}
}
