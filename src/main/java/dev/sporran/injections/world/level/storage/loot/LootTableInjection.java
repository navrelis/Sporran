package dev.sporran.injections.world.level.storage.loot;

import io.github.fabricators_of_create.porting_lib.loot.extensions.LootTableExtensions;
import net.minecraft.world.level.storage.loot.LootPool;
import dev.sporran.util.SporranHelper;

import java.util.List;

public interface LootTableInjection extends LootTableExtensions {
    default void freeze() {
        throw SporranHelper.createMixinException(LootTableInjection.class, "freeze");
    }

    default boolean isFrozen() {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "isFrozen");
    }

    default LootPool getPool(String name)  {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "getPool");
    }

    default LootPool removePool(String name) {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "removePool");
    }

    default void addPool(LootPool pool) {
        throw SporranHelper.createMixinException(LootPoolInjection.class, "addPool");
    }
}
