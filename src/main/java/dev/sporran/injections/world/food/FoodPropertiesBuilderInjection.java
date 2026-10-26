/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

public interface FoodPropertiesBuilderInjection {
    default FoodProperties.Builder effect(Supplier<MobEffectInstance> effect, float probability) {
        throw new IllegalStateException();
    }
}
