/*
 * This file ("IDataHandler.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.NetworkEvent;

public interface IDataHandler {

    void handleData(CompoundTag compound, NetworkEvent.Context context);

}
