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
