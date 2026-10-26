/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.inventory;

public interface AnvilMenuInjection {
    default void setMaximumCost(long value) {
        throw new IllegalStateException();
    }
}
