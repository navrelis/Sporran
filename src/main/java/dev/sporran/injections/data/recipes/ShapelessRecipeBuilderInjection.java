package dev.sporran.injections.data.recipes;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.item.ItemStack;

public interface ShapelessRecipeBuilderInjection {
    default void sporran$setResultStack(ItemStack result) {
        throw SporranHelper.createMixinException(ShapedRecipeBuilderInjection.class, "sporran$setResultStack");
    }
}
