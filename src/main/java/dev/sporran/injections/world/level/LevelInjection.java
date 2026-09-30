package dev.sporran.injections.world.level;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nullable;

import io.github.fabricators_of_create.porting_lib.extensions.common.LevelExtensions;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import dev.sporran.util.SporranHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public interface LevelInjection {
    default ArrayList<BlockSnapshot> sporran$getCapturedBlockSnapshots() {
        throw new IllegalStateException();
    }

    default boolean sporran$getRestoringBlockSnapshots() {
        throw SporranHelper.createMixinException(LevelInjection.class, "sporran$getRestoringBlockSnapshots");
    }

    default boolean sporran$getCapturingBlockSnapshots() {
        throw SporranHelper.createMixinException(LevelInjection.class, "sporran$getCapturingBlockSnapshots");
    }

    default void sporran$setCapturingBlockSnapshots(boolean value) {
        throw SporranHelper.createMixinException(LevelInjection.class, "sporran$setCapturingBlockSnapshots");
    }

    default void sporran$setRestoringBlockSnapshots(boolean value) {
        throw SporranHelper.createMixinException(LevelInjection.class, "sporran$setRestoringBlockSnapshots");
    }

    default void setDayTimeFraction(float dayTimeFraction) {
        throw SporranHelper.createMixinException(LevelInjection.class, "setDayTimeFraction");
    }

    default float getDayTimeFraction() {
        throw SporranHelper.createMixinException(LevelInjection.class, "getDayTimeFraction");
    }

    default float getDayTimePerTick() {
        throw SporranHelper.createMixinException(LevelInjection.class, "getDayTimePerTick");
    }

    default void setDayTimePerTick(float dayTimePerTick) {
        throw SporranHelper.createMixinException(LevelInjection.class, "setDayTimePerTick");
    }

    default long advanceDaytime() {
        throw SporranHelper.createMixinException(LevelInjection.class, "advanceDaytime");
    }

    default void markAndNotifyBlock(BlockPos pos, @Nullable LevelChunk levelchunk, BlockState oldState, BlockState newState, int flags, int p_46608_) {
        ((LevelExtensions) this).port_lib$markAndNotifyBlock(pos, levelchunk, oldState, newState, flags, p_46608_);
    }

    default void addFreshBlockEntities(Collection<BlockEntity> list) {
        throw SporranHelper.createMixinException(LevelInjection.class, "addFreshBlockEntities");
    }
}
