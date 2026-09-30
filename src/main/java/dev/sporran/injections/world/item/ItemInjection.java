package dev.sporran.injections.world.item;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface ItemInjection {
    default void initializeClient(Consumer<IClientItemExtensions> consumer) {
        throw SporranHelper.createMixinException(ItemInjection.class, "initializeClient");
    }

    default void modifyDefaultComponentsFrom(DataComponentPatch patch) {
        throw SporranHelper.createMixinException(ItemInjection.class, "modifyDefaultComponentsFrom");
    }

    interface TooltipContextInjection {
        default Level level() {
            throw SporranHelper.createMixinException(ItemInjection.class, "level");
        }
    }
}
