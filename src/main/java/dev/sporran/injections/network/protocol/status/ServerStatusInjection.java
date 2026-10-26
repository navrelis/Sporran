/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.network.protocol.status;

import dev.sporran.util.SporranHelper;

public interface ServerStatusInjection {
    default boolean isModded() {
        throw SporranHelper.createMixinException(ServerStatusInjection.class, "isModded");
    }

    default void sporran$setModded(boolean isModded) {
        throw SporranHelper.createMixinException(ServerStatusInjection.class, "sporran$setModded");
    }
}
