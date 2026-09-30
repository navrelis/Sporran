package dev.sporran.injections.client.gui.components;

import dev.sporran.util.SporranHelper;

public interface EditBoxInjection {
    default void setTextShadow(boolean textShadow) {
        throw SporranHelper.createMixinException(EditBoxInjection.class, "setTextShadow");
    }

    default boolean getTextShadow() {
        throw SporranHelper.createMixinException(EditBoxInjection.class, "getTextShadow");
    }
}
