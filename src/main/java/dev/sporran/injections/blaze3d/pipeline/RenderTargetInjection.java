/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
