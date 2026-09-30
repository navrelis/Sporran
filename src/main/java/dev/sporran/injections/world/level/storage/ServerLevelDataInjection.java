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
