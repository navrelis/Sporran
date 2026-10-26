/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.javafmlmod.FMLModContainer
import dev.sporran.loader.mod.NeoForgeMod

open class SporranModContainer(internal val mod: NeoForgeMod) : FMLModContainer(mod, emptyList(), mod.scanData, ModuleLayer.empty()) {
    override fun getEventBus(): IEventBus {
        return mod.eventBus
    }
}
