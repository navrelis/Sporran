package dev.sporran.workarounds

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import dev.sporran.Sporran

object SporranFluidTags {
    @JvmField val EMPTY: TagKey<Fluid> = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(Sporran.MOD_ID, "empty"))
    @JvmField val EMPTY_NONVANILLA: TagKey<Fluid> = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(Sporran.MOD_ID, "empty_nonvanilla"))

    @JvmStatic
    fun isTagForFluidTypePushing(tag: TagKey<Fluid>): Boolean {
        return tag == EMPTY || tag == EMPTY_NONVANILLA
    }
}
