/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.mixin.modifications.modifiers

interface MethodBasedModifier : MixinModifier {
    val methods: List<String>
    var mappedMethods: List<String>

    override fun asString(): String {
        return "$this + (mappedOwner = $mappedOwner, mappedMethods = $mappedMethods)"
    }

    fun matches(methodParam: Any): Boolean {
        return this.methods.any { methodParam == it || (methodParam as List<String>).any { a -> a == it } }
                || this.mappedMethods.any { methodParam == it || (methodParam as List<String>).any { a -> a == it } }
    }
}