package dev.sporran.injections.world.level.entity;

import net.minecraft.world.level.entity.EntityAccess;
import dev.sporran.util.SporranHelper;

public interface PersistentEntitySectionManagerInjection<T extends EntityAccess> {
    default void sporran$markWithoutEvent() {
        throw SporranHelper.createMixinException(PersistentEntitySectionManagerInjection.class, "sporran$markWithoutEvent");
    }

    default boolean addNewEntityWithoutEvent(T entity) {
        throw SporranHelper.createMixinException(PersistentEntitySectionManagerInjection.class, "addNewEntityWithoutEvent");
    }
}
