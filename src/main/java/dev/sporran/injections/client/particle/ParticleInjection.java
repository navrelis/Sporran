/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.particle;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.sporran.util.SporranHelper;

public interface ParticleInjection {
    default AABB getRenderBoundingBox(float partialTicks) {
        throw SporranHelper.createMixinException(ParticleInjection.class, "getRenderBoundingBox");
    }

    default Vec3 getPos() {
        throw SporranHelper.createMixinException(ParticleInjection.class, "getPos");
    }
}
