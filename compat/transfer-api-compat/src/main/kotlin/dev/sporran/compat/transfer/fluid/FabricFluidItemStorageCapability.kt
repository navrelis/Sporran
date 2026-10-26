/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.transfer.fluid

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class FabricFluidItemStorageCapability(storage: Storage<FluidVariant>, val stack: ContainerItemContext) : FabricFluidStorageCapability(storage), IFluidHandlerItem {
    override fun getContainer(): ItemStack {
        return stack.itemVariant.toStack()
    }
}
