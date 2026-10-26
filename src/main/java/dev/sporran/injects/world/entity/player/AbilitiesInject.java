/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.player;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Abilities;

@Mixin(Abilities.class)
public abstract class AbilitiesInject {
    // Sporran: nothing to do here
}
