/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
