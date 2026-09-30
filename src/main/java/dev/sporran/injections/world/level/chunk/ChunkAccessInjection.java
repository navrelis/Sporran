package dev.sporran.injections.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FabricInjectedInterface(ChunkAccess.class)
public interface ChunkAccessInjection {
    default void findBlocks(BiPredicate<BlockState, BlockPos> fineFilter, BiConsumer<BlockPos, BlockState> output) {
        throw SporranHelper.createMixinException(ChunkAccessInjection.class, "findBlocks");
    }

    default void findBlocks(Predicate<BlockState> predicate, BiPredicate<BlockState, BlockPos> fineFilter, BiConsumer<BlockPos, BlockState> output) {
        throw SporranHelper.createMixinException(ChunkAccessInjection.class, "findBlocks");
    }

    @Nullable
    default CompoundTag writeAttachmentsToNBT(HolderLookup.Provider provider) {
        throw SporranHelper.createMixinException(ChunkAccessInjection.class, "writeAttachmentsToNBT");
    }

    default void readAttachmentsFromNBT(HolderLookup.Provider provider, CompoundTag tag) {
        throw SporranHelper.createMixinException(ChunkAccessInjection.class, "readAttachmentsFromNBT");
    }

    default AttachmentHolder.AsField getAttachmentHolder() {
        throw SporranHelper.createMixinException(ChunkAccessInjection.class, "getAttachmentHolder");
    }

    @Nullable
    default Level getLevel() {
        return null;
    }
}
