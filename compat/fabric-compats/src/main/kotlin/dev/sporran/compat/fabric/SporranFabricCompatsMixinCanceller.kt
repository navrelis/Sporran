/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric

import com.bawnorton.mixinsquared.api.MixinCanceller
import net.fabricmc.loader.api.FabricLoader

class SporranFabricCompatsMixinCanceller : MixinCanceller {
    override fun shouldCancel(targetClassNames: List<String>, mixinClassName: String): Boolean {
        // Avoid registering mixins that are already handled by Sable Fabric
        if (mixinClassName.startsWith("dev.ryanhcode.sable.neoforge.mixin")) {
            // TODO: we should make this actually better
            if (mixinClassName.contains("entity.entity_swimming"))
                return true

            val sableFabric = FabricLoader.getInstance().getModContainer("sable").orElseThrow()

            if (sableFabric.findPath(mixinClassName.replace(".", "/").replace("/sable/neoforge/", "/sable/fabric/") + ".class").isPresent) {
                return true
            }
        }

        return false
    }
}
