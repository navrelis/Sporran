/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.util

import it.unimi.dsi.fastutil.Hash
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet

class CaseInsensitiveStringHashSet : ObjectOpenCustomHashSet<String>(CaseInsensitiveStringStrategy) {
    object CaseInsensitiveStringStrategy : Hash.Strategy<String> {
        override fun equals(a: String?, b: String?): Boolean {
            return a == b || a?.lowercase() == b?.lowercase()
        }

        override fun hashCode(o: String?): Int {
            return o?.lowercase()?.hashCode() ?: 0
        }
    }
}