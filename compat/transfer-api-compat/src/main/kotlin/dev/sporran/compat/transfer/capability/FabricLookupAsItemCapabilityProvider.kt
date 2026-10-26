/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.transfer.capability

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class FabricLookupAsItemCapabilityProvider<S, T, C>(
    item: Item, lookup: ItemApiLookup<S, C>,

    val context: (ItemStack) -> C,
    val isSelfStorage: (S) -> Boolean,
    val capability: (S, C) -> T,
) : ICapabilityProvider<ItemStack, Void?, T> {
    val provider = lookup.getProvider(item)

    override fun getCapability(stack: ItemStack, context: Void?): T? {
        val ctx = this.context.invoke(stack)
        val storage = this.provider?.find(stack, ctx)
            ?: return null

        // Ignore our own storage
        if (this.isSelfStorage.invoke(storage))
            return null

        return this.capability.invoke(storage, ctx)
    }
}
