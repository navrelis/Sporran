/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
