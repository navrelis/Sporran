/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create

import com.bawnorton.mixinsquared.api.MixinCanceller
import net.fabricmc.loader.api.FabricLoader

class SporranCreateCompatMixinCanceller : MixinCanceller {
    override fun shouldCancel(targetClassNames: List<String>, mixinClassName: String): Boolean {
        // Avoid registering mixins that are already handled by Colorwheel Fabric
        if (mixinClassName.startsWith("dev.djefrey.colorwheel.neoforge.mixin")) {
            // TODO: we should make this actually better
            val sableFabric = FabricLoader.getInstance().getModContainer("colorwheel").orElseThrow()

            if (sableFabric.findPath(mixinClassName.replace(".", "/").replace("/colorwheel/neoforge/", "/colorwheel/fabric/") + ".class").isPresent) {
                return true
            }
        }

        return false
    }
}
