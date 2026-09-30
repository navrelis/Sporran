package dev.sporran.injections.server.network;

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
