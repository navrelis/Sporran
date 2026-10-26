/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.storage.loot;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(LootParams.class)
public abstract class LootParamsInject {
    @Mixin(LootParams.Builder.class)
    public static abstract class BuilderInject {
        @WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Ljava/util/Set;isEmpty()Z"))
        private boolean sporran$allowCustomLootParams(Set instance, Operation<Boolean> original) {
            return true;
        }
    }
}
