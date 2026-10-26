/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.transfer.mixin;

import java.util.List;
import java.util.Map;

import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@Mixin(EntityCapability.class)
public interface EntityCapabilityAccessor<T, C extends @Nullable Object> {
    @Accessor("providers")
    Map<EntityType<?>, List<ICapabilityProvider<Entity, C, T>>> sporran$getProviders();

    @Mutable
    @Accessor("providers")
    void sporran$setProviders(Map<EntityType<?>, List<ICapabilityProvider<Entity, C, T>>> providers);
}
