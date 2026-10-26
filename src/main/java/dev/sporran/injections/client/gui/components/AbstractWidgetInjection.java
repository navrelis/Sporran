/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.components;

import dev.sporran.util.SporranHelper;

public interface AbstractWidgetInjection {
    int UNSET_FG_COLOR = -1;

    default int getFGColor() {
        throw SporranHelper.createMixinException(AbstractWidgetInjection.class, "getFGColor");
    }

    default void setFGColor(int color) {
        throw SporranHelper.createMixinException(AbstractWidgetInjection.class, "setFGColor");
    }

    default void clearFGColor() {
        throw SporranHelper.createMixinException(AbstractWidgetInjection.class, "clearFGColor");
    }
}
