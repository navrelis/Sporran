package dev.sporran.injects.world.item.crafting;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.util.SporranHelper;

@Mixin(Recipe.class)
public interface RecipeInject<T extends RecipeInput> {
    //@CreateStatic // Sporran TODO: how
    //Codec<Optional<WithConditions<Recipe<?>>>> CONDITIONAL_CODEC = RecipeInjection.CONDITIONAL_CODEC;

    // Sporran: getCraftingRemainingItem should be handled by Fabric API. hopefully.

    @Inject(method = "method_31583", at = @At("HEAD"), cancellable = true)
    private static void sporran$checkHasNoItems(Ingredient ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (SporranHelper.INSTANCE.hasMethodOverride(ingredient.getClass(), Ingredient.class, "hasNoItems")) {
            cir.setReturnValue(ingredient.hasNoItems());
        }
    }
}
