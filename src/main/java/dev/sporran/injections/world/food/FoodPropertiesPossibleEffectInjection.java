package dev.sporran.injections.world.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.function.Supplier;

@FabricInjectedInterface(FoodProperties.PossibleEffect.class)
public interface FoodPropertiesPossibleEffectInjection {

    static FoodProperties.PossibleEffect create(Supplier<MobEffectInstance> effectSupplier, float probability) {
        var possibleEffect = new FoodProperties.PossibleEffect(null, probability);
        possibleEffect.sporran$setEffectSupplier(effectSupplier);
        return possibleEffect;
    }

    default void sporran$setEffectSupplier(Supplier<MobEffectInstance> effectSupplier) {
        throw SporranHelper.createMixinException(FoodPropertiesPossibleEffectInjection.class, "sporran$setEffectSupplier");
    }

    default Supplier<MobEffectInstance> effectSupplier() {
        throw SporranHelper.createMixinException(FoodPropertiesPossibleEffectInjection.class, "effectSupplier");
    }
}
