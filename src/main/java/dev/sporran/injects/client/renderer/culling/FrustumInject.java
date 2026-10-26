// TRACKED HASH: 6f1cdd4a7dd57568eb7b70d3ed1a81d3d3ee248f
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.culling;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

@Mixin(Frustum.class)
public class FrustumInject {
    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void sporran$infiniteBoundEarlyExit(AABB aabb, CallbackInfoReturnable<Boolean> cir) {
        if (aabb.isInfinite())
            cir.setReturnValue(true);
    }
}