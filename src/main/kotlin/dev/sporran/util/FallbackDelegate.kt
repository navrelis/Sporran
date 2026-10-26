/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FallbackDelegate<T : Any>(private val fallbackProvider: () -> T) : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: fallbackProvider()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun <T : Any> fallback(provider: () -> T): FallbackDelegate<T> = FallbackDelegate(provider)
