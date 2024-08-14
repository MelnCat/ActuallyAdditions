/*
 * This file ("BannerHelper.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.misc;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class BannerHelper {
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, ActuallyAdditions.MODID);

    public static RegistryObject<BannerPattern> DRILL = BANNER_PATTERNS.register("drill", () -> new BannerPattern(ActuallyAdditions.MODID + ":drill"));
    public static RegistryObject<BannerPattern> LEAF_BLO = BANNER_PATTERNS.register("leaf_blo", () -> new BannerPattern(ActuallyAdditions.MODID + ":leaf_blo"));
    public static RegistryObject<BannerPattern> PHAN_CON = BANNER_PATTERNS.register("phan_con", () -> new BannerPattern(ActuallyAdditions.MODID + ":phan_con"));
    public static RegistryObject<BannerPattern> BOOK = BANNER_PATTERNS.register("book", () -> new BannerPattern(ActuallyAdditions.MODID + ":book"));

    public static void init(IEventBus eventBus) {
        BANNER_PATTERNS.register(eventBus);
    }
}
