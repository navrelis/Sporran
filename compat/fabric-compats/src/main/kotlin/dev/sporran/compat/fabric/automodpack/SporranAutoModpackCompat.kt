package dev.sporran.compat.fabric.automodpack

import pl.skidam.automodpack_core.GlobalVariables
import java.nio.file.Path

object SporranAutoModpackCompat {

    val modpackDir: Path?
        get() = GlobalVariables.selectedModpackDir

}