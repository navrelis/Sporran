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