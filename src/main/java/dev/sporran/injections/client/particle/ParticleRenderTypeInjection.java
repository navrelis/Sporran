package dev.sporran.injections.client.particle;

import dev.sporran.util.SporranHelper;

public interface ParticleRenderTypeInjection {
    default boolean isTranslucent() {
        throw SporranHelper.createMixinException(ParticleRenderTypeInjection.class, "isTranslucent");
    }
}
