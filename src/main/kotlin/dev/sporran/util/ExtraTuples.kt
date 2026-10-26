/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.util

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D) {
    override fun toString(): String {
        return "($first, $second, $third, $fourth)"
    }
}