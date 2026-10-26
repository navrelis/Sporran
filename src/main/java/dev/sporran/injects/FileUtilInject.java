/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects;

import net.minecraft.FileUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FileUtil.class)
public abstract class FileUtilInject {
    //private static final Pattern RESERVED_WINDOWS_FILENAMES_NEOFORGE = Pattern.compile(".*\\.|(?:CON|PRN|AUX|NUL|CLOCK\\$|CONIN\\$|CONOUT\\$|(?:COM|LPT)[¹²³0-9])(?:\\..*)?", 2);
}
