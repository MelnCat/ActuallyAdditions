/*
 * This file ("AssetUtil.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderTypes;
import de.ellpeck.actuallyadditions.mod.network.PacketHandler;
import de.ellpeck.actuallyadditions.mod.network.PacketServerToClient;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityAtomicReconstructor;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Matrix4f;

public final class AssetUtil {

    public static final int MAX_LIGHT_X = 0xF000F0;
    public static final int MAX_LIGHT_Y = 0xF000F0;

    public static final ResourceLocation GUI_INVENTORY_LOCATION = getGuiLocation("gui_inventory");
    private static final ResourceLocation FORGE_WHITE = new ResourceLocation("forge", "white");

    public static ResourceLocation getGuiLocation(String file) {
        return new ResourceLocation(ActuallyAdditions.MODID, "textures/gui/" + file + ".png");
    }

    public static ResourceLocation getBookletGuiLocation(String file) {
        return getGuiLocation("booklet/" + file);
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayNameString(GuiGraphics guiGraphics, Font font, int xSize, int yPositionOfMachineText, String text) {
        guiGraphics.drawString(font, text, xSize / 2f - font.width(text) / 2f, yPositionOfMachineText, 0xFFFFFF, false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayNameString(GuiGraphics guiGraphics, Font font, int xSize, int yPositionOfMachineText, TileEntityBase tile) {
        displayNameString(guiGraphics, font, xSize, yPositionOfMachineText, I18n.get(tile.getNameForTranslation()));
    }

    //    public static void renderBlockInWorld(Block block, int meta) {
    //        renderItemInWorld(new ItemStack(block, 1, meta), combinedLightIn, combinedOverlayIn, matrices, buffer);
    //    }

    @OnlyIn(Dist.CLIENT)
    public static void renderItemInWorld(ItemStack stack, int combinedLight, int combinedOverlay, PoseStack matrices, MultiBufferSource buffer) {
        if (!stack.isEmpty()) {
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, matrices, buffer, null, 0
            );
        }
    }

    //    @OnlyIn(Dist.CLIENT)
    //    public static void renderStateInWorld(BlockState state, IWorldReader world, BlockPos pos, float brightness) {
    //        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    //        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
    //        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
    //        int i = Minecraft.getInstance().getBlockColors().colorMultiplier(state, world, pos, 0);
    //
    //        float r = (i >> 16 & 255) / 255F;
    //        float g = (i >> 8 & 255) / 255F;
    //        float b = (i & 255) / 255F;
    //
    //        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(state, model, brightness, r, g, b);
    //    }

    @OnlyIn(Dist.CLIENT)
    public static void renderItemWithoutScrewingWithColors(ItemStack stack, PoseStack matrices, int combinedOverlay, int combinedLight) {
        if (StackUtil.isValid(stack)) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer renderer = mc.getItemRenderer();
            TextureManager manager = mc.getTextureManager();
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

            BakedModel model = renderer.getModel(stack, null, null, 0);
            manager.bindForSetup(TextureAtlas.LOCATION_BLOCKS); //bind
            manager.getTexture(TextureAtlas.LOCATION_BLOCKS).setBlurMipmap(false, false);
//            RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
//            RenderSystem.pushMatrix();
            matrices.pushPose();
            model = ForgeHooksClient.handleCameraTransforms(matrices, model, ItemDisplayContext.FIXED, false);
            renderer.render(stack, ItemDisplayContext.FIXED, false, matrices, bufferSource,
                    combinedOverlay, combinedLight, model);
//            RenderSystem.popMatrix();
            matrices.popPose();
//            RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
            manager.bindForSetup(TextureAtlas.LOCATION_BLOCKS); //bind
            manager.getTexture(TextureAtlas.LOCATION_BLOCKS).restoreLastBlurMipmap();
            bufferSource.endBatch();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderStackToGui(ItemStack stack, int x, int y, float scale) {
/*        GlStateManager._pushMatrix();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager._enableRescaleNormal();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);

        Minecraft mc = Minecraft.getInstance();
        boolean flagBefore = mc.font.getUnicodeFlag();
        mc.font.setUnicodeFlag(false);
        Minecraft.getInstance().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Minecraft.getInstance().getRenderItem().renderItemOverlayIntoGUI(mc.font, stack, 0, 0, null);
        mc.font.setUnicodeFlag(flagBefore);

        RenderHelper.turnOff();
        GlStateManager._popMatrix();*/
    }

    //Copied from Gui.class and changed
    @OnlyIn(Dist.CLIENT)
    public static void drawHorizontalGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel) {
/*        float f = (startColor >> 24 & 255) / 255.0F;
        float f1 = (startColor >> 16 & 255) / 255.0F;
        float f2 = (startColor >> 8 & 255) / 255.0F;
        float f3 = (startColor & 255) / 255.0F;
        float f4 = (endColor >> 24 & 255) / 255.0F;
        float f5 = (endColor >> 16 & 255) / 255.0F;
        float f6 = (endColor >> 8 & 255) / 255.0F;
        float f7 = (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager._enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager._shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuilder();
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.vertex(left, top, zLevel).color(f1, f2, f3, f).endVertex();
        renderer.vertex(left, bottom, zLevel).color(f1, f2, f3, f).endVertex();
        renderer.vertex(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        renderer.vertex(right, top, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.end();
        GlStateManager._shadeModel(7424);
        GlStateManager._disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();*/
    }

