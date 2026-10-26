/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.phys;

import net.minecraft.world.phys.AABB;
import dev.sporran.util.SporranHelper;

public interface AABBInjection {
    AABB INFINITE = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    default boolean isInfinite() {
        throw SporranHelper.createMixinException(AABBInjection.class, "isInfinite");
    }
}
