package dev.sporran.injections.world.item;

import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import net.minecraft.world.item.ItemDisplayContext;

public interface ItemDisplayContextInjection {
    default boolean isModded() {
        return false;
    }

    default @Nullable ItemDisplayContext fallback() {
        throw SporranHelper.createMixinException(ItemDisplayContextInjection.class, "fallback");
    }

    default void sporran$markModded() {
        throw SporranHelper.createMixinException(ItemDisplayContextInjection.class, "sporran$markModded");
    }

    default void sporran$setFallback(ItemDisplayContext fallback) {
        throw SporranHelper.createMixinException(ItemDisplayContextInjection.class, "sporran$setFallback");
    }
}
