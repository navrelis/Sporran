package dev.sporran.injections.client.gui;

import dev.sporran.util.SporranHelper;

import net.minecraft.client.gui.GuiGraphics;

public interface GuiInjection {
    default void renderSelectedItemName(GuiGraphics guiGraphics, int yShift) {
        throw SporranHelper.createMixinException(GuiInjection.class, "renderSelectedItemName");
    }

    default void initModdedOverlays() {
        throw SporranHelper.createMixinException(GuiInjection.class, "initModdedOverlays");
    }

    default int getLayerCount() {
        throw SporranHelper.createMixinException(GuiInjection.class, "getLayerCount");
    }
}
