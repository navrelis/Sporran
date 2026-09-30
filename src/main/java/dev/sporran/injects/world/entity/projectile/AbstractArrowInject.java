package dev.sporran.injects.world.entity.projectile;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowInject extends Projectile {
    // Non-NONE sentinel: vanilla's "deflection != NONE -> break" then leaves the loop like NeoForge's break on a cancelled impact event
    @Unique private static final ProjectileDeflection SPORRAN$BREAK = (projectile, entity, random) -> {};

    public AbstractArrowInject(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean sporran$clearFireIfFluidExtinguishes(boolean original) {
        var entity = this;

        return original || this.isInFluidType((fluidType, height) -> entity.canFluidExtinguish(fluidType));
    }

    @Definition(id = "hasImpulse", field = "Lnet/minecraft/world/entity/projectile/AbstractArrow;hasImpulse:Z")
    @Expression("this.hasImpulse = @(true)")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sporran$usActualImpulseValue(boolean original, @Share("currentImpulse") LocalBooleanRef currentImpulse) {
        // NeoForge leaves hasImpulse untouched on MISS / cancelled impact
        return currentImpulse.get() || this.hasImpulse;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"))
    private ProjectileDeflection sporran$handleImpact(AbstractArrow instance, HitResult hitResult, Operation<ProjectileDeflection> original, @Share("currentImpulse") LocalBooleanRef currentImpulse) {
        // The level clip returns a non-null MISS every tick; NeoForge never lets it reach onHit
        if (hitResult.getType() == HitResult.Type.MISS) {
            currentImpulse.set(false);
            return ProjectileDeflection.NONE;
        }

        if (EventHooks.onProjectileImpact(this, hitResult)) {
            currentImpulse.set(false);
            return SPORRAN$BREAK;
        }

        currentImpulse.set(true);
        return original.call(instance, hitResult);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 1), cancellable = true)
    private void sporran$cancelIfRemoved(CallbackInfo ci) {
        if (this.isRemoved())
            ci.cancel();
    }
}
