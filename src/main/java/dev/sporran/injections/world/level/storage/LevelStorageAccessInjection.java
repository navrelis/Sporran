/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
