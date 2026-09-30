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
