/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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