/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.data.tags;

import net.minecraft.data.tags.BiomeTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BiomeTagsProvider.class)
public abstract class BiomeTagsProviderInject {
    // Sporran: we have no reason to implement this
}
