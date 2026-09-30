package dev.sporran.injections.world.flag;

import dev.sporran.util.SporranHelper;

public interface FeatureFlagSetInjection {
    default long[] sporran$extendedMask() {
        throw SporranHelper.createMixinException(FeatureFlagSetInjection.class, "sporran$extendedMask");
    }

    default void sporran$setExtendedMask(long[] extendedMask) {
        throw SporranHelper.createMixinException(FeatureFlagSetInjection.class, "sporran$setExtendedMask");
    }
}
