package dev.sporran.injections.blaze3d.pipeline;

import dev.sporran.util.SporranHelper;

public interface RenderTargetInjection {
    default void enableStencil() {
        throw SporranHelper.createMixinException(RenderTargetInjection.class, "enableStencil");
    }

    default void disableStencil() {
        throw SporranHelper.createMixinException(RenderTargetInjection.class, "disableStencil");
    }

    default boolean isStencilEnabled() {
        throw SporranHelper.createMixinException(RenderTargetInjection.class, "isStencilEnabled");
    }
}
