package dev.sporran.mixin.workarounds.fluid_type;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabricators_of_create.porting_lib.fluids.PortingLibFluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.StupidWorkarounds;
import dev.sporran.workarounds.FabricFluidTypeExtensions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

@Mixin(IClientFluidTypeExtensions.class)
public interface IClientFluidTypeExtensionsMixin {
    @WrapOperation(method = {"of(Lnet/minecraft/world/level/material/Fluid;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"}, at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;of(Lnet/neoforged/neoforge/fluids/FluidType;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"))
    private static IClientFluidTypeExtensions sporran$tryHandleFabricFluidTypes(FluidType type, Operation<IClientFluidTypeExtensions> original, @Local(argsOnly = true) Fluid fluid) {
        if (type.sporran$isWrapped) {
            return StupidWorkarounds.sporran$fabricFluidExtensions.computeIfAbsent(type, $ -> new FabricFluidTypeExtensions(fluid));
        }

        return original.call(type);
    }

    @WrapOperation(method = {"of(Lnet/minecraft/world/level/material/FluidState;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"}, at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;of(Lnet/neoforged/neoforge/fluids/FluidType;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"))
    private static IClientFluidTypeExtensions sporran$tryHandleFabricFluidTypes(FluidType type, Operation<IClientFluidTypeExtensions> original, @Local(argsOnly = true) FluidState state) {
        if (type.sporran$isWrapped) {
            return StupidWorkarounds.sporran$fabricFluidExtensions.computeIfAbsent(type, $ -> new FabricFluidTypeExtensions(state.getType()));
        }

        return original.call(type);
    }

    @Inject(method = "of(Lnet/neoforged/neoforge/fluids/FluidType;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;", at = @At("HEAD"), cancellable = true)
    private static void sporran$tryHandleFabricFluidTypes(FluidType type, CallbackInfoReturnable<IClientFluidTypeExtensions> cir) {
        if (type.sporran$isWrapped) {
            if (StupidWorkarounds.sporran$fabricFluidExtensions.containsKey(type)) {
                cir.setReturnValue(StupidWorkarounds.sporran$fabricFluidExtensions.get(type));
                return;
            }

            // Sporran: Try to derive the fluid type
            if (type.sporran$wrapped != null) {
                var key = PortingLibFluids.FLUID_TYPES.getKey(type.sporran$wrapped);
                var fluid = BuiltInRegistries.FLUID.getOptional(key).orElse(null);

                if (fluid != null) {
                    var fabricExt = new FabricFluidTypeExtensions(fluid);
                    StupidWorkarounds.sporran$fabricFluidExtensions.put(type, fabricExt);

                    cir.setReturnValue(fabricExt);
                    StupidWorkarounds.sporran$fabricFluidExtensions.put(type, fabricExt);
                    return;
                }
            }
        }
    }
}
