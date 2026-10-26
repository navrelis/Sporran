/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.mixin.modifications

import com.llamalad7.mixinextras.sugar.Local
import org.objectweb.asm.Type

@JvmRecord
data class LocalPair(
    val descriptor: String,
    val local: Local,
    val unboxRef: Boolean = true
) {
    constructor(type: Type, local: Local) : this(type.descriptor, local)
}
