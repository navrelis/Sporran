/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.core;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import dev.sporran.processor.FabricInjectedInterface;

import java.util.List;

@FabricInjectedInterface(RegistrySetBuilder.class)
public interface RegistrySetBuilderInjection {
    List<? extends ResourceKey<? extends Registry<?>>> getEntryKeys();
}
