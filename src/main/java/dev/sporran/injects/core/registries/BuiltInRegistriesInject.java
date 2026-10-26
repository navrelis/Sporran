// TRACKED HASH: 33c37e1450e21ad82101b30b9cc1bf7f4cb0c12d
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.core.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.helpers.mixin.CreateStatic;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BuiltInRegistries.class)
public abstract class BuiltInRegistriesInject {
    @Shadow @Final private static Map<ResourceLocation, Supplier<?>> LOADERS;

    @CreateStatic
    private static Set<ResourceLocation> getVanillaRegistrationOrder() {
        return Collections.unmodifiableSet(LOADERS.keySet());
    }
}