package dev.sporran.injections.world.item;

import net.minecraft.world.entity.EntityType;

public interface SpawnEggItemInjection {
    EntityType<?> getDefaultType();
}
