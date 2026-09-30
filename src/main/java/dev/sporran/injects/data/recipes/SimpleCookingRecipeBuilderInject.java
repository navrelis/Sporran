package dev.sporran.injects.data.recipes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.data.recipes.SimpleCookingRecipeBuilderInjection;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

@Mixin(SimpleCookingRecipeBuilder.class)
public abstract class SimpleCookingRecipeBuilderInject<T extends AbstractCookingRecipe> implements SimpleCookingRecipeBuilderInjection {
    @Shadow
    private static CookingBookCategory determineRecipeCategory(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike result) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique
    private ItemStack resultStack;
    @Unique private boolean sporran$hasResultStack = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$createResultStack(RecipeCategory category, CookingBookCategory bookCategory, ItemLike result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<T> factory, CallbackInfo ci) {
        this.resultStack = new ItemStack(result);
    }

    public SimpleCookingRecipeBuilderInject(RecipeCategory category, CookingBookCategory bookCategory, ItemLike result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<T> factory) {}

    @CreateInitializer
    public SimpleCookingRecipeBuilderInject(RecipeCategory category, CookingBookCategory bookCategory, ItemStack result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<T> factory) {
        this(category, bookCategory, result.getItem(), ingredient, experience, cookingTime, factory);
        this.resultStack = result;
        this.sporran$hasResultStack = true;
    }

    @Override
    public void sporran$setResultStack(ItemStack result) {
        this.resultStack = result;
        this.sporran$hasResultStack = true;
    }

    @CreateStatic
    private static <T extends AbstractCookingRecipe> SimpleCookingRecipeBuilder generic(Ingredient ingredient, RecipeCategory category, ItemStack result, float experience, int cookingTime, RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> factory) {
        var builder = SimpleCookingRecipeBuilder.generic(ingredient, category, result.getItem(), experience, cookingTime, cookingSerializer, factory);
        builder.sporran$setResultStack(result);
        return builder;
    }

    @CreateStatic
    private static SimpleCookingRecipeBuilder campfireCooking(Ingredient ingredient, RecipeCategory category, ItemStack result, float experience, int cookingTime) {
        var builder = SimpleCookingRecipeBuilder.campfireCooking(ingredient, category, result.getItem(), experience, cookingTime);
        builder.sporran$setResultStack(result);
        return builder;
    }

    @CreateStatic
    private static SimpleCookingRecipeBuilder blasting(Ingredient ingredient, RecipeCategory category, ItemStack result, float experience, int cookingTime) {
        var builder = SimpleCookingRecipeBuilder.blasting(ingredient, category, result.getItem(), experience, cookingTime);
        builder.sporran$setResultStack(result);
        return builder;
    }

    @CreateStatic
    private static SimpleCookingRecipeBuilder smelting(Ingredient ingredient, RecipeCategory category, ItemStack result, float experience, int cookingTime) {
        var builder = SimpleCookingRecipeBuilder.smelting(ingredient, category, result.getItem(), experience, cookingTime);
        builder.sporran$setResultStack(result);
        return builder;
    }

    @CreateStatic
    private static SimpleCookingRecipeBuilder smoking(Ingredient ingredient, RecipeCategory category, ItemStack result, float experience, int cookingTime) {
        var builder = SimpleCookingRecipeBuilder.smoking(ingredient, category, result.getItem(), experience, cookingTime);
        builder.sporran$setResultStack(result);
        return builder;
    }

    @WrapOperation(method = "save", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack sporran$tryUseResultStack(ItemLike item, Operation<ItemStack> original) {
        if (this.sporran$hasResultStack) {
            return this.resultStack;
        }

        return original.call(item);
    }
}
