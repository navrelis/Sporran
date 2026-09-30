package dev.sporran.mixin.compat.fabric_api;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.impl.registry.sync.trackers.Int2ObjectMapTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Int2ObjectMapTracker.class, remap = false)
public abstract class Int2ObjectMapTrackerMixin<V, OV> {
    @Shadow @Final private Int2ObjectMap<OV> mappers;

    @Inject(method = "onRemap", at = @At("HEAD"), cancellable = true)
    private void sporran$avoidModernFixResourcesCrash(RegistryIdRemapCallback.RemapState<V> state, CallbackInfo ci) {
        if (this.mappers.isEmpty())
            ci.cancel();
    }
}
