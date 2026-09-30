package dev.sporran.injections.world.level.storage;

import net.minecraft.world.level.storage.PrimaryLevelData;
import dev.sporran.util.SporranHelper;

public interface PrimaryLevelDataInjection {
    default boolean hasConfirmedExperimentalWarning() {
        throw SporranHelper.createMixinException(PrimaryLevelDataInjection.class, "hasConfirmedExperimentalWarning");
    }

    default PrimaryLevelData withConfirmedWarning(boolean confirmedWarning) {
        throw SporranHelper.createMixinException(PrimaryLevelDataInjection.class, "withConfirmedWarning");
    }
}
