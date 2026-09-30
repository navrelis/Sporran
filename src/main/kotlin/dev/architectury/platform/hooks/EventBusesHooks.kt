package dev.architectury.platform.hooks

import net.neoforged.bus.api.IEventBus
import dev.sporran.loader.SporranLoader
import java.util.*
import java.util.function.Consumer

// Reimplemented from https://github.com/architectury/architectury-api/blob/1.20/forge/src/main/java/dev/architectury/platform/forge/EventBuses.java,
// because OpenComputers II: Reimagined uses this for some reason.
object EventBusesHooks {
    @JvmStatic
    fun whenAvailable(modId: String, busConsumer: Consumer<IEventBus>) {
        busConsumer.accept(getModEventBus(modId).orElseThrow { IllegalStateException("Mod '$modId' is not available!") })
    }

    @JvmStatic
    fun getModEventBus(modId: String): Optional<IEventBus> {
        return Optional.ofNullable(SporranLoader.instance.getMod(modId)?.eventBus)
    }
}