package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ActuallyTiles {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ActuallyAdditions.MODID);

    //    public static final RegistryObject<,<?>> COMPOST_TILE = TILES.register("compost", () -> TileEntityType.Builder.create(TileEntityCompost::new, InitBlocks.blockCompost.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityFeeder>> FEEDER_TILE = TILES.register("feeder", () -> TileEntityType.Builder.create(TileEntityFeeder::new, ActuallyBlocks.FEEDER.get()).build(null));
    //    public static final RegistryObject<build<?>> GIANTCHEST_TILE = TILES.register("", () -> TileEntityType.Builder.create(TileEntityGiantChest::new, ).build(null));
    //    public static final RegistryObject<build<?>> GIANTCHESTMEDIUM_TILE = TILES.register("", () -> TileEntityType.Builder.create(TileEntityGiantChestMedium::new, ).build(null));
    //    public static final RegistryObject<build<?>> GIANTCHESTLARGE_TILE = TILES.register("", () -> TileEntityType.Builder.create(TileEntityGiantChestLarge::new, ).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityCrusher>> GRINDER_TILE = TILES.register("grinder", () -> TileEntityType.Builder.create(TileEntityCrusher::new, ActuallyBlocks.GRINDER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityFurnaceDouble>> FURNACE_DOUBLE_TILE = TILES.register("furnaceDouble", () -> TileEntityType.Builder.create(TileEntityFurnaceDouble::new, ActuallyBlocks.FURNACE_DOUBLE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityInputter>> INPUTTER_TILE = TILES.register("inputter", () -> TileEntityType.Builder.create(TileEntityInputter::new, ActuallyBlocks.INPUTTER.get()).build(null));
    //    public static final RegistryObject<TileEntityType<TileEntityFurnaceSolar>> SOLAR_TILE = TILES.register("solarPanel", () -> TileEntityType.Builder.create(TileEntityFurnaceSolar::new, ActuallyBlocks.blockFurnaceSolar.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityHeatCollector>> HEATCOLLECTOR_TILE = TILES.register("heatCollector", () -> TileEntityType.Builder.create(TileEntityHeatCollector::new, ActuallyBlocks.HEAT_COLLECTOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityBreaker>> BREAKER_TILE = TILES.register("breaker", () -> TileEntityType.Builder.create(TileEntityBreaker::new, ActuallyBlocks.BREAKER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityDropper>> DROPPER_TILE = TILES.register("dropper", () -> TileEntityType.Builder.create(TileEntityDropper::new, ActuallyBlocks.DROPPER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityInputterAdvanced>> INPUTTERADVANCED_TILE = TILES.register("inputterAdvanced", () -> TileEntityType.Builder.create(TileEntityInputterAdvanced::new, ActuallyBlocks.INPUTTER_ADVANCED.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPlacer>> PLACER_TILE = TILES.register("placer", () -> TileEntityType.Builder.create(TileEntityPlacer::new, ActuallyBlocks.PLACER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityCrusherDouble>> GRINDER_DOUBLE_TILE = TILES.register("grinderDouble", () -> TileEntityType.Builder.create(TileEntityCrusherDouble::new, ActuallyBlocks.GRINDER_DOUBLE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityCanolaPress>> CANOLAPRESS_TILE = TILES.register("canolaPress", () -> TileEntityType.Builder.create(TileEntityCanolaPress::new, ActuallyBlocks.CANOLA_PRESS.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFermentingBarrel>> FERMENTINGBARREL_TILE = TILES.register("fermentingBarrel", () -> TileEntityType.Builder.create(TileEntityFermentingBarrel::new, ActuallyBlocks.FERMENTING_BARREL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityOilGenerator>> OILGENERATOR_TILE = TILES.register("oilGenerator", () -> TileEntityType.Builder.create(TileEntityOilGenerator::new, ActuallyBlocks.OIL_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCoalGenerator>> COALGENERATOR_TILE = TILES.register("coalGenerator", () -> TileEntityType.Builder.create(TileEntityCoalGenerator::new, ActuallyBlocks.COAL_GENERATOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomItemface>> PHANTOMITEMFACE_TILE = TILES.register("phantomface", () -> TileEntityType.Builder.create(TileEntityPhantomItemface::new, ActuallyBlocks.PHANTOMFACE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomLiquiface>> PHANTOMLIQUIFACE_TILE = TILES.register("liquiface", () -> TileEntityType.Builder.create(TileEntityPhantomLiquiface::new, ActuallyBlocks.PHANTOM_LIQUIFACE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomEnergyface>> PHANTOMENERGYFACE_TILE = TILES.register("energyface", () -> TileEntityType.Builder.create(TileEntityPhantomEnergyface::new, ActuallyBlocks.PHANTOM_ENERGYFACE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPlayerInterface>> PLAYERINTERFACE_TILE = TILES.register("playerInterface", () -> TileEntityType.Builder.create(TileEntityPlayerInterface::new, ActuallyBlocks.PLAYER_INTERFACE.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomPlacer>> PHANTOMPLACER_TILE = TILES.register("phantomPlacer", () -> TileEntityType.Builder.create(TileEntityPhantomPlacer::new, ActuallyBlocks.PHANTOM_PLACER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomBreaker>> PHANTOMBREAKER_TILE = TILES.register("phantomBreaker", () -> TileEntityType.Builder.create(TileEntityPhantomBreaker::new, ActuallyBlocks.PHANTOM_BREAKER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityFluidCollector>> FLUIDCOLLECTOR_TILE = TILES.register("fluidCollector", () -> TileEntityType.Builder.create(TileEntityFluidCollector::new, ActuallyBlocks.FLUID_COLLECTOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityFluidPlacer>> FLUIDPLACER_TILE = TILES.register("fluidPlacer", () -> TileEntityType.Builder.create(TileEntityFluidPlacer::new, ActuallyBlocks.FLUID_PLACER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityLavaFactoryController>> LAVAFACTORYCONTROLLER_TILE = TILES.register("lavaFactory", () -> TileEntityType.Builder.create(TileEntityLavaFactoryController::new, ActuallyBlocks.LAVA_FACTORY_CONTROLLER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityCoffeeMachine>> COFFEEMACHINE_TILE = TILES.register("coffeeMachine", () -> TileEntityType.Builder.create(TileEntityCoffeeMachine::new, ActuallyBlocks.COFFEE_MACHINE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityPhantomBooster>> PHANTOM_BOOSTER_TILE = TILES.register("phantomBooster", () -> TileEntityType.Builder.create(TileEntityPhantomBooster::new, ActuallyBlocks.PHANTOM_BOOSTER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityEnergizer>> ENERGIZER_TILE = TILES.register("energizer", () -> TileEntityType.Builder.create(TileEntityEnergizer::new, ActuallyBlocks.ENERGIZER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityEnervator>> ENERVATOR_TILE = TILES.register("enervator", () -> TileEntityType.Builder.create(TileEntityEnervator::new, ActuallyBlocks.ENERVATOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityXPSolidifier>> XPSOLIDIFIER_TILE = TILES.register("xpSolidifier", () -> TileEntityType.Builder.create(TileEntityXPSolidifier::new, ActuallyBlocks.XP_SOLIDIFIER.get()).build(null));
    //    public static final RegistryObject<.<?>> SMILEYCLOUD_TILE = TILES.register("", () -> TileEntityType.Builder.create(TileEntitySmileyCloud::new, InitBlocks.blockSmileyCloud.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityLeafGenerator>> LEAFGENERATOR_TILE = TILES.register("leafGenerator", () -> TileEntityType.Builder.create(TileEntityLeafGenerator::new, ActuallyBlocks.LEAF_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityDirectionalBreaker>> DIRECTIONALBREAKER_TILE = TILES.register("directionalBreaker", () -> TileEntityType.Builder.create(TileEntityDirectionalBreaker::new, ActuallyBlocks.DIRECTIONAL_BREAKER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityRangedCollector>> RANGEDCOLLECTOR_TILE = TILES.register("rangedCollector", () -> TileEntityType.Builder.create(TileEntityRangedCollector::new, ActuallyBlocks.RANGED_COLLECTOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityAtomicReconstructor>> ATOMICRECONSTRUCTOR_TILE = TILES.register("reconstructor", () -> TileEntityType.Builder.create(TileEntityAtomicReconstructor::new, ActuallyBlocks.ATOMIC_RECONSTRUCTOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityMiner>> MINER_TILE = TILES.register("miner", () -> TileEntityType.Builder.create(TileEntityMiner::new, ActuallyBlocks.MINER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityFireworkBox>> FIREWORKBOX_TILE = TILES.register("fireworkBox", () -> TileEntityType.Builder.create(TileEntityFireworkBox::new, ActuallyBlocks.FIREWORK_BOX.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityPhantomRedstoneface>> PHANTOMREDSTONEFACE_TILE = TILES.register("redstoneface", () -> TileEntityType.Builder.create(TileEntityPhantomRedstoneface::new, ActuallyBlocks.PHANTOM_REDSTONEFACE.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayItem>> LASERRELAYITEM_TILE = TILES.register("laserRelayItem", () -> TileEntityType.Builder.create(TileEntityLaserRelayItem::new, ActuallyBlocks.LASER_RELAY_ITEM.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayEnergy>> LASERRELAYENERGY_TILE = TILES.register("laserRelay", () -> TileEntityType.Builder.create(TileEntityLaserRelayEnergy::new, ActuallyBlocks.LASER_RELAY.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayEnergyAdvanced>> LASERRELAYENERGYADVANCED_TILE = TILES.register("laserRelayAdvanced", () -> TileEntityType.Builder.create(TileEntityLaserRelayEnergyAdvanced::new, ActuallyBlocks.LASER_RELAY_ADVANCED.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayEnergyExtreme>> LASERRELAYENERGYEXTREME_TILE = TILES.register("laserRelayExtreme", () -> TileEntityType.Builder.create(TileEntityLaserRelayEnergyExtreme::new, ActuallyBlocks.LASER_RELAY_EXTREME.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayItemWhitelist>> LASERRELAYITEMWHITELIST_TILE = TILES.register("laserRelayItemWhitelist", () -> TileEntityType.Builder.create(TileEntityLaserRelayItemWhitelist::new, ActuallyBlocks.LASER_RELAY_ITEM_WHITELIST.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityItemInterface>> ITEMVIEWER_TILE = TILES.register("itemViewer", () -> TileEntityType.Builder.create(TileEntityItemInterface::new, ActuallyBlocks.ITEM_VIEWER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityDisplayStand>> DISPLAYSTAND_TILE = TILES.register("displayStand", () -> TileEntityType.Builder.create(TileEntityDisplayStand::new, ActuallyBlocks.DISPLAY_STAND.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityShockSuppressor>> SHOCKSUPPRESSOR_TILE = TILES.register("shockSuppressor", () -> TileEntityType.Builder.create(TileEntityShockSuppressor::new, ActuallyBlocks.SHOCK_SUPPRESSOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityEmpowerer>> EMPOWERER_TILE = TILES.register("empowerer", () -> TileEntityType.Builder.create(TileEntityEmpowerer::new, ActuallyBlocks.EMPOWERER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityLaserRelayFluids>> LASERRELAYFLUIDS_TILE = TILES.register("laserRelayFluids", () -> TileEntityType.Builder.create(TileEntityLaserRelayFluids::new, ActuallyBlocks.LASER_RELAY_FLUIDS.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityBioReactor>> BIOREACTOR_TILE = TILES.register("bioReactor", () -> TileEntityType.Builder.create(TileEntityBioReactor::new, ActuallyBlocks.BIOREACTOR.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityFarmer>> FARMER_TILE = TILES.register("farmer", () -> TileEntityType.Builder.create(TileEntityFarmer::new, ActuallyBlocks.FARMER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityItemInterfaceHopping>> ITEMVIEWERHOPPING_TILE = TILES.register("itemViewerHopping", () -> TileEntityType.Builder.create(TileEntityItemInterfaceHopping::new, ActuallyBlocks.ITEM_VIEWER.get()).build(null));
    //public static final RegistryObject<TileEntityType<TileEntityBatteryBox>> BATTERYBOX_TILE = TILES.register("batteryBox", () -> TileEntityType.Builder.create(TileEntityBatteryBox::new, ActuallyBlocks.BATTERY_BOX.get()).build(null));
}
