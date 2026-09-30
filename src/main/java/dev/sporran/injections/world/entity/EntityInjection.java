package dev.sporran.injections.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import java.util.function.Predicate;

public interface EntityInjection {
    default <T, C extends @Nullable Object> T getCapability(EntityCapability<T, C> capability, C context) {
        throw SporranHelper.createMixinException(EntityInjection.class, "getCapability");
    }

    default <T> T getCapability(EntityCapability<T, @Nullable Void> capability) {
        throw SporranHelper.createMixinException(EntityInjection.class, "getCapability");
    }
}
