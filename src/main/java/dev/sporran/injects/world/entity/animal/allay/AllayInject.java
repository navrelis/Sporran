/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.animal.allay;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Allay.class)
public abstract class AllayInject extends PathfinderMob {
    protected AllayInject(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "wantsToPickUp", at = @At("RETURN"))
    private boolean sporran$checkMobGriefingEvent(boolean original) {
        return original && EventHooks.canEntityGrief(this.level(), this);
    }
}