//    @OnlyIn(Dist.CLIENT)
//    public static void renderNameTag(String tag, double x, double y, double z) {
//        FontRenderer fontrenderer = Minecraft.getInstance().font;
//        float f = 1.6F;
//        float f1 = 0.016666668F * f;
//        GlStateManager._pushMatrix();
//        GlStateManager.translate(x, y, z);
//        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(-Minecraft.getInstance().getEntityRenderDispatcher().playerViewY, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(Minecraft.getInstance().getEntityRenderDispatcher().playerViewX, 1.0F, 0.0F, 0.0F);
//        GlStateManager.scale(-f1, -f1, f1);
//        GlStateManager._disableLighting();
//        GlStateManager._depthMask(false);
//        GlStateManager.disableDepth();
//        GlStateManager._enableBlend();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder renderer = tessellator.getBuilder();
//        int i = 0;
//        int j = fontrenderer.width(tag) / 2;
//        GlStateManager.disableTexture2D();
//        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//        renderer.vertex(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//        renderer.vertex(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//        renderer.vertex(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//        renderer.vertex(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//        tessellator.end();
//        GlStateManager.enableTexture2D();
//        fontrenderer.draw(tag, -fontrenderer.width(tag) / 2, i, 553648127);
//        GlStateManager.enableDepth();
//        GlStateManager._depthMask(true);
//        fontrenderer.draw(tag, -fontrenderer.width(tag) / 2, i, -1);
//        GlStateManager._enableLighting();
//        GlStateManager._disableBlend();
//        GlStateManager.color3arg(1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager._popMatrix();
//    }

    public static void spawnLaserWithTimeServer(Level world, double startX, double startY, double startZ, double endX, double endY, double endZ, int color, int maxAge, double rotationTime, float size, float alpha) {
        if (!world.isClientSide) {
            CompoundTag data = new CompoundTag();
            data.putDouble("StartX", startX);
            data.putDouble("StartY", startY);
            data.putDouble("StartZ", startZ);
            data.putDouble("EndX", endX);
            data.putDouble("EndY", endY);
            data.putDouble("EndZ", endZ);
            data.putInt("Color", color);
            data.putDouble("RotationTime", rotationTime);
            data.putFloat("Size", size);
            data.putInt("MaxAge", maxAge);
            data.putFloat("Alpha", alpha);
            PacketHandler.THE_NETWORK.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(startX, startY, startZ, 96, world.dimension())), new PacketServerToClient(data, PacketHandler.LASER_HANDLER));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnLaserWithTimeClient(double startX, double startY, double startZ, double endX, double endY, double endZ, int color, int maxAge, double rotationTime, float size, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        BlockEntity tile = mc.level.getBlockEntity(BlockPos.containing(startX, startY, startZ));
        if(tile instanceof TileEntityAtomicReconstructor)
            ((TileEntityAtomicReconstructor) tile).resetBeam(maxAge, color);


/*        if (mc.player.distanceToSqr(startX, startY, startZ) <= 64 || mc.player.distanceToSqr(endX, endY, endZ) <= 64) {
            Particle fx = new ParticleBeam(mc.level, startX, startY, startZ, endX, endY, endZ, color, maxAge, rotationTime, size, alpha);
            mc.level.addParticle((IParticleData) fx, startX, startY, startZ, 0, 0, 0);
        }*/
    }
