package dev.sporran.compat.fabric.resourcefullib

import net.neoforged.neoforge.registries.DeferredRegister
import dev.sporran.Sporran
import dev.sporran.api.compatibility.ModBridgeStrategy

object SporranResourcefulLibCompat {
    @JvmStatic
    fun <T> attachToModContainer(modId: String, register: DeferredRegister<T>) {
        val container = Sporran.loader.getMod(modId) ?: throw IllegalArgumentException("Could not find NeoForge mod by ID $modId!")
        register.register(container.eventBus)
    }
}

/**
 * Sporran: the Fabric and the NeoForge ResourcefulLib are only both needed when a NeoForge mod uses ResourcefulLib (its
 * registries are routed through FabricResourcefulRegistryMixin, which needs the NeoForge variant's classes). A pack with
 * only the Fabric ResourcefulLib and no NeoForge mod that depends on it works as it is, so it must not be refused.
 */
object ResourcefulLibBridgeStrategy : ModBridgeStrategy.RequireBoth("Due to a workaround used in Sporran, you are required to have both the Fabric and the NeoForge versions of ResourcefulLib installed when a NeoForge mod uses ResourcefulLib.") {
    override fun checkValid(fabricModId: String, neoForgeModId: String) {
        val neoForgeModUsesIt = Sporran.loader.mods.any { mod -> mod.definition.dependencies.any { it.id == neoForgeModId } }
        if (!Sporran.loader.hasMod(neoForgeModId) && !neoForgeModUsesIt)
            return

        super.checkValid(fabricModId, neoForgeModId)
    }
}
