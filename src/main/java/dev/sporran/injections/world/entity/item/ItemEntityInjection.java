/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.entity.item;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

public interface ItemEntityInjection {
    default int sporran$getLifespan() {
        throw SporranHelper.createMixinException(ItemEntityInjection.class, "sporran$getLifespan");
    }

    @Nullable
    default UUID getTarget() {
        throw SporranHelper.createMixinException(ItemEntityInjection.class, "getTarget");
    }
}
