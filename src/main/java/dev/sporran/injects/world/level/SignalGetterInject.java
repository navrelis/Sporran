/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SignalGetter.class)
public interface SignalGetterInject {
    @WrapOperation(method = "getSignal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isRedstoneConductor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean sporran$checkWeakPowerBlock(BlockState instance, BlockGetter blockGetter, BlockPos blockPos, Operation<Boolean> original, @Local(argsOnly = true) Direction direction) {
        return original.call(instance, blockGetter, blockPos) && instance.shouldCheckWeakPower((SignalGetter) this, blockPos, direction);
    }
}
