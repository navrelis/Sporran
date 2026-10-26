/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create.flywheel

import dev.engine_room.flywheel.api.event.EndClientResourceReloadCallback
import dev.engine_room.flywheel.api.event.EndClientResourceReloadEvent
import dev.engine_room.flywheel.api.event.ReloadLevelRendererCallback
import dev.engine_room.flywheel.api.event.ReloadLevelRendererEvent
import net.neoforged.fml.ModLoader
import net.neoforged.neoforge.common.NeoForge

object FlywheelCompatBridge {
    fun init() {
        EndClientResourceReloadCallback.EVENT.register { minecraft, resourceManager, initialReload, error ->
            ModLoader.postEventWrapContainerInModOrder(EndClientResourceReloadEvent(minecraft, resourceManager, initialReload, error))
        }

        ReloadLevelRendererCallback.EVENT.register { level ->
            NeoForge.EVENT_BUS.post(ReloadLevelRendererEvent(level))
        }
    }
}
