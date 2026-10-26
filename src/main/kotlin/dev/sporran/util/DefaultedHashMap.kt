/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.util

class DefaultedHashMap<K, V>(capacity: Int, factor: Float) : HashMap<K, V>(capacity, factor) {
    var defaultValue: V? = null

    override fun get(key: K): V? {
        return super.get(key) ?: defaultValue
    }
}