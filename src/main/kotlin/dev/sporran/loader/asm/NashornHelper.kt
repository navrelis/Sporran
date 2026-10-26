/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.asm

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import java.util.function.Function
import javax.script.Bindings

object NashornHelper {
    fun <A, R> getFunction(obj: Bindings): Function<A, R> {
        return Function {
            (obj as ScriptObjectMirror).call(obj, it) as R
        }
    }
}