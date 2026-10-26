/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.storage;

import dev.sporran.util.SporranHelper;

public interface ServerLevelDataInjection {
    default void setDayTimeFraction(float dayTimeFraction) {
        throw SporranHelper.createMixinException(ServerLevelDataInjection.class, "setDayTimeFraction");
    }

    default float getDayTimeFraction() {
        throw SporranHelper.createMixinException(ServerLevelDataInjection.class, "getDayTimeFraction");
    }

    default float getDayTimePerTick() {
        throw SporranHelper.createMixinException(ServerLevelDataInjection.class, "getDayTimePerTick");
    }

    default void setDayTimePerTick(float dayTimePerTick) {
        throw SporranHelper.createMixinException(ServerLevelDataInjection.class, "setDayTimePerTick");
    }
}
