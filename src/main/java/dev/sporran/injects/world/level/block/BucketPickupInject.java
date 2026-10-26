// TRACKED HASH: 91406160d3c0d0a33b7e173e57d20f7690d0528f
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.level.block;

import net.minecraft.world.level.block.BucketPickup;
import net.neoforged.neoforge.common.extensions.IBucketPickupExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BucketPickup.class)
public interface BucketPickupInject extends IBucketPickupExtension {
}