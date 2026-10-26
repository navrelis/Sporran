/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.data.DataGenerator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataGenerator.class)
public interface DataGeneratorAccessor {
}
