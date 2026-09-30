package dev.sporran.injections.world.level.biome;

import net.minecraft.world.level.levelgen.GenerationStep;
import dev.sporran.util.SporranHelper;

import java.util.Set;

public interface BiomeGenerationSettingsInjection {
    default Set<GenerationStep.Carving> getCarvingStages() {
        throw SporranHelper.createMixinException(BiomeGenerationSettingsInjection.class, "getCarvingStages");
    }
}
