package dev.sporran.compat.create.mixin.ponder_fabric;

import net.createmod.ponder.FabricPonderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(FabricPonderClient.class)
public abstract class FabricPonderClientMixin {
    @Redirect(method = "onClientStarted", at = @At(value = "INVOKE", target = "Lnet/createmod/ponder/PonderClient;modLoadCompleted()V"))
    private static void sporran$ponder_fabric$noopModLoad() {}
}
