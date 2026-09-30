package dev.sporran.injections.client.multiplayer;

import net.neoforged.neoforge.client.ExtendedServerListData;

public interface ServerDataInjection {
    default ExtendedServerListData sporran$getNeoForgeData() {
        throw new IllegalStateException();
    }

    default void sporran$setNeoForgeData(ExtendedServerListData data) {
        throw new IllegalStateException();
    }
}
