/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
