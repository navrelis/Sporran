package dev.sporran.injects.world.entity.monster;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.world.entity.projectile.ProjectileUtilInjection;

@Mixin(Illusioner.class)
public abstract class IllusionerInject extends SpellcasterIllager {
    protected IllusionerInject(EntityType<? extends SpellcasterIllager> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "performRangedAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"))
    private InteractionHand sporran$tryGetHandWithCrossbow(LivingEntity shooter, Item weapon, Operation<InteractionHand> original) {
        if (shooter.isHolding(weapon))
            return original.call(shooter, weapon);

        return ProjectileUtilInjection.getWeaponHoldingHand(shooter, item -> item instanceof BowItem);
    }
}
