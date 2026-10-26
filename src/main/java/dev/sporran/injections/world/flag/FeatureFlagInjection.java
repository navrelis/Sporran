/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