/*    @OnlyIn(Dist.CLIENT)
    public static void renderLaser(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y, float z, float tx, float ty, float tz, float rotation, int color, float beamWidth) {


    }*/

    @OnlyIn(Dist.CLIENT)
    public static void renderLaser(PoseStack matrixStack, MultiBufferSource buffer, float offX, float offY, float offZ, float yaw, float pitch, float length, float rotationTime, int color, float alpha, float beamWidth) {
        Level world = Minecraft.getInstance().level;
/*        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (int) (alpha * 255);*/
        int r = (int)(((color >> 16) & 0xFF) * alpha);
        int g = (int)(((color >> 8) & 0xFF) * alpha);
        int b = (int)((color & 0xFF) * alpha);
        int a = 255;

        int lightmap = LightTexture.pack(MAX_LIGHT_X, MAX_LIGHT_Y);

        float roll = rotationTime > 0.0f ? 360.0f * (world.getGameTime() % rotationTime / rotationTime) : 0.0f;

        VertexConsumer builder = buffer.getBuffer(RenderTypes.LASER);
        matrixStack.pushPose();
        matrixStack.translate(0.5f, 0.5f, 0.5f);
        matrixStack.translate(offX, offY, offZ);

        matrixStack.mulPose(Axis.YP.rotationDegrees(yaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(pitch));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(roll));

        Matrix4f matrix = matrixStack.last().pose();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(FORGE_WHITE);
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        //Draw laser tube faces
        for (int i = 1; i < 4; i++) {
            float width = beamWidth * (i / 4.0f);
            //top
            builder.vertex(matrix, -width,  width,    0.0f).color(r, g, b, a).uv(minU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width,  width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width,  width, -length).color(r, g, b, a).uv(maxU, minV).uv2(lightmap).endVertex();
            builder.vertex(matrix, -width,  width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
            //bottom
            builder.vertex(matrix, -width, -width,    0.0f).color(r, g, b, a).uv(minU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix, -width, -width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width, -width, -length).color(r, g, b, a).uv(maxU, minV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width, -width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            //left
            builder.vertex(matrix, -width,  width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix, -width, -width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix, -width, -width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
            builder.vertex(matrix, -width,  width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
            //right
            builder.vertex(matrix,  width,  width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width, -width,    0.0f).color(r, g, b, a).uv(maxU, maxV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width, -width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
            builder.vertex(matrix,  width,  width, -length).color(r, g, b, a).uv(minU, minV).uv2(lightmap).endVertex();
        }


        matrixStack.popPose();
    }

    public static void renderLaser(PoseStack matrixStack, MultiBufferSource buffer, Vec3 startOffset, Vec3 endOffset, float rotationTime, int color, float alpha, float beamWidth) {
        Vec3 combined = endOffset.subtract(startOffset);

        double pitch = Math.toDegrees(Math.atan2(combined.y, Math.sqrt(combined.x * combined.x + combined.z * combined.z)));
        double yaw = Math.toDegrees(Math.atan2(-combined.z, combined.x));
        double length = combined.length();

        renderLaser(matrixStack, buffer, (float) startOffset.x, (float) startOffset.y, (float) startOffset.z, (float) yaw, (float) pitch, (float) length, rotationTime, color, alpha, beamWidth);
    }

    //Thanks to feldim2425 for this.
    //I can't do rendering code. Ever.
    @OnlyIn(Dist.CLIENT)
    public static void renderLaser(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ, double rotationTime, float alpha, double beamWidth, float[] color) {
        Tesselator tessy = Tesselator.getInstance();
        BufferBuilder render = tessy.getBuilder();
        Level world = Minecraft.getInstance().level;

        float r = color[0];
        float g = color[1];
        float b = color[2];

        Vec3 vec1 = new Vec3(firstX, firstY, firstZ);
        Vec3 vec2 = new Vec3(secondX, secondY, secondZ);
        Vec3 combinedVec = vec2.subtract(vec1);

        double rot = rotationTime > 0
                ? 360D * (world.getGameTime() % rotationTime / rotationTime)
                : 0;
        double pitch = Math.atan2(combinedVec.y, Math.sqrt(combinedVec.x * combinedVec.x + combinedVec.z * combinedVec.z));
        double yaw = Math.atan2(-combinedVec.z, combinedVec.x);

        double length = combinedVec.length();

//        GlStateManager._pushMatrix();

/*        GlStateManager._disableLighting();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
        float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
        GlStateManager._alphaFunc(GL11.GL_ALWAYS, 0);
        GlStateManager.translate(firstX - TileEntityRendererDispatcher.staticPlayerX, firstY - TileEntityRendererDispatcher.staticPlayerY, firstZ - TileEntityRendererDispatcher.staticPlayerZ);
        GlStateManager.rotate((float) (180 * yaw / Math.PI), 0, 1, 0);
        GlStateManager.rotate((float) (180 * pitch / Math.PI), 0, 0, 1);
        GlStateManager.rotate((float) rot, 1, 0, 0);*/

        /*if(r != r2 || g != g2 || b != b2){
            render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            Minecraft.getInstance().renderEngine.bindTexture(ClientUtil.LIGHT_BEAM_GRADIENT);

            render.pos(length, -beamWidth, beamWidth).tex(0, 0).color(r, g, b, alpha).endVertex();
            render.pos(length, beamWidth, beamWidth).tex(0, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, beamWidth, beamWidth).tex(1, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, -beamWidth, beamWidth).tex(1, 0).color(r, g, b, alpha).endVertex();

            render.pos(length, -beamWidth, beamWidth).tex(1, 0).color(r2, g2, b2, alpha).endVertex();
            render.pos(length, beamWidth, beamWidth).tex(1, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, beamWidth, beamWidth).tex(0, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, -beamWidth, beamWidth).tex(0, 0).color(r2, g2, b2, alpha).endVertex();

            render.pos(length, beamWidth, -beamWidth).tex(0, 0).color(r, g, b, alpha).endVertex();
            render.pos(length, -beamWidth, -beamWidth).tex(0, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, -beamWidth, -beamWidth).tex(1, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, beamWidth, -beamWidth).tex(1, 0).color(r, g, b, alpha).endVertex();

            render.pos(length, beamWidth, -beamWidth).tex(1, 0).color(r2, g2, b2, alpha).endVertex();
            render.pos(length, -beamWidth, -beamWidth).tex(1, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, -beamWidth, -beamWidth).tex(0, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, beamWidth, -beamWidth).tex(0, 0).color(r2, g2, b2, alpha).endVertex();

            render.pos(length, beamWidth, beamWidth).tex(0, 0).color(r, g, b, alpha).endVertex();
            render.pos(length, beamWidth, -beamWidth).tex(0, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, beamWidth, -beamWidth).tex(1, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, beamWidth, beamWidth).tex(1, 0).color(r, g, b, alpha).endVertex();

            render.pos(length, beamWidth, beamWidth).tex(1, 0).color(r2, g2, b2, alpha).endVertex();
            render.pos(length, beamWidth, -beamWidth).tex(1, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, beamWidth, -beamWidth).tex(0, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, beamWidth, beamWidth).tex(0, 0).color(r2, g2, b2, alpha).endVertex();

            render.pos(length, -beamWidth, -beamWidth).tex(0, 0).color(r, g, b, alpha).endVertex();
            render.pos(length, -beamWidth, beamWidth).tex(0, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, -beamWidth, beamWidth).tex(1, 1).color(r, g, b, alpha).endVertex();
            render.pos(0, -beamWidth, -beamWidth).tex(1, 0).color(r, g, b, alpha).endVertex();

            render.pos(length, -beamWidth, -beamWidth).tex(1, 0).color(r2, g2, b2, alpha).endVertex();
            render.pos(length, -beamWidth, beamWidth).tex(1, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, -beamWidth, beamWidth).tex(0, 1).color(r2, g2, b2, alpha).endVertex();
            render.pos(0, -beamWidth, -beamWidth).tex(0, 0).color(r2, g2, b2, alpha).endVertex();
            tessy.draw();
        }
        else{*/
        //GlStateManager.disableTexture2D();
        //render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        for (double i = 0; i < 4; i++) {
            double width = beamWidth * (i / 4.0);
            render.vertex(length, width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, -width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(length, -width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.vertex(length, -width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, -width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(length, width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.vertex(length, width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(length, width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            render.vertex(length, -width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, -width, width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(0, -width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            render.vertex(length, -width, -width).uv(0, 0).uv2(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
        }
        tessy.end();

        //GlStateManager.enableTexture2D();
        //}

        //GlStateManager._alphaFunc(func, ref);
        //GlStateManager._blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager._disableBlend();
//        GlStateManager._enableLighting();
//        GlStateManager._popMatrix();
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderTextInWorld(GuiGraphics guiGraphics, double offsetX, double offsetY, double offsetZ, NonNullList<String> text, int color) {
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();
        matrices.translate(offsetX,offsetY,offsetZ);
        matrices.scale(-1, -1, 1);
        matrices.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().cameraEntity.getYRot()));
        matrices.scale(0.01F, 0.01F, 0.01F);

        Font font = Minecraft.getInstance().font;

        int y = 0;
        for (String s : text) {
            guiGraphics.drawString(font, s, 0, y, color);
            y+= 10;
        }

        matrices.popPose();
    }

    public static float[] getWheelColor(float pos) {
        if (pos < 85.0f) {
            return new float[]{pos * 3.0F, 255.0f - pos * 3.0f, 0.0f};
        }
        if (pos < 170.0f) {
            return new float[]{255.0f - (pos -= 85.0f) * 3.0f, 0.0f, pos * 3.0f};
        }
        return new float[]{0.0f, (pos -= 170.0f) * 3.0f, 255.0f - pos * 3.0f};
    }
}
