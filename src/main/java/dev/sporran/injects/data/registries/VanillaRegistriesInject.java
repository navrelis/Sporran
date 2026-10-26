// TRACKED HASH: 7fb07c0db39d3d5c0d350e9748839bc08a66e387
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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