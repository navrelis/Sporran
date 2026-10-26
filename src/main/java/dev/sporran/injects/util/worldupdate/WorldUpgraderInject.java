/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.util.worldupdate;

import net.neoforged.neoforge.common.IOUtilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.worldupdate.WorldUpgrader;

@Mixin(WorldUpgrader.class)
public abstract class WorldUpgraderInject {
    @Inject(method = "work", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/DimensionDataStorage;save()V", shift = At.Shift.AFTER))
    private void sporran$waitUntilIOWorkerComplete(CallbackInfo ci) {
        IOUtilities.waitUntilIOWorkerComplete();
    }
}
