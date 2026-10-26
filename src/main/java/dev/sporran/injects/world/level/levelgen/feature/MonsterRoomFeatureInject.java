/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.levelgen.feature;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.neoforged.neoforge.common.MonsterRoomHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MonsterRoomFeature.class)
public abstract class MonsterRoomFeatureInject {
    @ModifyReturnValue(method = "randomEntityId", at = @At("RETURN"))
    private EntityType<?> sporran$tryReturnRandomDungeonMob(EntityType<?> original, @Local(argsOnly = true) RandomSource randomSource) {
        return MonsterRoomHooks.getRandomMonsterRoomMob(randomSource);
    }
}
