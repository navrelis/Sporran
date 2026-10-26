/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.workarounds.avoid_feature_cycles;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.level.biome.BiomeGenerationSettings;

@Mixin(BiomeGenerationSettings.PlainBuilder.class)
public abstract class BiomeGenerationSettings$PlainBuilderMixin {
    @WrapOperation(method = "addFeature(ILnet/minecraft/core/Holder;)Lnet/minecraft/world/level/biome/BiomeGenerationSettings$PlainBuilder;", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private <E> boolean sporran$preventFeatureReuse(List<E> instance, E e, Operation<Boolean> original) {
        if (instance.contains(e)) { // Prevent adding duplicate features.
            return false;
        }

        return original.call(instance, e);
    }
}
