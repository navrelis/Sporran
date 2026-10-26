/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
