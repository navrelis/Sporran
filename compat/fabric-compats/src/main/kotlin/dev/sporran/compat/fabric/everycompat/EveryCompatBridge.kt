/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.everycompat

import net.fabricmc.loader.api.FabricLoader
import net.mehvahdjukaar.every_compat.neoforge.EveryCompatForge
import dev.sporran.loader.mod.fabric.WrappedFabricModContainer

object EveryCompatBridge {
    val container = WrappedFabricModContainer.get(FabricLoader.getInstance().getModContainer("everycomp").orElseThrow())
    val forgeInstance = EveryCompatForge(this.container.eventBus)

    fun init() {}
}
