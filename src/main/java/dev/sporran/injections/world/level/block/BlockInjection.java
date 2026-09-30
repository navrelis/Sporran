package dev.sporran.injections.world.level.block;

import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface BlockInjection {
    default void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        throw SporranHelper.createMixinException(BlockInjection.class, "initializeClient");
    }
}
