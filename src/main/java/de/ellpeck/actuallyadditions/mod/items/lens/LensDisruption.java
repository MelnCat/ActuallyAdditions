/*
 * This file ("LensColor.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.lens;

import de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor;
import de.ellpeck.actuallyadditions.api.lens.Lens;
import de.ellpeck.actuallyadditions.mod.crafting.ColorChangeRecipe;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class LensDisruption extends Lens {

    public static final int ENERGY_USE = 100000;
    private final Random rand = new Random();
    private final RandomSource randomSource = RandomSource.create();

    @Override
    public boolean invoke(BlockState hitState, BlockPos hitBlock, IAtomicReconstructor tile) {
        if (tile.getEnergy() >= ENERGY_USE) {
            List<ItemEntity> items = tile.getWorldObject().getEntitiesOfClass(ItemEntity.class, new AABB(hitBlock.getX(), hitBlock.getY(), hitBlock.getZ(), hitBlock.getX() + 1, hitBlock.getY() + 1, hitBlock.getZ() + 1));
            for (ItemEntity item : items) {
                if (item.isAlive() && !item.getItem().isEmpty() && tile.getEnergy() >= ENERGY_USE) {
                    ItemStack newStack = new ItemStack(BuiltInRegistries.ITEM.getRandom(randomSource).get().get(), item.getItem().getCount());
                    if (!newStack.isEmpty()) {
                        item.discard();

                        ItemEntity newItem = new ItemEntity(tile.getWorldObject(), item.getX(), item.getY(), item.getZ(), newStack);
                        tile.getWorldObject().addFreshEntity(newItem);

                        tile.extractEnergy(ENERGY_USE);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getColor() {
        return 0x440000;
    }

    @Override
    public int getDistance() {
        return 4;
    }

    @Override
    public boolean canInvoke(IAtomicReconstructor tile, Direction sideToShootTo, int energyUsePerShot) {
        return tile.getEnergy() - energyUsePerShot >= ENERGY_USE;
    }
}
