/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create.mixin.colorwheel;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.djefrey.colorwheel.Colorwheel;
import dev.djefrey.colorwheel.neoforge.ClrwlNeoForgeXplat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("colorwheel")
@Pseudo
@Mixin(value = Colorwheel.class, remap = false)
public abstract class ColorwheelMixin {
    private static final ClrwlNeoForgeXplat sporran$neoXplat = new ClrwlNeoForgeXplat();

    @ModifyExpressionValue(method = "getModCompat", at = @At(value = "INVOKE", target = "Ldev/djefrey/colorwheel/ClrwlXplat;getCustomModCompatClasspath()Ljava/lang/String;"))
    private static String sporran$tryLoadSableCompatPath(String original) {
        if (original != null) {
            return original;
        }

        return sporran$neoXplat.getCustomModCompatClasspath();
    }
}
