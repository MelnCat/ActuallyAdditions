/*
 * This file ("TileEntityCoalGenerator.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.SolidFuelRecipe;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerCoalGenerator;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA.IAcceptor;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA.IRemover;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class TileEntityCoalGenerator extends TileEntityInventoryBase implements MenuProvider, ISharingEnergyProvider, IEnergyDisplay {

    public final CustomEnergyStorage storage = new CustomEnergyStorage(60000, 0, 80);
    public final LazyOptional<IEnergyStorage> lazyEnergy = LazyOptional.of(() -> this.storage);
    public int maxBurnTime;
    public int currentBurnTime;
    private int lastEnergy;
    private int lastBurnTime;
    private int lastCurrentBurnTime;
    private int lastCompare;
    private SolidFuelRecipe currentRecipe = null;

    public TileEntityCoalGenerator(BlockPos pos, BlockState state) {
        super(ActuallyBlocks.COAL_GENERATOR.getTileEntityType(), pos, state, 1);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnergyScaled(int i) {
        return this.storage.getEnergyStored() * i / this.storage.getMaxEnergyStored();
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurningScaled(int i) {
        return this.currentBurnTime * i / this.maxBurnTime;
    }

    @Override
    public void writeSyncableNBT(CompoundTag compound, NBTType type) {
        if (type != NBTType.SAVE_BLOCK) {
            compound.putInt("BurnTime", this.currentBurnTime);
            compound.putInt("MaxBurnTime", this.maxBurnTime);
            if (currentRecipe != null)
                compound.putString("currentRecipe", currentRecipe.getId().toString());
        }
        this.storage.writeToNBT(compound);
        super.writeSyncableNBT(compound, type);
    }

    @Override
    public void readSyncableNBT(CompoundTag compound, NBTType type) {
        if (type != NBTType.SAVE_BLOCK) {
            this.currentBurnTime = compound.getInt("BurnTime");
            this.maxBurnTime = compound.getInt("MaxBurnTime");
            if (compound.contains("currentRecipe")) {
                ResourceLocation id = new ResourceLocation(compound.getString("currentRecipe"));
				level.getRecipeManager().getAllRecipesFor(ActuallyRecipes.Types.SOLID_FUEL.get())
					.stream().filter(x -> x.getId().equals(id))
					.findFirst().ifPresent(x -> currentRecipe = x);
            }
        }
        this.storage.readFromNBT(compound);
        super.readSyncableNBT(compound, type);
    }

    public static <T extends BlockEntity> void clientTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityCoalGenerator tile) {
            tile.clientTick();
        }
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityCoalGenerator tile) {
            tile.serverTick();

            boolean flag = tile.currentBurnTime > 0;

            if (tile.currentBurnTime > 0 && tile.currentRecipe != null) {
                tile.currentBurnTime--;
                int produce = tile.currentRecipe.getTotalEnergy() / tile.currentRecipe.getBurnTime();
                if (produce > 0) {
                    tile.storage.receiveEnergyInternal(produce, false);
                }
            }

            if (!tile.isRedstonePowered && tile.currentBurnTime <= 0 && tile.storage.getEnergyStored() < tile.storage.getMaxEnergyStored()) {
                ItemStack stack = tile.inv.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    level.getRecipeManager().getAllRecipesFor(ActuallyRecipes.Types.SOLID_FUEL.get()).stream()
                        .filter(r -> r.matches(stack)).findFirst().ifPresent(recipe -> {
                        tile.currentRecipe = recipe;
		                    tile.maxBurnTime = recipe.getBurnTime();
		                    tile.currentBurnTime = tile.maxBurnTime;
		                    tile.inv.setStackInSlot(0, StackUtil.shrinkForContainer(stack, 1));
	                    });
                } else
                    tile.currentRecipe = null;
            }

            if (flag != tile.currentBurnTime > 0 || tile.lastCompare != tile.getComparatorStrength()) {
                tile.lastCompare = tile.getComparatorStrength();
                tile.setChanged();
            }

            if ((tile.storage.getEnergyStored() != tile.lastEnergy || tile.currentBurnTime != tile.lastCurrentBurnTime || tile.lastBurnTime != tile.maxBurnTime) && tile.sendUpdateWithInterval()) {
                tile.lastEnergy = tile.storage.getEnergyStored();
                tile.lastCurrentBurnTime = tile.currentBurnTime;
                tile.lastBurnTime = tile.currentBurnTime;
            }
        }
    }

    @Override
    public int getComparatorStrength() {
        float calc = (float) this.storage.getEnergyStored() / (float) this.storage.getMaxEnergyStored() * 15F;
        return (int) calc;
    }

    @Override
    public IAcceptor getAcceptor() {
        return (slot, stack, automation) -> {
            return level.getRecipeManager().getAllRecipesFor(ActuallyRecipes.Types.SOLID_FUEL.get()).stream()
            .anyMatch(x -> x.matches(stack));
        };
    }

    @Override
    public IRemover getRemover() {
        return (slot, automation) -> {
            if (!automation) {
                return true;
            }
            return ForgeHooks.getBurnTime(this.inv.getStackInSlot(0), null) <= 0;
        };
    }

    @Override
    public int getEnergyToSplitShare() {
        return this.storage.getEnergyStored();
    }

    @Override
    public boolean doesShareEnergy() {
        return true;
    }

    @Override
    public Direction[] getEnergyShareSides() {
        return Direction.values();
    }

    @Override
    public boolean canShareTo(BlockEntity tile) {
        return true;
    }

    @Override
    public LazyOptional<IEnergyStorage> getEnergyStorage(Direction facing) {
        return this.lazyEnergy;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.actuallyadditions.coalGenerator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new ContainerCoalGenerator(windowId, playerInventory, this);
    }

    @Override
    public CustomEnergyStorage getEnergyStorage() {
        return storage;
    }

    @Override
    public boolean needsHoldShift() {
        return false;
    }
}
