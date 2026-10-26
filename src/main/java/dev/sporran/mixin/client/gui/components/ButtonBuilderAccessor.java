/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.client.gui.components;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.Builder.class)
public interface ButtonBuilderAccessor {
    @Accessor
    Component getMessage();

    @Accessor
    Button.OnPress getOnPress();

    @Accessor
    Tooltip getTooltip();

    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor("width")
    int getWidth();

    @Accessor("height")
    int getHeight();

    @Accessor
    Button.CreateNarration getCreateNarration();
}
