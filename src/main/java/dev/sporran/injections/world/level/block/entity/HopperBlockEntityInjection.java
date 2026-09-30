package dev.sporran.injections.world.level.block.entity;

public interface HopperBlockEntityInjection {
    default long getLastUpdateTime() {
        throw new IllegalStateException();
    }
}
