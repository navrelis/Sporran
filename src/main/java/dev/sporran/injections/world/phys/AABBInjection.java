package dev.sporran.injections.world.phys;

import net.minecraft.world.phys.AABB;
import dev.sporran.util.SporranHelper;

public interface AABBInjection {
    AABB INFINITE = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    default boolean isInfinite() {
        throw SporranHelper.createMixinException(AABBInjection.class, "isInfinite");
    }
}
