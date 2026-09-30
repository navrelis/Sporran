package dev.sporran.injections.core;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(MappedRegistry.class)
public interface MappedRegistryInjection<T> {
    default void unfreeze() {
        throw new IllegalStateException();
    }

    default Holder.Reference<T> register(int id, ResourceKey<T> key, T value, RegistrationInfo info) {
        throw new IllegalStateException();
    }

    default void registerIdMapping(ResourceKey<T> key, int id) {
        throw SporranHelper.createMixinException(MappedRegistryInjection.class, "registerIdMapping");
    }

    default void clear(boolean full) {
        throw SporranHelper.createMixinException(MappedRegistryInjection.class, "clear");
    }

    // Sporran: mainly used to call the super, honestly.
    default void sporran$clear(boolean full) {}
}
