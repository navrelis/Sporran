package dev.sporran.injects.world.item.crafting;

import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.ShapedRecipePatternStorage;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.world.item.crafting.ShapedRecipePatternInjection;

@Mixin(ShapedRecipePattern.class)
public class ShapedRecipePatternInject implements ShapedRecipePatternInjection {
    private static int maxWidth = 3;
    private static int maxHeight = 3;

    @CreateStatic
    private static int getMaxWidth() {
        return ShapedRecipePatternStorage.getMaxWidth();
    }

    @CreateStatic
    private static int getMaxHeight() {
        return ShapedRecipePatternStorage.getMaxHeight();
    }

    @CreateStatic
    private static void setCraftingSize(int width, int height) {
        ShapedRecipePatternStorage.setCraftingSize(width, height);
        if (maxWidth < width) maxWidth = width;
        if (maxHeight < height) maxHeight = height;
    }
}
