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
