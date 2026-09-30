package dev.sporran.injections.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(Minecraft.class)
public interface MinecraftInjection {
    default ItemColors getItemColors() {
        throw new IllegalStateException();
    }
}
