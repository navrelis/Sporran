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
