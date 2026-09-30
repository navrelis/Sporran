package dev.sporran.injections.world.entity;

import dev.sporran.util.SporranHelper;

public interface LightningBoltInjection {
    default void setDamage(float damage) {
        throw SporranHelper.createMixinException(LightningBoltInjection.class, "setDamage");
    }

    default float getDamage() {
        throw SporranHelper.createMixinException(LightningBoltInjection.class, "getDamage");
    }
}
