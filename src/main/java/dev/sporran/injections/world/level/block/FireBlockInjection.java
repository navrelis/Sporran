/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import dev.sporran.util.SporranHelper;

public interface FireBlockInjection {
    default boolean canCatchFire(BlockGetter level, BlockPos pos, Direction face) {
        throw SporranHelper.createMixinException(FireBlockInjection.class, "canCatchFire");
    }
}
