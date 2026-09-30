package dev.sporran.injections.core.registries;

import net.minecraft.resources.ResourceLocation;
import dev.sporran.mixin.core.BuiltInRegistriesAccessor;

import java.util.Collections;
import java.util.Set;

public interface BuiltInRegistriesInjection {
    static Set<ResourceLocation> getVanillaRegistrationOrder() {
        return Collections.unmodifiableSet(BuiltInRegistriesAccessor.getLoaders().keySet());
    }
}
