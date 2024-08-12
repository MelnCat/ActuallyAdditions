/*
 * This file ("ItemEnergy.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.base;

import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.tile.CustomEnergyStorage;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public abstract class ItemEnergy extends ItemBase {

    private final int maxPower;
    private final int transfer;

    public ItemEnergy(int maxPower, int transfer) {
        super(ActuallyItems.defaultProps().stacksTo(1));
        this.maxPower = maxPower;
        this.transfer = transfer;
    }
    public ItemEnergy(Properties props, int maxPower, int transfer) {
        super(props);
        this.maxPower = maxPower;
        this.transfer = transfer;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int energy = 0;
        if (stack.hasTag() && stack.getTag().contains("Energy")) {
            energy = stack.getTag().getInt("Energy");
        }
        NumberFormat format = NumberFormat.getInstance();
        tooltip.add(Component.translatable("misc.actuallyadditions.power_long", format.format(energy), format.format(this.maxPower)));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

//    @Override
//    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
//        super.fillItemCategory(group, items);
//        if (!this.allowdedIn(group)) {
//            return;
//        }
//
//        ItemStack charged = new ItemStack(this);
//        charged.getOrCreateTag().putDouble("Energy", this.getMaxEnergyStored(charged));
//        items.add(charged);
//    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Energy")) {
            return (int)((stack.getTag().getDouble("Energy") / this.maxPower) * MAX_BAR_WIDTH);
        }
        return 1;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int defaultColor = super.getBarColor(stack);
        if (FMLEnvironment.dist.isClient()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return defaultColor;
            float[] color = AssetUtil.getWheelColor(mc.player.level().getGameTime() % 256);
            return Mth.color(color[0] / 255F, color[1] / 255F, color[2] / 255F);
        }
        return defaultColor;
    }

    public void setEnergy(ItemStack stack, int energy) {
        stack.getCapability(ForgeCapabilities.ENERGY, null).ifPresent(cap -> {
            if (cap instanceof CustomEnergyStorage) {
                ((CustomEnergyStorage) cap).setEnergyStored(energy);
            }
        });
    }

    @Deprecated
    public int receiveEnergyInternal(ItemStack stack, int maxReceive, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
            .map(cap -> ((CustomEnergyStorage) cap).receiveEnergyInternal(maxReceive, simulate))
            .orElse(0);
    }

    public int extractEnergyInternal(ItemStack stack, int maxExtract, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
            .map(cap -> cap instanceof CustomEnergyStorage
                ? ((CustomEnergyStorage) cap).extractEnergyInternal(maxExtract, simulate)
                : 0)
            .orElse(0);
    }

    @Deprecated
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
            .map(cap -> cap.receiveEnergy(maxReceive, simulate))
            .orElse(0);
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
            .map(cap -> cap.extractEnergy(maxExtract, simulate))
            .orElse(0);
    }

    public int getEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY, null)
            .map(IEnergyStorage::getEnergyStored)
            .orElse(0);
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY, null)
            .map(IEnergyStorage::getMaxEnergyStored)
            .orElse(0);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new EnergyCapabilityProvider(stack, this);
    }

    private static class EnergyCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

        public final CustomEnergyStorage storage;
        private final LazyOptional<CustomEnergyStorage> energyCapability;

        private final ItemStack stack;

        public EnergyCapabilityProvider(ItemStack stack, ItemEnergy item) {
            this.storage = new CustomEnergyStorage(item.maxPower, item.transfer, item.transfer);
            this.energyCapability = LazyOptional.of(() -> this.storage);
            this.stack = stack;
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ENERGY) {
                return this.energyCapability.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            if (this.storage.isDirty())
                stack.getOrCreateTag().putInt("Energy", this.storage.getEnergyStored());
            this.storage.clearDirty();
            return new CompoundTag();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (stack.getOrCreateTag().contains("Energy"))
                this.storage.setEnergyStored(stack.getOrCreateTag().getInt("Energy"));
        }
    }
}
