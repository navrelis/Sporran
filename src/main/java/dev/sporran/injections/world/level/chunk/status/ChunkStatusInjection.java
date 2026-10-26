/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.chunk.status;

import java.util.EnumSet;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.level.levelgen.Heightmap;

public interface ChunkStatusInjection {
    default EnumSet<Heightmap.Types> getChunkSaveHeightmaps() {
        throw SporranHelper.createMixinException(ChunkStatusInjection.class, "getChunkSaveHeightmaps");
    }
}
