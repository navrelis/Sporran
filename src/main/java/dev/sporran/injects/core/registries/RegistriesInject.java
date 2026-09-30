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
