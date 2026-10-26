/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.create.extensions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public interface EntityBuilderExtension<T> {

//    void sporran$setCustomClientFactory(BiFunction<PlayMessages.SpawnEntity, Level, T> customClientFactory);

    void sporran$setVelocityUpdateSupplier(Predicate<EntityType<?>> velocityUpdateSupplier);

    void sporran$setTrackingRangeSupplier(ToIntFunction<EntityType<?>> trackingRangeSupplier);

    void sporran$setUpdateIntervalSupplier(ToIntFunction<EntityType<?>> updateIntervalSupplier);

}
