package dev.sporran.injections.server.packs.resources;

import net.minecraft.resources.ResourceLocation;
import dev.sporran.util.SporranHelper;

public interface SimpleJsonResourceReloadListenerInjection {
    default ResourceLocation getPreparedPath(ResourceLocation loc) {
        throw SporranHelper.createMixinException(SimpleJsonResourceReloadListenerInjection.class, "getPreparedPath");
    }
}
