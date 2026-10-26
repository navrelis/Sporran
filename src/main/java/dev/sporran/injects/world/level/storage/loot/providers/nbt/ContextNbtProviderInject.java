/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.storage.loot.providers.nbt;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.world.level.storage.loot.EntityTargetInjection;

@Mixin(ContextNbtProvider.class)
public abstract class ContextNbtProviderInject {
    @Mixin(targets = "net/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$2")
    public static abstract class Getter2Inject {
        @Shadow @Final private LootContext.EntityTarget val$target;

        @ModifyReturnValue(method = "getId", at = @At("RETURN"))
        private String sporran$useInternalName(String original) {
            return ((EntityTargetInjection) (Object) val$target).getName();
        }
    }
}
