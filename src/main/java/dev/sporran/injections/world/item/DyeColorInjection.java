package dev.sporran.injections.world.item;

import dev.sporran.util.SporranHelper;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface DyeColorInjection {
    static DyeColor getColor(ItemStack stack) {
        if (stack.getItem() instanceof DyeItem)
            return ((DyeItem) stack.getItem()).getDyeColor();

        for (int x = 0; x < DyeColor.BLACK.getId(); x++) {
            var dyeColor = DyeColor.byId(x);
            if (stack.is(dyeColor.getTag()))
                return dyeColor;
        }

        return null;
    }

    default TagKey<Item> getTag() {
        throw SporranHelper.createMixinException(DyeColorInjection.class, "getTag");
    }

    default TagKey<Item> getDyedTag() {
        throw SporranHelper.createMixinException(DyeColorInjection.class, "getDyedTag");
    }
}
