/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelSettings;
import dev.sporran.util.SporranHelper;

public interface LevelSettingsInjection {
    default LevelSettings withLifecycle(Lifecycle lifecycle) {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "withLifecycle");
    }

    default Lifecycle getLifecycle() {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "getLifecycle");
    }

    default void sporran$setLifecycle(Lifecycle lifecycle) {
        throw SporranHelper.createMixinException(LevelSettingsInjection.class, "sporran$setLifecycle");
    }
}
