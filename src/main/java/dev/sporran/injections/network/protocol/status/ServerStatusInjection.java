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
