/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.data.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(BlockLootSubProvider.class)
public interface BlockLootSubProviderInjection {
    default Iterable<Block> getKnownBlocks() {
        throw new IllegalStateException();
    }
}
