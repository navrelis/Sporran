package dev.sporran.injections.data.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import dev.sporran.injections.core.RegistrySetBuilderInjection;
import dev.sporran.mixin.data.registries.VanillaRegistriesAccessor;

import java.util.List;

public interface VanillaRegistriesInjection {
    List<? extends ResourceKey<? extends Registry<?>>> DATAPACK_REGISTRY_KEYS = ((RegistrySetBuilderInjection) VanillaRegistriesAccessor.getBuilder()).getEntryKeys();
}
