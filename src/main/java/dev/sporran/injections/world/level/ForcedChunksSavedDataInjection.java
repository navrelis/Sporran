package dev.sporran.injections.world.level;

import io.github.fabricators_of_create.porting_lib.chunk.loading.extensions.ForcedChunksSavedDataExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ForcedChunksSavedData;
import net.neoforged.neoforge.common.world.chunk.ForcedChunkManager;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import java.util.UUID;

@FabricInjectedInterface(ForcedChunksSavedData.class)
public interface ForcedChunksSavedDataInjection extends ForcedChunksSavedDataExtension {
    default ForcedChunkManager.TicketTracker<BlockPos> neo$getBlockForcedChunks() {
        throw SporranHelper.createMixinException(ForcedChunksSavedDataInjection.class, "neo$getBlockForcedChunks");
    }

    default ForcedChunkManager.TicketTracker<UUID> neo$getEntityForcedChunks() {
        throw SporranHelper.createMixinException(ForcedChunksSavedDataInjection.class, "neo$getEntityForcedChunks");
    }
}
