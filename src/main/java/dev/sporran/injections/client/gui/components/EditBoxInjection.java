/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
