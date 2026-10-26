/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.wthit;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded("wthit")
@Pseudo
@Mixin(targets = "mcp.mobius.waila.Waila", remap = false)
public class WailaMixin {
    // Sporran: Avoid WTHIT from crashing with Sporran.

    @Dynamic
    @Inject(method = "unsupportedPlatform", at = @At("HEAD"), cancellable = true)
    private static void sporran$avoidForgeCrashOnWthit(CallbackInfo ci) {
        ci.cancel();
    }
}

