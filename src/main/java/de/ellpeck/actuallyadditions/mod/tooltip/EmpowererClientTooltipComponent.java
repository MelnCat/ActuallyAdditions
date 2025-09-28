package de.ellpeck.actuallyadditions.mod.tooltip;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class EmpowererClientTooltipComponent implements ClientTooltipComponent, TooltipComponent {
	@Override
	public int getHeight() {
		return 90;
	}

	@Override
	public int getWidth(Font pFont) {
		return 128;
	}

	@Override
	public void renderImage(Font pFont, int pX, int pY, GuiGraphics guiGraphics) {
		var matrix = guiGraphics.pose();
		matrix.pushPose();
		matrix.translate(pX, pY, 0);
		guiGraphics.blit(AssetUtil.getGuiLocation("empowerer_tooltip"), 0, 0, 0, 0, 128, 90);
		matrix.popPose();
	}
}
