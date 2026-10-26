/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.asm.coremod

import dev.sporran.loader.remap.SporranRemapper

object CoreModHelper {
    @JvmStatic
    fun remapClass(name: String): String {
        return SporranRemapper.remapClass(name, ignoreWorkaround = true)
    }

    @JvmStatic
    fun remapDescriptor(descriptor: String): String {
        return SporranRemapper.remapDescriptor(descriptor)
    }
}