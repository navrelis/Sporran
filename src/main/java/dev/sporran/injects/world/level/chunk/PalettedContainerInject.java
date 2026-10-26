/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.chunk;

import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PalettedContainer.class)
public abstract class PalettedContainerInject {
    // Sporran: Funniest shit? There's no patch here.
    //       There's just a to-do here from Forge.
}
