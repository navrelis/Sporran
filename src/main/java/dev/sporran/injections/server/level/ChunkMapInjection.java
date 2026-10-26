/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.level;

import net.minecraft.server.level.ChunkTaskPriorityQueueSorter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import dev.sporran.util.SporranHelper;

import java.util.List;

public interface ChunkMapInjection {
    default void scheduleOnMainThreadMailbox(ChunkTaskPriorityQueueSorter.Message<Runnable> msg) {
        throw SporranHelper.createMixinException(ChunkMapInjection.class, "scheduleOnMainThreadMailbox");
    }

    default List<ServerPlayer> getPlayersWatching(Entity entity) {
        throw SporranHelper.createMixinException(ChunkMapInjection.class, "getPlayersWatching");
    }
}
