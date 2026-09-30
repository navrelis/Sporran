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
