/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.chunk;

import dev.sporran.util.SporranHelper;

import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;

public interface RenderRegionCacheInjection {
    default void sporran$setNullForEmpty(boolean value) {
        throw SporranHelper.createMixinException(RenderRegionCacheInjection.class, "sporran$setNullForEmpty");
    }

    default RenderChunkRegion createRegion(Level level, SectionPos pos, boolean nullForEmpty) {
        throw SporranHelper.createMixinException(RenderRegionCacheInjection.class, "createRegion");
    }
}
