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
