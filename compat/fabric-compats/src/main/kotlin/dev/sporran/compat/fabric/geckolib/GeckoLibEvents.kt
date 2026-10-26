/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.geckolib

import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent
import net.neoforged.neoforge.common.NeoForge
import software.bernie.geckolib.event.GeoRenderEvent

// Sporran: this is a mess, I hate everything about this.
object GeckoLibEvents {
    fun init() {
        GeoRenderEvent.Armor.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Armor.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Armor.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }

        GeoRenderEvent.Block.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Block.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Block.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }

        GeoRenderEvent.Entity.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Entity.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Entity.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }

        GeoRenderEvent.Item.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Item.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Item.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }

        GeoRenderEvent.Object.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Object.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.Object.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }

        GeoRenderEvent.ReplacedEntity.Pre.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.ReplacedEntity.Post.EVENT.register { callForgeEventOnFabric(it) }
        GeoRenderEvent.ReplacedEntity.CompileRenderLayers.EVENT.register { callForgeEventOnFabric(it) }
    }

    private fun <T : GeoRenderEvent> callForgeEventOnFabric(event: T): Boolean {
        return (NeoForge.EVENT_BUS.post(event as Event) as? ICancellableEvent)?.isCanceled != true
    }
}