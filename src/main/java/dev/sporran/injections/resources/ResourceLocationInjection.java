package dev.sporran.injections.resources;

import io.github.fabricators_of_create.porting_lib.extensions.common.ResourceLocationExtension;
import dev.sporran.processor.FabricInjectedInterface;

import net.minecraft.resources.ResourceLocation;

@FabricInjectedInterface(ResourceLocation.class)
public interface ResourceLocationInjection extends ResourceLocationExtension {
    private ResourceLocation self() { // Sporran: make private cuz otherwise things panic
        return (ResourceLocation) this;
    }

    default int compareNamespaced(ResourceLocation o) {
        var ret = self().getNamespace().compareTo(o.getNamespace());
        return ret != 0 ? ret : self().getPath().compareTo(o.getPath());
    }
}
