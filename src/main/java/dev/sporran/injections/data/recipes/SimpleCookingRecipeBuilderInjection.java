package dev.sporran.injections.data.recipes;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.item.ItemStack;

public interface SimpleCookingRecipeBuilderInjection {
    default void sporran$setResultStack(ItemStack result) {
        throw SporranHelper.createMixinException(ShapedRecipeBuilderInjection.class, "sporran$setResultStack");
    }
}
