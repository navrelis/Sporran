/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.storage.loot.parameters;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootContextParamSets.class)
public abstract class LootContextParamSetsInject {
    @Inject(method = "method_15970", at = @At("TAIL"))
    private static void sporran$appendAttackingEntityToChest(LootContextParamSet.Builder builder, CallbackInfo ci) {
        builder.optional(LootContextParams.ATTACKING_ENTITY);
    }

    @Inject(method = "method_764", at = @At("TAIL"))
    private static void sporran$appendAttackingEntityToFishing(LootContextParamSet.Builder builder, CallbackInfo ci) {
        builder.optional(LootContextParams.ATTACKING_ENTITY);
    }
}
