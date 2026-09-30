package dev.sporran.injections.world.item;

import net.minecraft.network.chat.Style;
import dev.sporran.util.SporranHelper;

import java.util.function.UnaryOperator;

public interface RarityInjection {

    default UnaryOperator<Style> getStyleModifier() {
        throw SporranHelper.createMixinException(RarityInjection.class, "getStyleModifier");
    }

    default boolean sporran$hasCustomStyleModifier() {
        throw SporranHelper.createMixinException(RarityInjection.class, "sporran$hasCustomStyleModifier");
    }

}
