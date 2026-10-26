/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.automodpack

import pl.skidam.automodpack_core.GlobalVariables
import java.nio.file.Path

object SporranAutoModpackCompat {

    val modpackDir: Path?
        get() = GlobalVariables.selectedModpackDir

}