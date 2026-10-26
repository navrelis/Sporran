/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.ai.behavior;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(VillagerMakeLove.class)
public abstract class VillagerMakeLoveInject {
    @Inject(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"), cancellable = true)
    private void sporran$preventBreedIfBlocked(ServerLevel level, Villager parent, Villager partner, CallbackInfoReturnable<Optional<Villager>> cir, @Local(ordinal = 2) Villager child) {
        if (!child.isAddedToLevel())
            cir.setReturnValue(Optional.empty());
    }
}
