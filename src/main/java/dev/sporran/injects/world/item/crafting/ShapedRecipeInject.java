// TRACKED HASH: 6b717e608f1a84947c867222d601c601a3b66507
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.world.item.crafting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeInject {
    @ModifyReturnValue(method = "method_31585", at = @At("RETURN"))
    private static boolean checkHasNoItems(boolean original, Ingredient ingredient) {
        if (!original) {
            return ingredient.hasNoItems();
        }
        return true;
    }
}