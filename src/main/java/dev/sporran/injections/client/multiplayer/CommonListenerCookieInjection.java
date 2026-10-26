/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.multiplayer;

import net.neoforged.neoforge.network.connection.ConnectionType;
import dev.sporran.util.SporranHelper;

public interface CommonListenerCookieInjection {
    default ConnectionType connectionType() {
        throw SporranHelper.createMixinException(CommonListenerCookieInjection.class, "connectionType");
    }

    default void sporran$setConnectionType(ConnectionType connectionType) {
        throw SporranHelper.createMixinException(CommonListenerCookieInjection.class, "sporran$setConnectionType");
    }
}
