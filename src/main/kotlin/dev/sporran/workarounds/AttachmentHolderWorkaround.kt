/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.workarounds

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

interface AttachmentHolderWorkaround {
    fun deserializeAttachments(provider: HolderLookup.Provider, tag: CompoundTag)
    fun serializeAttachments(provider: HolderLookup.Provider): CompoundTag?
}