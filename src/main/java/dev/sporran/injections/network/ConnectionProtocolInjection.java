package dev.sporran.injections.network;

import dev.sporran.util.SporranHelper;

public interface ConnectionProtocolInjection {
    default boolean isPlay() {
        throw SporranHelper.createMixinException(ConnectionProtocolInjection.class, "isPlay");
    }

    default boolean isConfiguration() {
        throw SporranHelper.createMixinException(ConnectionProtocolInjection.class, "isConfiguration");
    }
}
