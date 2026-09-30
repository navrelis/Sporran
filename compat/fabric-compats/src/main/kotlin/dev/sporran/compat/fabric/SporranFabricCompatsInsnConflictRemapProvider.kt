package dev.sporran.compat.fabric

import dev.sporran.api.remapping.InsnConflictRemapProvider

class SporranFabricCompatsInsnConflictRemapProvider : InsnConflictRemapProvider {
    override fun remapMethod(owner: String, name: String, descriptor: String): String {
        if (owner == "virtuoel/pehkui/api/ScaleType") {
            if ((name == "getScaleChangedEvent" || name == "getPreTickEvent" || name == "getPostTickEvent") && descriptor == "Ljava/util/Collection;") {
                return $$"sporran$pehkui$$$name"
            }
        }

        return super.remapMethod(owner, name, descriptor)
    }
}