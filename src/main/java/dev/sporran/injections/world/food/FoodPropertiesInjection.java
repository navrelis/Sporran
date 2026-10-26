/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.food;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.function.Supplier;

public interface FoodPropertiesInjection {
    default void sporran$setDeferredEffects(List<Pair<Supplier<MobEffectInstance>, Float>> deferredEffects) {
        throw new IllegalStateException();
    }
}
