/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.resources;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.resources.FileToIdConverterInjection;

import java.util.List;
import java.util.Map;

@Mixin(FileToIdConverter.class)
public abstract class FileToIdConverterInject implements FileToIdConverterInjection {
    @Shadow @Final private String prefix;
    @Shadow @Final private String extension;

    @Override
    public Map<ResourceLocation, Resource> listMatchingResourcesFromNamespace(ResourceManager manager, String namespace) {
        String extension = this.extension;
        return manager.listResources(this.prefix, path -> path.getNamespace().equals(namespace) && path.getPath().endsWith(extension));
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listMatchingResourceStacksFromNamespace(ResourceManager manager, String namespace) {
        String extension = this.extension;
        return manager.listResourceStacks(this.prefix, path -> path.getNamespace().equals(namespace) && path.getPath().endsWith(extension));
    }
}
