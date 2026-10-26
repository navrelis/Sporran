/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity;

import net.minecraft.world.entity.Shearable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Shearable.class)
public interface ShearableInject {
    // Sporran: bruh
}
