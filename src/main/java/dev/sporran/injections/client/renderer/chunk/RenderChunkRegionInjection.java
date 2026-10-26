/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import net.neoforged.neoforge.client.model.data.ModelData;
import dev.sporran.util.SporranHelper;

public interface RenderChunkRegionInjection {
    default void sporran$setModelDataSnapshot(Long2ObjectFunction<ModelData> modelDataSnapshot) {
        throw SporranHelper.createMixinException(RenderChunkRegionInjection.class, "sporran$setModelDataSnapshot");
    }
}
