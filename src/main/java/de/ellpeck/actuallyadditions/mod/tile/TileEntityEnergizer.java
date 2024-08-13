/*
 * This file ("TileEntityEnergizer.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerEnergizer;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA.IAcceptor;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA.IRemover;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityEnergizer extends TileEntityInventoryBase implements MenuProvider {

    public final CustomEnergyStorage storage = new CustomEnergyStorage(50000, 1000, 0);
    public final LazyOptional<IEnergyStorage> lazyEnergy = LazyOptional.of(() -> this.storage);
    private int lastEnergy;

    public TileEntityEnergizer(BlockPos pos, BlockState state) {
        super(ActuallyBlocks.ENERGIZER.getTileEntityType(), pos, state, 2);
    }

    @Override
    public void writeSyncableNBT(CompoundTag compound, NBTType type) {
        this.storage.writeToNBT(compound);
        super.writeSyncableNBT(compound, type);
    }

    @Override
    public void readSyncableNBT(CompoundTag compound, NBTType type) {
        this.storage.readFromNBT(compound);
        super.readSyncableNBT(compound, type);
    }

    public static <T extends BlockEntity> void clientTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityEnergizer tile) {
            tile.clientTick();
        }
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityEnergizer tile) {
            tile.serverTick();

            if (!tile.inv.getStackInSlot(0).isEmpty() && tile.inv.getStackInSlot(1).isEmpty()) {
                if (tile.storage.getEnergyStored() > 0) {
                    int received = tile.inv.getStackInSlot(0).getCapability(ForgeCapabilities.ENERGY, null).map(cap -> cap.receiveEnergy(tile.storage.getEnergyStored(), false)).orElse(0);
                    boolean canTakeUp = tile.inv.getStackInSlot(0).getCapability(ForgeCapabilities.ENERGY, null).map(cap -> cap.getEnergyStored() >= cap.getMaxEnergyStored()).orElse(false);

                    if (received > 0) {
                        tile.storage.extractEnergyInternal(received, false);
                    }

                    if (canTakeUp) {
                        tile.inv.setStackInSlot(1, tile.inv.getStackInSlot(0).copy());
                        tile.inv.setStackInSlot(0, StackUtil.shrink(tile.inv.getStackInSlot(0), 1));
                    }
                }
            }

            if (tile.lastEnergy != tile.storage.getEnergyStored() && tile.sendUpdateWithInterval()) {
                tile.lastEnergy = tile.storage.getEnergyStored();
            }
        }
    }

    @Override
    public IAcceptor getAcceptor() {
        return (slot, stack, automation) -> !automation || slot == 0 && stack.getCapability(ForgeCapabilities.ENERGY, null).isPresent();
    }

    @Override
    public IRemover getRemover() {
        return (slot, automation) -> !EnchantmentHelper.hasBindingCurse(this.inv.getStackInSlot(slot)) && !automation || slot == 1;
    }

    public int getEnergyScaled(int i) {
        return this.storage.getEnergyStored() * i / this.storage.getMaxEnergyStored();
    }

    @Override
    public LazyOptional<IEnergyStorage> getEnergyStorage(Direction facing) {
        return this.lazyEnergy;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.actuallyadditions.energizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new ContainerEnergizer(windowId, playerInventory, this);
    }
}
