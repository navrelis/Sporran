/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.entity;

import net.minecraft.world.entity.MobSpawnType;

public interface MobInjection {
    boolean isSpawnCancelled();
    void setSpawnCancelled(boolean cancel);

    MobSpawnType getSpawnType();
}
