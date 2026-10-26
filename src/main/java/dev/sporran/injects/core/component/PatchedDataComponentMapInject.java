/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.core.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import dev.sporran.injections.core.component.PatchedDataComponentMapInjection;

import java.util.Optional;

@Mixin(PatchedDataComponentMap.class)
public abstract class PatchedDataComponentMapInject implements PatchedDataComponentMapInjection {
    @Shadow private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;

    @Override
    public boolean isPatchEmpty() {
        return this.patch.isEmpty();
    }
}
