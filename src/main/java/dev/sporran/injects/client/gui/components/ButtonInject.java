// TRACKED HASH: ef9fd99db3d48c0d70bf22631982a82f03e8d541
package dev.sporran.injects.client.gui.components;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.client.gui.components.ButtonBuilderInjection;
import dev.sporran.injections.client.gui.components.ButtonInjection;
import dev.sporran.mixin.client.gui.components.ButtonBuilderAccessor;

import java.util.function.Function;

@Mixin(Button.class)
public abstract class ButtonInject extends AbstractButton implements ButtonInjection {
    protected ButtonInject(int x, int y, int width, int height, Component message, Button.OnPress onPress, Button.CreateNarration createNarration) {
        super(x, y, width, height, message);
    }

    @CreateInitializer
    protected ButtonInject(Button.Builder builder) {
        this(((ButtonBuilderAccessor) builder).getX(), ((ButtonBuilderAccessor) builder).getY(), ((ButtonBuilderAccessor) builder).getWidth(), ((ButtonBuilderAccessor) builder).getHeight(), ((ButtonBuilderAccessor) builder).getMessage(), ((ButtonBuilderAccessor) builder).getOnPress(), ((ButtonBuilderAccessor) builder).getCreateNarration());
        this.setTooltip(((ButtonBuilderAccessor) builder).getTooltip());
    }

    @Mixin(Button.Builder.class)
    public static class ButtonBuilderInject implements ButtonBuilderInjection {
        @Override
        public Button build(Function<Button.Builder, Button> builder) {
            return ButtonBuilderInjection.super.build(builder);
        }
    }
}