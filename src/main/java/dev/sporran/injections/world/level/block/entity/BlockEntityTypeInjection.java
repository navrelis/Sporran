/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.level.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import dev.sporran.util.SporranHelper;

import java.util.Set;

public interface BlockEntityTypeInjection<T extends BlockEntity> {
    default Set<Block> getValidBlocks() {
        throw SporranHelper.createMixinException(BlockEntityTypeInjection.class, "getValidBlocks");
    }
}
