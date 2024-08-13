/*
 * This file ("StringUtil.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Deprecated
public final class StringUtil {

    /**
     * Localizes a given formatted String with the given Replacements
     */
    @OnlyIn(Dist.CLIENT)
    public static String localizeFormatted(String text, Object... replace) {
        return I18n.get(text, replace);
    }

    // TODO: Move to official
    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public static void drawSplitString(Font renderer, String strg, int x, int y, int width, int color, boolean shadow) {
//        ResourcePackList <- holds the correct way
//        List<String> list = renderer.listFormattedStringToWidth(strg, width);
//        for (int i = 0; i < list.size(); i++) {
//            String s1 = list.get(i);
//            renderer.draw(s1, x, y + i * renderer.lineHeight, color, shadow);
//        }
    }

//    @OnlyIn(Dist.CLIENT)
//    public static void renderSplitScaledAsciiString(FontRenderer font, String text, int x, int y, int color, boolean shadow, float scale, int length) {
//        List<String> lines = font.listFormattedStringToWidth(text, (int) (length / scale));
//        for (int i = 0; i < lines.size(); i++) {
//            renderScaledAsciiString(font, lines.get(i), x, y + i * (int) (font.lineHeight * scale + 3), color, shadow, scale);
//        }
//    }

    @OnlyIn(Dist.CLIENT)
    public static void renderScaledString(GuiGraphics guiGraphics, Font font, String text, float x, float y, int color, boolean shadow, float scale) {
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();
        matrices.translate(x, y, 0);
        matrices.scale(scale, scale, 1.0F);
        if (shadow)
            guiGraphics.drawString(font, text, 0, 0, color);
        else
            guiGraphics.drawString(font, text, x, y, color, false);
        matrices.popPose();
    }
}
