package dev.sporran.injections.world.level.chunk.status;

import java.util.EnumSet;

import dev.sporran.util.SporranHelper;

import net.minecraft.world.level.levelgen.Heightmap;

public interface ChunkStatusInjection {
    default EnumSet<Heightmap.Types> getChunkSaveHeightmaps() {
        throw SporranHelper.createMixinException(ChunkStatusInjection.class, "getChunkSaveHeightmaps");
    }
}
