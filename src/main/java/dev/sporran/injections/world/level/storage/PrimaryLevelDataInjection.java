/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
