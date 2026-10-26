/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block;

public interface PoweredRailBlockInjection {
    default void registerDefaultState() {
        throw new IllegalStateException();
    }

    default boolean isActivatorRail() {
        throw new IllegalStateException();
    }

    default void sporran$setActivator(boolean value) {
        throw new IllegalStateException();
    }
}
