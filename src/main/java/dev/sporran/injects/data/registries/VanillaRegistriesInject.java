// TRACKED HASH: 7fb07c0db39d3d5c0d350e9748839bc08a66e387
package dev.sporran.injects.data.registries;

import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.data.registries.VanillaRegistriesInjection;

import java.util.List;

@Mixin(VanillaRegistries.class)
public class VanillaRegistriesInject {
    @CreateStatic
    private static final List<? extends ResourceKey<? extends Registry<?>>> DATAPACK_REGISTRY_KEYS = VanillaRegistriesInjection.DATAPACK_REGISTRY_KEYS;
}