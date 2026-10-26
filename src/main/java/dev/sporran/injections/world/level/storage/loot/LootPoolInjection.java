/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.storage.loot;

import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import dev.sporran.util.SporranHelper;

public interface LootPoolInjection {
    default void freeze() {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "freeze");
    }

    default boolean isFrozen() {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "isFrozen");
    }

    default NumberProvider getRolls() {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "getRolls");
    }

    default NumberProvider getBonusRolls() {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "getBonusRolls");
    }

    default void setRolls(NumberProvider provider) {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "setRolls");
    }

    default void setBonusRolls(NumberProvider provider) {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "setBonusRolls");
    }
}
