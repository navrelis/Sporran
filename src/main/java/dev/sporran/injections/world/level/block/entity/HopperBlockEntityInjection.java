/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block.entity;

public interface HopperBlockEntityInjection {
    default long getLastUpdateTime() {
        throw new IllegalStateException();
    }
}
