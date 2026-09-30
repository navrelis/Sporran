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
