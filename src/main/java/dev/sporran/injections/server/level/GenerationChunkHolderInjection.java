/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server.level;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.level.chunk.LevelChunk;

public interface GenerationChunkHolderInjection {
    default LevelChunk sporran$getCurrentlyLoading() {
        throw SporranHelper.createMixinException(GenerationChunkHolderInjection.class, "sporran$getCurrentlyLoading");
    }

    default void sporran$setCurrentlyLoading(LevelChunk chunk) {
        throw SporranHelper.createMixinException(GenerationChunkHolderInjection.class, "sporran$setCurrentlyLoading");
    }
}
