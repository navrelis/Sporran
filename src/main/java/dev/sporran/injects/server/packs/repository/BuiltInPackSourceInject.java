/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.packs.repository;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.server.packs.repository.BuiltInPackSourceInjection;

import java.util.function.Function;

@Mixin(BuiltInPackSource.class)
public abstract class BuiltInPackSourceInject implements BuiltInPackSourceInjection {
    @CreateStatic
    private static Pack.ResourcesSupplier fromName(final Function<PackLocationInfo, PackResources> onName) {
        return BuiltInPackSourceInjection.fromName(onName);
    }
}
