/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
