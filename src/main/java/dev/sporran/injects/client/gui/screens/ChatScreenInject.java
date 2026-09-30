package dev.sporran.injects.client.gui.screens;

import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChatScreen.class)
public abstract class ChatScreenInject {
    // Sporran: personally don't think this is needed.
}
