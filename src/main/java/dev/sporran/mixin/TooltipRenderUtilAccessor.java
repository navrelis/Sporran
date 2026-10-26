/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TooltipRenderUtil.class)
public interface TooltipRenderUtilAccessor {
    @Invoker
    static void callRenderFrameGradient(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int topColor, int bottomColor) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static void callRenderVerticalLine(GuiGraphics guiGraphics, int x, int y, int length, int z, int color) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static void callRenderVerticalLineGradient(GuiGraphics guiGraphics, int x, int y, int length, int z, int topColor, int bottomColor) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static void callRenderHorizontalLine(GuiGraphics guiGraphics, int x, int y, int length, int z, int color) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static void callRenderRectangle(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int color) {
        throw new UnsupportedOperationException();
    }
}
