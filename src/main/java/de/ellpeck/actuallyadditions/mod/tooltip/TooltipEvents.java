package de.ellpeck.actuallyadditions.mod.tooltip;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipEvents {

	@SubscribeEvent
	public void onTooltipRegistration(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(EmpowererClientTooltipComponent.class, x -> x);
	}

}
