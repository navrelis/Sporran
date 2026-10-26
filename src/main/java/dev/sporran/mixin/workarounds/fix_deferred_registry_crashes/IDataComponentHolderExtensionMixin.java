/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.workarounds.fix_deferred_registry_crashes;

import java.util.function.Supplier;

import net.neoforged.neoforge.common.extensions.IDataComponentHolderExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponentType;

@Mixin(IDataComponentHolderExtension.class)
public interface IDataComponentHolderExtensionMixin {
    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    private void sporran$avoidCrashWithUnboundHolders(Supplier<? extends DataComponentType<?>> type, CallbackInfoReturnable<Boolean> cir) {
        if (type instanceof DeferredHolder<?, ?> holder && !holder.isBound())
            cir.setReturnValue(false);
    }
}
