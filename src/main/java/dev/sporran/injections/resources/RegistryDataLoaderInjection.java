package dev.sporran.injections.resources;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface RegistryDataLoaderInjection {
    interface RegistryDataInjection<T> {
        default Consumer<RegistryBuilder<T>> registryBuilderConsumer() {
            throw SporranHelper.createMixinException(RegistryDataInjection.class, "registryBuilderConsumer");
        }

        default void sporran$setRegistryBuilderConsumer(Consumer<RegistryBuilder<T>> builderConsumer) {
            throw SporranHelper.createMixinException(RegistryDataInjection.class, "sporran$setRegistryBuilderConsumer");
        }

        static <T> RegistryDataLoader.RegistryData<T> create(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, boolean requiredNonEmpty, Consumer<RegistryBuilder<T>> builderConsumer) {
            var registryData = new RegistryDataLoader.RegistryData<>(key, elementCodec, requiredNonEmpty);
            registryData.sporran$setRegistryBuilderConsumer(builderConsumer);
            return registryData;
        }
    }
}
