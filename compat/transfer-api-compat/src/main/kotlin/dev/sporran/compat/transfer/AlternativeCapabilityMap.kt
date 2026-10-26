/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.transfer

import com.google.common.collect.ForwardingMap

class AlternativeCapabilityMap<K, V>(private val delegate: MutableMap<K, V>, val provider: (K) -> V) : ForwardingMap<K, V>() {
    override fun delegate(): Map<K, V> = this.delegate

    override fun get(key: K?): V? {
        var value = super.get(key)

        if (key != null && value == null) {
            value = this.provider(key)
            this[key] = value
            return value
        }

        return value
    }
}
