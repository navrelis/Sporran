package dev.sporran.injections.world.level.chunk;

import dev.sporran.util.SporranHelper;

public interface ChunkGeneratorInjection {
    default void refreshFeaturesPerStep() {
        throw SporranHelper.createMixinException(ChunkGeneratorInjection.class, "refreshFeaturesPerStep");
    }
}
