package dev.sporran.injects.server;

import net.minecraft.server.Main;
import net.neoforged.neoforge.server.loading.ServerModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.util.SporranTimings;

@Mixin(Main.class)
public abstract class MainInject {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;startTimerHackThread()V", shift = At.Shift.AFTER))
    private static void sporran$initForgeLoader(String[] strings, CallbackInfo ci) {
        long start = System.nanoTime();
        ServerModLoader.load();
        SporranTimings.log("ServerModLoader.load", System.nanoTime() - start);
    }

    // TODO: oh jesus christ good luck.
}
