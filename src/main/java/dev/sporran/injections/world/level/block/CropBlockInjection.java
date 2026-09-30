package dev.sporran.injections.world.level.block;

import net.minecraft.world.level.block.state.BlockState;

public interface CropBlockInjection {
    ThreadLocal<BlockState> sporran$currentState = new ThreadLocal<>();
}
