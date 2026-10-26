/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.fabric_api;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.workarounds.FluidHandlerWorkaround;

import net.minecraft.world.level.material.Fluid;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl;

@Mixin(value = FluidRenderHandlerRegistryImpl.class, remap = false)
public abstract class FluidRenderHandlerRegistryImplMixin {
    @ModifyReturnValue(method = "get", at = @At("RETURN"))
    public FluidRenderHandler sporran$useForgeHandler(FluidRenderHandler original, @Local(argsOnly = true) Fluid fluid) {
        if (original != null)
            return original;

        var fluidType = fluid.neo$getFluidType();
        if (fluidType == NeoForgeMod.EMPTY_TYPE.value())
            return null;

        return FluidHandlerWorkaround.INSTANCE.getFluidRenderHandler(fluidType);
    }

    @ModifyReturnValue(method = "getOverride", at = @At("RETURN"))
    public FluidRenderHandler sporran$useForgeOverrides(FluidRenderHandler original, @Local(argsOnly = true) Fluid fluid) {
        if (original != null)
            return original;

        var fluidType = fluid.neo$getFluidType();
        if (fluidType == NeoForgeMod.EMPTY_TYPE.value())
            return null;

        return FluidHandlerWorkaround.INSTANCE.getFluidRenderHandler(fluidType);
    }
}
