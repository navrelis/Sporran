/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
