/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.block;

import net.neoforged.neoforge.common.data.SoundDefinition;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoundDefinition.SoundType.class)
public abstract class SoundTypeInject {
    // Sporran: nothing here
}
