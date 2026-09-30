package dev.sporran.injections.client.model;

import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;
import net.neoforged.neoforge.client.entity.animation.json.AnimationLoader;
import dev.sporran.util.SporranHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;

public interface HierarchicalModelInjection {
    static AnimationHolder getAnimation(ResourceLocation key) {
        return AnimationLoader.INSTANCE.getAnimationHolder(key);
    }

    default void animate(AnimationState state, AnimationHolder animation, float ageInTicks) {
        throw SporranHelper.createMixinException(HierarchicalModelInjection.class, "animate");
    }

    default void animateWalk(AnimationHolder animation, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor) {
        throw SporranHelper.createMixinException(HierarchicalModelInjection.class, "animateWalk");
    }

    default void animate(AnimationState state, AnimationHolder animation, float ageInTicks, float speed) {
        throw SporranHelper.createMixinException(HierarchicalModelInjection.class, "animate");
    }

    default void applyStatic(AnimationHolder animation) {
        throw SporranHelper.createMixinException(HierarchicalModelInjection.class, "applyStatic");
    }
}
