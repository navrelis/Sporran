package dev.sporran.injections.client;

import net.minecraft.client.MouseHandler;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(MouseHandler.class)
public interface MouseHandlerInjection {
    default double getXVelocity() {
        throw new IllegalStateException();
    }

    default double getYVelocity() {
        throw new IllegalStateException();
    }
}
