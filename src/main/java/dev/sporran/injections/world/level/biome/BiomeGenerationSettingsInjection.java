/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.biome;

import net.minecraft.world.level.levelgen.GenerationStep;
import dev.sporran.util.SporranHelper;

import java.util.Set;

public interface BiomeGenerationSettingsInjection {
    default Set<GenerationStep.Carving> getCarvingStages() {
        throw SporranHelper.createMixinException(BiomeGenerationSettingsInjection.class, "getCarvingStages");
    }
}
