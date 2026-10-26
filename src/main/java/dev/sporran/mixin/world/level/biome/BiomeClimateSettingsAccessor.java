/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.world.level.biome;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.ClimateSettings.class)
public interface BiomeClimateSettingsAccessor {
    @Invoker("<init>")
    static Biome.ClimateSettings createClimateSettings(boolean bl, float f, Biome.TemperatureModifier temperatureModifier, float g) {
        throw new UnsupportedOperationException();
    }
}
