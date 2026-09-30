package dev.sporran.injections.client.renderer.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import net.neoforged.neoforge.client.model.data.ModelData;
import dev.sporran.util.SporranHelper;

public interface RenderChunkRegionInjection {
    default void sporran$setModelDataSnapshot(Long2ObjectFunction<ModelData> modelDataSnapshot) {
        throw SporranHelper.createMixinException(RenderChunkRegionInjection.class, "sporran$setModelDataSnapshot");
    }
}
