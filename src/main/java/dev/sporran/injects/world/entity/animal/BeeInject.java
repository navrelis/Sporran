/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.animal;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
public abstract class BeeInject {
    @Definition(id = "blockEntity", local = @Local(type = BlockEntity.class))
    @Definition(id = "getType", method = "Lnet/minecraft/world/level/block/entity/BlockEntity;getType()Lnet/minecraft/world/level/block/entity/BlockEntityType;")
    @Expression("blockEntity.getType() == ?")
    @ModifyExpressionValue(method = "isHiveValid", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$checkIfBeehiveBlockEntity(boolean original, @Local BlockEntity blockEntity) {
        return original || blockEntity instanceof BeehiveBlockEntity;
    }
}
