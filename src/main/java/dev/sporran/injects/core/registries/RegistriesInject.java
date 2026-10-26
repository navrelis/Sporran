/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.core.registries;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.registries.Registries;

@Mixin(Registries.class)
public abstract class RegistriesInject {
    // Sporran: DO NOT BY ANY MEANS USE THIS, IT WILL CAUSE PROBLEMS. (#743, #161, #187)
//    @Inject(method = "elementsDirPath", at = @At("HEAD"), cancellable = true)
//    private static void sporran$checkPrefixNamespace(ResourceKey<? extends Registry<?>> registryKey, CallbackInfoReturnable<String> cir) {
//        cir.setReturnValue(CommonHooks.prefixNamespace(registryKey.location()));
//    }

    // Sporran: Handled by Fabric API
}
