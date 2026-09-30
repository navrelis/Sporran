package dev.sporran.injections.world.flag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import dev.sporran.util.SporranHelper;

import java.util.Map;

public interface FeatureFlagRegistryInjection {
    default FeatureFlag getFlag(ResourceLocation id) {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "getFlag");
    }

    default Map<ResourceLocation, FeatureFlag> getAllFlags() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "getAllFlags");
    }

    default boolean hasAnyModdedFlags() {
        throw SporranHelper.createMixinException(FeatureFlagInjection.class, "hasAnyModdedFlags");
    }

    interface BuilderInjection {
        default FeatureFlag create(ResourceLocation id, boolean modded) {
            throw SporranHelper.createMixinException(FeatureFlagRegistryInjection.BuilderInjection.class, "create");
        }
    }
}
