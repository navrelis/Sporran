/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.data.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import dev.sporran.injections.core.RegistrySetBuilderInjection;
import dev.sporran.mixin.data.registries.VanillaRegistriesAccessor;

import java.util.List;

public interface VanillaRegistriesInjection {
    List<? extends ResourceKey<? extends Registry<?>>> DATAPACK_REGISTRY_KEYS = ((RegistrySetBuilderInjection) VanillaRegistriesAccessor.getBuilder()).getEntryKeys();
}
