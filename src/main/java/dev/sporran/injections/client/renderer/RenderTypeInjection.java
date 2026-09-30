package dev.sporran.injections.client.renderer;

import net.minecraft.client.renderer.RenderType;
import dev.sporran.processor.FabricInjectedInterface;

import java.util.concurrent.atomic.AtomicInteger;

@FabricInjectedInterface(RenderType.class)
public interface RenderTypeInjection {
    AtomicInteger sporran$loadedChunkLayers = new AtomicInteger(0);

    default int getChunkLayerId() {
        throw new RuntimeException("mixin.");
    }

    default void setChunkLayerId(int id) {
        throw new RuntimeException("mixin.");
    }

    static void sporran$initLoadedChunkLayers() {
        var layers = RenderType.chunkBufferLayers();
        if (layers.size() == sporran$loadedChunkLayers.get())
            return;

        var i = 0;
        for (var layer : layers)
            layer.setChunkLayerId(i++);

        sporran$loadedChunkLayers.set(layers.size());
    }
}
