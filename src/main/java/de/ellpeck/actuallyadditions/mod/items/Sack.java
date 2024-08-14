/*
 * This file ("ItemBag.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items;

import de.ellpeck.actuallyadditions.mod.inventory.SackContainer;
import de.ellpeck.actuallyadditions.mod.inventory.VoidSackContainer;
import de.ellpeck.actuallyadditions.mod.items.base.ItemBase;
import de.ellpeck.actuallyadditions.mod.sack.SackData;
import de.ellpeck.actuallyadditions.mod.sack.SackManager;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.UUID;

public class Sack extends ItemBase {
    public final boolean isVoid;

    public Sack(boolean isVoid) {
        super(ActuallyItems.defaultProps().stacksTo(1));
        this.isVoid = isVoid;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        if (!this.isVoid) {
            BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
            if (tile != null) {
                if (!context.getLevel().isClientSide) {
                    ItemStackHandlerAA inv = new ItemStackHandlerAA(28);

                    boolean changed = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, context.getClickedFace())
                        .map(cap -> {
                            boolean localChanged = false;
                            DrillItem.loadSlotsFromNBT(inv, stack);

                            for (int j = 0; j < inv.getSlots(); j++) {
                                ItemStack invStack = inv.getStackInSlot(j);
                                if (!invStack.isEmpty()) {
                                    for (int i = 0; i < cap.getSlots(); i++) {
                                        ItemStack remain = cap.insertItem(i, invStack, false);
                                        if (!ItemStack.matches(remain, invStack)) {
                                            inv.setStackInSlot(j, remain.copy());
                                            localChanged = true;
                                            if (remain.isEmpty()) {
                                                break;
                                            }
                                            invStack = remain;
                                        }
                                    }
                                }
                            }

                            return localChanged;
                        }).orElse(false);

                    if (changed) {
                        DrillItem.writeSlotsToNBT(inv, stack);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack sackStack = player.getItemInHand(hand);
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND && sackStack.getItem() instanceof Sack && player instanceof ServerPlayer) {

            if (!isVoid) {
                SackData data = getData(sackStack);
                if (data == null)
                    return InteractionResultHolder.fail(sackStack);

                UUID uuid = data.getUuid();

                data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());


                NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider((id, inv, entity) ->
                        new SackContainer(id, inv, data.getSpecialHandler(), data.getFilter()), sackStack.getHoverName()), (buffer -> buffer.writeUUID(uuid)));
            } else
                player.openMenu(new SimpleMenuProvider((id, inv, entity) -> new VoidSackContainer(id, inv), sackStack.getHoverName()));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public static SackData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof Sack))
            return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("UUID")) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        } else
            uuid = tag.getUUID("UUID");
        return SackManager.get().getOrCreateSack(uuid);
    }
}
