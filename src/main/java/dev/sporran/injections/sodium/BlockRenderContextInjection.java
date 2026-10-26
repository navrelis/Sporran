/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.sodium;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.model.data.ModelData;

public interface BlockRenderContextInjection {
    default ModelData sporran$getModelData(BlockPos pos) {
        return ModelData.EMPTY;
    }
}
