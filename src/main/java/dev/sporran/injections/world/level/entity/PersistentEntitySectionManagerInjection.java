/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
