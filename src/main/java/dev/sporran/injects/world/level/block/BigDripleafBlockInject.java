/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BigDripleafBlock.class)
public abstract class BigDripleafBlockInject extends HorizontalDirectionalBlock {
    protected BigDripleafBlockInject(Properties properties) {
        super(properties);
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void sporran$checkCanSustainPlant(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        var soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault())
            cir.setReturnValue(soilDecision.isTrue());
    }
}
