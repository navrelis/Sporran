package dev.sporran.injections.world.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import dev.sporran.util.SporranHelper;

import java.util.Stack;

public interface LivingEntityInjection {
    default Stack<DamageContainer> sporran$getDamageContainers() {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "sporran$getDamageContainers");
    }

    default boolean removeEffectsCuredBy(EffectCure cure) {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "removeEffectsCuredBy");
    }

    default boolean shouldRiderFaceForward(Player player) {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "shouldRiderFaceForward");
    }

    // Sporran: used to report deaths of NeoForge mod entities that override die() without calling super to Fabric
    default void sporran$setPendingDeathEvent(LivingDeathEvent event) {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "sporran$setPendingDeathEvent");
    }

    default void sporran$markFabricDeathReported() {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "sporran$markFabricDeathReported");
    }

    // Sporran: posts LivingDeathEvent from inside vanilla LivingEntity/Player/ServerPlayer.die, see ArchitecturyLivingDeathBridge
    default boolean sporran$postVanillaLivingDeath(DamageSource source) {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "sporran$postVanillaLivingDeath");
    }

    default boolean sporran$isPostingVanillaLivingDeath() {
        throw SporranHelper.createMixinException(LivingEntityInjection.class, "sporran$isPostingVanillaLivingDeath");
    }
}
