package dev.sporran.compat.neoconfig

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents
import net.neoforged.fml.config.ModConfig
import dev.sporran.workarounds.ForgeConfigApiPortCompat

class SporranForgeConfigApiPortCompat : ForgeConfigApiPortCompat {
    override fun fireConfigLoadEvent(modId: String, config: ModConfig?) {
        NeoForgeModConfigEvents.loading(modId).invoker().onModConfigLoading(config)
    }

    override fun fireConfigReloadEvent(modId: String, config: ModConfig?) {
        NeoForgeModConfigEvents.reloading(modId).invoker().onModConfigReloading(config)
    }

    override fun fireConfigUnloadEvent(modId: String, config: ModConfig?) {
        NeoForgeModConfigEvents.unloading(modId).invoker().onModConfigUnloading(config)
    }
}
