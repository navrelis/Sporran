package dev.sporran.injections.world.entity.item;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

public interface ItemEntityInjection {
    default int sporran$getLifespan() {
        throw SporranHelper.createMixinException(ItemEntityInjection.class, "sporran$getLifespan");
    }

    @Nullable
    default UUID getTarget() {
        throw SporranHelper.createMixinException(ItemEntityInjection.class, "getTarget");
    }
}
