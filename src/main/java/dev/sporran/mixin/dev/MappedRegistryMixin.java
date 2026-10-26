/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.dev;

import com.moulberry.mixinconstraints.annotations.IfDevEnvironment;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfDevEnvironment
@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
    @Shadow @Final private ResourceKey<? extends Registry<T>> key;

    @Inject(method = "freeze", at = @At("HEAD"))
    private void sporran$checkFreezers(CallbackInfoReturnable<Registry<T>> cir) {
        //System.out.println("Registry " + this.key + " was frozen");
        //(new Exception("frozen")).printStackTrace();
    }
}
