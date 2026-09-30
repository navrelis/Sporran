package dev.sporran.workarounds;

import net.minecraft.resources.ResourceLocation;

public interface MappedRegistryWorkaround {

    void addAlias(ResourceLocation old, ResourceLocation newId);

}
