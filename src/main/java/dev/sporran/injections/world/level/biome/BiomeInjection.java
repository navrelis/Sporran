/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public interface BiomeInjection {
    default ModifiableBiomeInfo modifiableBiomeInfo() {
        throw new IllegalStateException();
    }

    default Biome.ClimateSettings getModifiedClimateSettings() {
        throw new IllegalStateException();
    }

    default BiomeSpecialEffects getModifiedSpecialEffects() {
        throw new IllegalStateException();
    }
}
