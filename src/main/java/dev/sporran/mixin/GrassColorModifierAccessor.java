/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
public interface GrassColorModifierAccessor {
    @Invoker("<init>")
    static BiomeSpecialEffects.GrassColorModifier createGrassColorModifier(String name, int id, String string2) {
        throw new UnsupportedOperationException();
    }

    @Accessor("$VALUES")
    static BiomeSpecialEffects.GrassColorModifier[] getValues() {
        throw new IllegalStateException();
    }

    @Accessor("$VALUES")
    @Mutable
    static void setValues(BiomeSpecialEffects.GrassColorModifier[] values) {
        throw new IllegalStateException();
    }
}
