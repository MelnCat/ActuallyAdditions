package de.ellpeck.actuallyadditions.mod.items;

import de.ellpeck.actuallyadditions.mod.items.base.ItemBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemTag extends ItemBase {
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag() && pStack.getOrCreateTag().contains("ItemTag")) {
            TagKey<Item> key = TagKey.create(Registries.ITEM, Objects.requireNonNull(ResourceLocation.tryParse(pStack.getOrCreateTag().getString("ItemTag"))));
            pTooltipComponents.add(Component.literal("Tag: " + key.location()).withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.actuallyadditions.item_tag.no_tag").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


/*    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, @Nonnull Player pPlayer, @Nonnull InteractionHand pUsedHand) {
        if (!pLevel.isClientSide)
            pPlayer.openMenu(new SimpleMenuProvider((pId, pInventory, player) -> new ItemTagContainer(pId, pInventory), Component.translatable("container.actuallyadditions.item_tag")));

        return super.use(pLevel, pPlayer, pUsedHand);
    }*/
}
