/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.npc;

import net.minecraft.world.entity.npc.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CatSpawner.class)
public abstract class CatSpawnerInject {
    // Sporran: this is a bugfix inject, not needed for us.
}
