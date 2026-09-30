package dev.sporran.loader

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.javafmlmod.FMLModContainer
import dev.sporran.loader.mod.NeoForgeMod

open class SporranModContainer(internal val mod: NeoForgeMod) : FMLModContainer(mod, emptyList(), mod.scanData, ModuleLayer.empty()) {
    override fun getEventBus(): IEventBus {
        return mod.eventBus
    }
}
