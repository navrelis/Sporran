/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.util.SporranHelper;

@Mixin(FlyingMob.class)
public abstract class FlyingMobInject extends Mob {
    protected FlyingMobInject(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState sporran$storeCurrentBlockState(Level instance, BlockPos pos, Operation<BlockState> original, @Share("blockState") LocalRef<BlockState> stateRef, @Share("blockPos") LocalRef<BlockPos> posRef) {
        var state = original.call(instance, pos);
        stateRef.set(state);
        posRef.set(pos);

        return state;
    }

    // Sporran: asked against IBlockExtension, which declares getFriction as a default (block.getFriction(), what is replaced); against Block.class it was true for every block.
    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float sporran$callForgeFrictionIfPossible(Block instance, Operation<Float> original, @Share("blockState") LocalRef<BlockState> stateRef, @Share("blockPos") LocalRef<BlockPos> posRef) {
        if (SporranHelper.INSTANCE.hasMethodOverride(instance.getClass(), IBlockExtension.class, "getFriction", BlockState.class, LevelReader.class, BlockPos.class, Entity.class)) {
            return instance.getFriction(stateRef.get(), this.level(), posRef.get(), this);
        }

        return original.call(instance);
    }
}
