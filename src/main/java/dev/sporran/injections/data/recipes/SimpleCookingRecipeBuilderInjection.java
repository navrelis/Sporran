/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.data.recipes;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.item.ItemStack;

public interface SimpleCookingRecipeBuilderInjection {
    default void sporran$setResultStack(ItemStack result) {
        throw SporranHelper.createMixinException(ShapedRecipeBuilderInjection.class, "sporran$setResultStack");
    }
}
