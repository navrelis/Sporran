/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.HashMap;

public interface SpawnPlacementsInjection {
    static boolean hasPlacement(EntityType<?> type) {
        return SpawnPlacements.DATA_BY_TYPE.containsKey(type);
    }

    static void fireSpawnPlacementEvent() {
        var map = new HashMap<EntityType<?>, RegisterSpawnPlacementsEvent.MergedSpawnPredicate<?>>();

        SpawnPlacements.DATA_BY_TYPE.forEach((entityType, data) -> {
            map.put(entityType, new RegisterSpawnPlacementsEvent.MergedSpawnPredicate<>(data.predicate(), data.placement(), data.heightMap()));
        });

        ModLoader.postEvent(new RegisterSpawnPlacementsEvent(map));

        map.forEach((entityType, merged) -> {
            SpawnPlacements.DATA_BY_TYPE.put(entityType, new SpawnPlacements.Data(merged.getHeightmapType(), merged.getSpawnType(), merged.build()));
        });
    }
}
