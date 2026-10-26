/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.provider

import net.neoforged.fml.ModContainer
import net.neoforged.neoforgespi.language.IModInfo
import net.neoforged.neoforgespi.language.IModLanguageLoader
import net.neoforged.neoforgespi.language.ModFileScanData

object NoopLanguageLoader : IModLanguageLoader {
    override fun name(): String = "sporran_noop"

    override fun version(): String = "0.0.0"

    override fun loadMod(
        info: IModInfo?,
        modFileScanResults: ModFileScanData?,
        layer: ModuleLayer?
    ): ModContainer? {
        throw IllegalStateException()
    }
}
