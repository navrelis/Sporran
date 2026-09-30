package dev.sporran.injections.client.multiplayer.prediction;

import net.neoforged.neoforge.common.util.BlockSnapshot;
import dev.sporran.util.SporranHelper;

import net.minecraft.core.BlockPos;

public interface BlockStatePredictionHandlerInjection {
    default void retainSnapshot(BlockPos pos, BlockSnapshot snapshot) {
        throw SporranHelper.createMixinException(BlockStatePredictionHandlerInjection.class, "retainSnapshot");
    }

    interface ServerVerifiedStateInjection {
        default BlockSnapshot sporran$getSnapshot() {
            throw SporranHelper.createMixinException(BlockStatePredictionHandlerInjection.class, "sporran$getSnapshot");
        }

        default void sporran$setSnapshot(BlockSnapshot snapshot) {
            throw SporranHelper.createMixinException(BlockStatePredictionHandlerInjection.class, "sporran$setSnapshot");
        }
    }
}
