/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.server;

import java.util.Map;

import dev.sporran.util.SporranHelper;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface MinecraftServerInjection {
    default long[] getTickTime(ResourceKey<Level> dim) {
        throw new IllegalStateException();
    }

    default String getStatusJson() {
        throw new IllegalStateException();
    }

    default MinecraftServer.ReloadableResources getServerResources() {
        throw SporranHelper.createMixinException(MinecraftServerInjection.class, "getServerResources");
    }

    default Map<ResourceKey<Level>, ServerLevel> forgeGetWorldMap() {
        throw SporranHelper.createMixinException(MinecraftServerInjection.class, "forgeGetWorldMap");
    }

    default void markWorldsDirty() {
        throw SporranHelper.createMixinException(MinecraftServerInjection.class, "markWorldsDirty");
    }
}
