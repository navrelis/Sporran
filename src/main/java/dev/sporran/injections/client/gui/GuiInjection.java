/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
