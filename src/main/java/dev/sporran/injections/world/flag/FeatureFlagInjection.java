package dev.sporran.injections.world.flag;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.flag.FeatureFlagUniverse;

public interface FeatureFlagInjection {
    default int sporran$extMaskIndex() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "sporran$extMaskIndex");
    }

    default void sporran$setExtMaskIndex(int index) {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "sporran$setExtMaskIndex");
    }

    default boolean isModded() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "isModded");
    }

    default void sporran$setModded(boolean modded) {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "sporran$setModded");
    }

    default FeatureFlagUniverse sporran$universe() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "sporran$universe");
    }

    default long sporran$mask() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "sporran$mask");
    }
}
