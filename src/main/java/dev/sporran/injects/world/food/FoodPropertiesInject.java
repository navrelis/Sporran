// TRACKED HASH: 7db9f60a09f2e5b156013ce9fa93086ae63920c1
package dev.sporran.injects.world.food;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.world.food.FoodPropertiesBuilderInjection;
import dev.sporran.injections.world.food.FoodPropertiesInjection;
import dev.sporran.injections.world.food.FoodPropertiesPossibleEffectInjection;

import java.util.function.Supplier;

@Mixin(FoodProperties.class)
public class FoodPropertiesInject implements FoodPropertiesInjection {

    // TODO: patch in equals method?

    @Mixin(FoodProperties.PossibleEffect.class)
    public static abstract class PossibleEffectInject implements FoodPropertiesPossibleEffectInjection {
        @Shadow
        @Final
        private MobEffectInstance effect;
        private Supplier<MobEffectInstance> sporran$effectSupplier;

        public PossibleEffectInject(MobEffectInstance effect, float probability) {}

        @CreateInitializer
        public PossibleEffectInject(Supplier<MobEffectInstance> effectSupplier, float probability) {
            this((MobEffectInstance) null, probability);
            this.sporran$effectSupplier = effectSupplier;
        }

        @Override
        public void sporran$setEffectSupplier(Supplier<MobEffectInstance> effectSupplier) {
            this.sporran$effectSupplier = effectSupplier;
        }

        @Override
        public Supplier<MobEffectInstance> effectSupplier() {
            if (this.effect != null)
                this.sporran$effectSupplier = () -> this.effect;
            return sporran$effectSupplier;
        }

        @Inject(method = "effect", at = @At("HEAD"), cancellable = true)
        private void changeEffect(CallbackInfoReturnable<MobEffectInstance> cir) {
            if (this.effect == null && this.sporran$effectSupplier != null) {
                cir.setReturnValue(new MobEffectInstance(this.sporran$effectSupplier.get()));
            }
        }
    }

    @Mixin(FoodProperties.Builder.class)
    public static class BuilderInject implements FoodPropertiesBuilderInjection {

        @Shadow
        @Final
        private ImmutableList.Builder<FoodProperties.PossibleEffect> effects;

        @Override
        public FoodProperties.Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.effects.add(FoodPropertiesPossibleEffectInjection.create(effect, probability));
            return (FoodProperties.Builder) (Object) this;
        }
    }

}