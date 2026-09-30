package dev.sporran.injections.world.level.storage;

import dev.sporran.util.SporranHelper;

import java.nio.file.Path;

public interface LevelStorageAccessInjection {
    default void readAdditionalLevelSaveData(boolean fallback) {
        throw SporranHelper.createMixinException(LevelStorageAccessInjection.class, "readAdditionalLevelSaveData");
    }

    default Path getWorldDir() {
        throw SporranHelper.createMixinException(LevelStorageAccessInjection.class, "getWorldDir");
    }
}
