package de.ellpeck.actuallyadditions.mod;

import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ActuallyTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ActuallyAdditions.MODID);

	public static final Supplier<CreativeModeTab> GROUP = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder() // TODO coherently order them :P
			.icon(() -> ActuallyItems.ITEM_BOOKLET.get().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.actuallyadditions"))
			.displayItems((parameters, output) -> {
				List<ItemStack> stacks = ActuallyItems.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				stacks.forEach(stack -> {
					if(stack.getItem() instanceof ItemEnergy itemEnergy) {
						stack.getOrCreateTag().putDouble("Energy", itemEnergy.getMaxEnergyStored(stack));
						stack.getOrCreateTag().putBoolean("Charged", true);
					}
				});
				output.acceptAll(stacks);
			}).build());

	public static void init(IEventBus evt) {
		CREATIVE_MODE_TABS.register(evt);
	}
}
