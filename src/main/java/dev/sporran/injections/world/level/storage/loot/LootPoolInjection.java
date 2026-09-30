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
