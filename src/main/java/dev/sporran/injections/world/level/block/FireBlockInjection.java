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
