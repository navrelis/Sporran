package dev.sporran.injections.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import dev.sporran.util.SporranHelper;

public interface ArrowItemInjection {
    default boolean isInfinite(ItemStack stack, ItemStack bow, LivingEntity entity) {
        throw SporranHelper.createMixinException(ArrowItemInjection.class, "isInfinite");
    }
}
