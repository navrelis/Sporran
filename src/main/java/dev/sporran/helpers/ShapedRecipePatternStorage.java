package dev.sporran.helpers;

import dev.sporran.helpers.mixin.CreateStatic;

public class ShapedRecipePatternStorage {
    static int maxWidth = 3;
    static int maxHeight = 3;

    public static int getMaxWidth() {
        return maxWidth;
    }

    public static int getMaxHeight() {
        return maxHeight;
    }

    public static void setCraftingSize(int width, int height) {
        if (maxWidth < width) maxWidth = width;
        if (maxHeight < height) maxHeight = height;
    }
}
