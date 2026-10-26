/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.gui;

import net.minecraft.server.gui.MinecraftServerGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CountDownLatch;

@Mixin(MinecraftServerGui.class)
public abstract class MinecraftServerGuiInject {
    @Unique private CountDownLatch latch = new CountDownLatch(1);

    @Inject(method = "start", at = @At("TAIL"))
    private void sporran$startCountDownLatch(CallbackInfo ci) {
        this.latch.countDown();
    }

    @Inject(method = "print", at = @At("HEAD"))
    private void sporran$awaitLatch(CallbackInfo ci) {
        try {
            this.latch.await();
        } catch (InterruptedException ignored) {}
    }
}
