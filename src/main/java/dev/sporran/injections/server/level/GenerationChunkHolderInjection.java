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
