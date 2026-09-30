package dev.sporran.injections.resources;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import dev.sporran.util.SporranHelper;

import java.util.List;
import java.util.Map;

public interface FileToIdConverterInjection {
    default Map<ResourceLocation, Resource> listMatchingResourcesFromNamespace(ResourceManager manager, String namespace) {
        throw SporranHelper.createMixinException(FileToIdConverterInjection.class, "listMatchingResourcesFromNamespace");
    }

    default Map<ResourceLocation, List<Resource>> listMatchingResourceStacksFromNamespace(ResourceManager manager, String namespace) {
        throw SporranHelper.createMixinException(FileToIdConverterInjection.class, "listMatchingResourceStacksFromNamespace");
    }
}
