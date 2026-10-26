/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import dev.sporran.util.SporranHelper;

public interface ScreenInjection {
    default void sporran$addEventWidget(GuiEventListener b) {
        throw new IllegalStateException();
    }
    default Minecraft getMinecraft() {
        throw SporranHelper.createMixinException(ScreenInjection.class, "getMinecraft");
    }
}
