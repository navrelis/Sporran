/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.level;

import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;

public interface DistanceManagerInjection {
    <T> void addRegionTicket(TicketType<T> type, ChunkPos pos, int distance, T value, boolean forceTicks);
    <T> void removeRegionTicket(TicketType<T> type, ChunkPos pos, int distance, T value, boolean forceTicks);
}
