/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.veil;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import foundry.veil.fabric.platform.FabricRegistrationFactory;
import foundry.veil.platform.registry.RegistrationProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

@IfModLoaded("veil")
@Mixin(FabricRegistrationFactory.class)
public abstract class FabricRegistrationFactoryMixin {
    @Inject(method = "create(Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;)Lfoundry/veil/platform/registry/RegistrationProvider;", at = @At("HEAD"))
    private <T> void sporran$veil$ensureUnfreezeRegistry(ResourceKey<? extends Registry<T>> key, String modId, CallbackInfoReturnable<RegistrationProvider<T>> cir) {
        if (BuiltInRegistries.REGISTRY instanceof MappedRegistry mappedRegistry)
            mappedRegistry.unfreeze();
    }
}
