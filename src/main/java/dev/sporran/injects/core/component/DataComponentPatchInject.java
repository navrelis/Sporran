/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.core.component;

import net.minecraft.core.component.DataComponentPatch;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataComponentPatch.class)
public abstract class DataComponentPatchInject {
    // Sporran: we don't have to implement component validation
}
