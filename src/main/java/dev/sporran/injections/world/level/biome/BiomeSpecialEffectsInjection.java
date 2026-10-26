/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.biome;

import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.HashMap;
import java.util.Map;

// fuck it i'm making it necessary
public interface BiomeSpecialEffectsInjection {
    interface GrassColorModifierInjection {
        Map<String, BiomeSpecialEffects.GrassColorModifier> BY_NAME = new HashMap<>();

        static BiomeSpecialEffects.GrassColorModifier byName(String name) {
            return BY_NAME.get(name);
        }

        @FunctionalInterface
        public interface ColorModifier {
            int modifyGrassColor(double x, double z, int color);
        }

        default void setDelegate(ColorModifier delegate) {
            throw new IllegalStateException();
        }
    }
}
