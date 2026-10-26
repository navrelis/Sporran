/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.remap.resource

import xyz.bluspring.knit.loader.mod.ModDefinition
import java.io.InputStream

interface ResourceRemapper {
    fun canTransform(path: String): Boolean

    fun transform(mod: ModDefinition, path: String, input: InputStream): ByteArray? {
        return transform(path, input)
    }

    fun transform(path: String, input: InputStream): ByteArray? {
        return null
    }
}