/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.neoforged.neoforge.common.extensions.IFluidExtension;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.util.SporranHelper;

@Mixin(ExplosionDamageCalculator.class)
public abstract class ExplosionDamageCalculatorInject {
    // Sporran: asked against IBlockExtension / IFluidExtension, which declare getExplosionResistance as defaults (the plain getExplosionResistance(), what is replaced); against Block.class / Fluid.class they were true for everything.
    @WrapOperation(method = "getBlockExplosionResistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getExplosionResistance()F"))
    private float sporran$tryUseForgeExplosionResistance(Block instance, Operation<Float> original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true) Explosion explosion, @Local(argsOnly = true) BlockPos pos) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getClass(), IBlockExtension.class, "getExplosionResistance", BlockState.class, BlockGetter.class, BlockPos.class, Explosion.class)) {
            return instance.getExplosionResistance(state, level, pos, explosion);
        }

        return original.call(instance);
    }

    @WrapOperation(method = "getBlockExplosionResistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getExplosionResistance()F"))
    private float sporran$tryUseForgeExplosionResistance(FluidState instance, Operation<Float> original, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true) Explosion explosion, @Local(argsOnly = true) BlockPos pos) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getType().getClass(), IFluidExtension.class, "getExplosionResistance", FluidState.class, BlockGetter.class, BlockPos.class, Explosion.class)) {
            return instance.getExplosionResistance(level, pos, explosion);
        }

        return original.call(instance);
    }
}
