/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create

import net.createmod.ponder.PonderClient
import net.fabricmc.loader.api.FabricLoader
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent
import dev.sporran.Sporran
import dev.sporran.api.SporranWrappedModContainerEntrypoint
import dev.sporran.api.compatibility.SporranModCompatBridgeManager

class SporranCreateCompatWrappedNeoContainer : SporranWrappedModContainerEntrypoint {
    override fun onLoadModContainer(container: ModContainer) {
        if ((FabricLoader.getInstance().isModLoaded("ponder") && !Sporran.loader.hasMod("ponder")) || SporranModCompatBridgeManager.isActive("ponder")) {
            container.eventBus!!.addListener { _: FMLLoadCompleteEvent ->
                // Initialize Ponder Fabric later
                PonderClient.modLoadCompleted()
            }
        }
    }
}
