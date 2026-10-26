// TRACKED HASH: 8163397c1ab77801d2732a96d6b5fb5aeec201ba
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.client.gui.screens.inventory.tooltip.TooltipRenderUtilInjection;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilInject implements TooltipRenderUtilInjection {
    @CreateStatic
    private static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom) {
        TooltipRenderUtilInjection.renderTooltipBackground(guiGraphics, x, y, width, height, z, backgroundTop, backgroundBottom, borderTop, borderBottom);
    }
}