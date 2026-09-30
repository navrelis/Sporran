package dev.sporran.injections.client;

import dev.sporran.util.SporranHelper;

public interface OptionsInjection {
    default void load(boolean limited) {
        throw SporranHelper.createMixinException(OptionsInjection.class, "load");
    }
}
