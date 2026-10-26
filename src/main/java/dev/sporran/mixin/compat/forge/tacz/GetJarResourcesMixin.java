/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.forge.tacz;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Pseudo
@Mixin(targets = "com.tacz.guns.util.GetJarResources", remap = false)
public abstract class GetJarResourcesMixin {
    @Redirect(method = "lambda$copyFolder$0", at = @At(value = "INVOKE", target = "Ljava/nio/file/Path;resolve(Ljava/lang/String;)Ljava/nio/file/Path;"))
    private static Path sporran$useProperPathRelativize(Path targetPath, String other, @Local(ordinal = 1, argsOnly = true) Path source, @Local(ordinal = 0, argsOnly = true) URI sourceURI) {
        return targetPath.resolve(Paths.get(sourceURI).relativize(source).toString());
    }
}
