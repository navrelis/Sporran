/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.entity.boss;

import net.minecraft.world.entity.boss.EnderDragonPart;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderDragonPart.class)
public abstract class EnderDragonPartInject {
    // Sporran: by all logical means, we should implement this.
    //       however, how the hell do we do that?
}
