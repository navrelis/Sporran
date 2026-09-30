package dev.sporran.injections.world.level.biome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import dev.sporran.util.SporranHelper;

import java.util.Set;

public interface MobSpawnSettingsInjection {
    default Set<MobCategory> getSpawnerTypes() {
        throw SporranHelper.createMixinException(MobSpawnSettingsInjection.class, "getSpawnerTypes");
    }

    default Set<EntityType<?>> getEntityTypes() {
        throw SporranHelper.createMixinException(MobSpawnSettingsInjection.class, "getEntityTypes");
    }
}
