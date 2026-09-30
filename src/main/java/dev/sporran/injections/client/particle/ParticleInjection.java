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
