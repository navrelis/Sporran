/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.remaps.fml.config

import net.neoforged.fml.config.IConfigSpec
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.ModContainer
import dev.sporran.loader.SporranModContainer

/*open class ModConfigRemap : ModConfig {
    constructor(type: Type, spec: IConfigSpec<*>, activeContainer: ModContainer) : super(type, spec, (activeContainer as SporranModContainer).modId)
    constructor(type: Type, spec: IConfigSpec<*>, activeContainer: ModContainer, fileName: String) : super(type, spec, (activeContainer as SporranModContainer).modId, fileName)
}*/