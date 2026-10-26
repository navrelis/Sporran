/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.mixin.modifications

import org.objectweb.asm.Type

data class ParamPair(
    val descriptor: String,
    val ordinal: Int
) {
    constructor(type: Type, ordinal: Int) : this(type.descriptor, ordinal)
}
