/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.remap

import net.fabricmc.loader.api.MappingResolver
import net.minecraftforge.srgutils.IMappingFile
import net.minecraftforge.srgutils.IRenamer

class DevMojClassMappingRenamer(private val resolver: MappingResolver) : IRenamer {
    override fun rename(value: IMappingFile.IClass): String {
        return resolver.mapClassName("intermediary", value.mapped.replace("/", ".")).replace(".", "/")
    }
}