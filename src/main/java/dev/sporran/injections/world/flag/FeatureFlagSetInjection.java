/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
