package de.ellpeck.actuallyadditions.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CapHelper {
    @Nonnull
    public static LazyOptional<IItemHandler> getItemHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side);
            }
        }
        return LazyOptional.empty();
    }

    @Nonnull
    public static LazyOptional<IItemHandler> getItemHandler(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
    }
}
