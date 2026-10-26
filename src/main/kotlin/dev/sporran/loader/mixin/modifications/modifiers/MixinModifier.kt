/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.mixin.modifications.modifiers

interface MixinModifier {
    val owner: String
    var mappedOwner: String

    fun asString(): String {
        return "$this"
    }
}