/*
 * This file ("ActuallyAdditions.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod;

import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.blocks.render.ReconstructorRenderer;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderBatteryBox;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderDisplayStand;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderEmpowerer;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderLaserRelay;
import de.ellpeck.actuallyadditions.mod.entity.RenderWorm;
import de.ellpeck.actuallyadditions.mod.event.ClientEvents;
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids;
import de.ellpeck.actuallyadditions.mod.inventory.ActuallyContainers;
import de.ellpeck.actuallyadditions.mod.inventory.gui.CrusherScreen;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiBioReactor;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiBreaker;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiCanolaPress;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiCoalGenerator;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiCoffeeMachine;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiDirectionalBreaker;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiDrill;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiDropper;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiEnergizer;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiEnervator;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFarmer;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFeeder;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFermentingBarrel;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFilter;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFireworkBox;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFluidCollector;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiFurnaceDouble;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiLaserRelayItemWhitelist;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiMiner;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiOilGenerator;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiPhantomPlacer;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiRangedCollector;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiXPSolidifier;
import de.ellpeck.actuallyadditions.mod.inventory.gui.SackGui;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.misc.special.SpecialRenderInit;
import de.ellpeck.actuallyadditions.mod.particle.ActuallyParticles;
import de.ellpeck.actuallyadditions.mod.particle.ParticleBeam;
import de.ellpeck.actuallyadditions.mod.particle.ParticleLaserItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ActuallyAdditionsClient {

    public static void setup(FMLClientSetupEvent event) {
        MenuScreens.register(ActuallyContainers.BAG_CONTAINER.get(), SackGui::new);
        MenuScreens.register(ActuallyContainers.BIO_REACTOR_CONTAINER.get(), GuiBioReactor::new);
        MenuScreens.register(ActuallyContainers.BREAKER_CONTAINER.get(), GuiBreaker::new);
        MenuScreens.register(ActuallyContainers.CANOLA_PRESS_CONTAINER.get(), GuiCanolaPress::new);
        MenuScreens.register(ActuallyContainers.COAL_GENERATOR_CONTAINER.get(), GuiCoalGenerator::new);
        MenuScreens.register(ActuallyContainers.COFFEE_MACHINE_CONTAINER.get(), GuiCoffeeMachine::new);
        MenuScreens.register(ActuallyContainers.DIRECTIONAL_BREAKER_CONTAINER.get(), GuiDirectionalBreaker::new);
        MenuScreens.register(ActuallyContainers.DRILL_CONTAINER.get(), GuiDrill::new);
        MenuScreens.register(ActuallyContainers.DROPPER_CONTAINER.get(), GuiDropper::new);
        MenuScreens.register(ActuallyContainers.ENERVATOR_CONTAINER.get(), GuiEnervator::new);
        MenuScreens.register(ActuallyContainers.ENERGIZER_CONTAINER.get(), GuiEnergizer::new);
        MenuScreens.register(ActuallyContainers.FARMER_CONTAINER.get(), GuiFarmer::new);
        MenuScreens.register(ActuallyContainers.FEEDER_CONTAINER.get(), GuiFeeder::new);
        MenuScreens.register(ActuallyContainers.FERMENTING_BARREL_CONTAINER.get(), GuiFermentingBarrel::new);
        MenuScreens.register(ActuallyContainers.FILTER_CONTAINER.get(), GuiFilter::new);
        MenuScreens.register(ActuallyContainers.FIREWORK_BOX_CONTAINER.get(), GuiFireworkBox::new);
        MenuScreens.register(ActuallyContainers.FLUID_COLLECTOR_CONTAINER.get(), GuiFluidCollector::new);
        MenuScreens.register(ActuallyContainers.FURNACE_DOUBLE_CONTAINER.get(), GuiFurnaceDouble::new);
        MenuScreens.register(ActuallyContainers.GRINDER_CONTAINER.get(), CrusherScreen::new);
        MenuScreens.register(ActuallyContainers.LASER_RELAY_ITEM_WHITELIST_CONTAINER.get(), GuiLaserRelayItemWhitelist::new);
        MenuScreens.register(ActuallyContainers.MINER_CONTAINER.get(), GuiMiner::new);
        MenuScreens.register(ActuallyContainers.OIL_GENERATOR_CONTAINER.get(), GuiOilGenerator::new);
        MenuScreens.register(ActuallyContainers.PHANTOM_PLACER_CONTAINER.get(), GuiPhantomPlacer::new);
        MenuScreens.register(ActuallyContainers.RANGED_COLLECTOR_CONTAINER.get(), GuiRangedCollector::new);
        MenuScreens.register(ActuallyContainers.XPSOLIDIFIER_CONTAINER.get(), GuiXPSolidifier::new);
        // From old proxy
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new SpecialRenderInit());

        event.enqueueWork(() ->
                ItemProperties.register(ActuallyItems.WORM.get(), new ResourceLocation(ActuallyAdditions.MODID, "snail"),
                        (stack, level, entity, tintIndex) -> "snail mail".equalsIgnoreCase(stack.getHoverName().getString()) ? 1F : 0F));

        setupRenderLayers();
    }

    private static void setupRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(InitFluids.CANOLA_OIL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.CANOLA_OIL.getFlowing(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.REFINED_CANOLA_OIL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.REFINED_CANOLA_OIL.getFlowing(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.CRYSTALLIZED_OIL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.CRYSTALLIZED_OIL.getFlowing(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.EMPOWERED_OIL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InitFluids.EMPOWERED_OIL.getFlowing(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ActuallyBlocks.CANOLA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ActuallyBlocks.RICE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ActuallyBlocks.FLAX.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ActuallyBlocks.COFFEE.get(), RenderType.cutout());
    }

    public static void setupSpecialRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getTileEntityType(), ReconstructorRenderer::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.DISPLAY_STAND.getTileEntityType(), RenderDisplayStand::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.EMPOWERER.getTileEntityType(), RenderEmpowerer::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.BATTERY_BOX.getTileEntityType(), RenderBatteryBox::new);

        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY.getTileEntityType(), RenderLaserRelay::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY_ADVANCED.getTileEntityType(), RenderLaserRelay::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY_EXTREME.getTileEntityType(), RenderLaserRelay::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY_ITEM.getTileEntityType(), RenderLaserRelay::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY_ITEM_ADVANCED.getTileEntityType(), RenderLaserRelay::new);
        event.registerBlockEntityRenderer(ActuallyBlocks.LASER_RELAY_FLUIDS.getTileEntityType(), RenderLaserRelay::new);

        event.registerEntityRenderer(ActuallyAdditions.ENTITY_WORM.get(), RenderWorm::new);
    }

    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ActuallyParticles.LASER_ITEM.get(), ParticleLaserItem.Factory::new);
        event.registerSpriteSet(ActuallyParticles.BEAM.get(), ParticleBeam.Factory::new);
    }

    // TODO: [port] validate that this works
    public static void sendBreakPacket(BlockPos pos) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        assert connection != null;
        assert Minecraft.getInstance().hitResult != null;
        connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, ((BlockHitResult) Minecraft.getInstance().hitResult).getDirection()));
    }
}
