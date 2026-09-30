// TRACKED HASH: cdaa6bc13984c0e47e37365d1be1e2f32b4453e0
package dev.sporran.injects.client.gui.screens.recipebook;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.world.inventory.RecipeBookMenuInjection;
import dev.sporran.util.SporranHelper;

import java.util.List;

@Mixin(RecipeBookComponent.class)
public class RecipeBookComponentInject {
    @Shadow protected RecipeBookMenu<?, ?> menu;

    @WrapOperation(method = "initVisuals", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/RecipeBookCategories;getCategories(Lnet/minecraft/world/inventory/RecipeBookType;)Ljava/util/List;"))
    public List<RecipeBookCategories> sporran$getMenuRecipeCategories(RecipeBookType recipeBookType, Operation<List<RecipeBookCategories>> original) {
        if (SporranHelper.INSTANCE.hasMethodOverride(this.menu.getClass(), RecipeBookMenu.class, "getRecipeBookCategories")) {
            return ((RecipeBookMenuInjection) this.menu).getRecipeBookCategories();
        }

        return original.call(recipeBookType);
    }
}