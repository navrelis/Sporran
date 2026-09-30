package dev.sporran.injects.data.recipes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.data.recipes.ShapelessRecipeBuilderInjection;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@Mixin(ShapelessRecipeBuilder.class)
public abstract class ShapelessRecipeBuilderInject implements ShapelessRecipeBuilderInjection {
    @Unique
    private ItemStack resultStack;
    @Unique private boolean sporran$hasResultStack = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$createResultStack(RecipeCategory category, ItemLike result, int count, CallbackInfo ci) {
        this.resultStack = new ItemStack(result, count);
    }

    public ShapelessRecipeBuilderInject(RecipeCategory category, ItemLike result, int count) {}

    @CreateInitializer
    public ShapelessRecipeBuilderInject(RecipeCategory category, ItemStack result) {
        this(category, result.getItem(), result.getCount());
        this.resultStack = result;
        this.sporran$hasResultStack = true;
    }

    @Override
    public void sporran$setResultStack(ItemStack result) {
        this.resultStack = result;
        this.sporran$hasResultStack = true;
    }

    @CreateStatic
    private static ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
        var builder = new ShapelessRecipeBuilder(category, result.getItem(), result.getCount());
        builder.sporran$setResultStack(result);
        return builder;
    }

    @WrapOperation(method = "save", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack sporran$tryUseResultStack(ItemLike item, int count, Operation<ItemStack> original) {
        if (this.sporran$hasResultStack) {
            return this.resultStack;
        }

        return original.call(item, count);
    }
}
