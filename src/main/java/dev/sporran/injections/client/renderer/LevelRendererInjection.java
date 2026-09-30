package dev.sporran.injections.client.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import dev.sporran.util.SporranHelper;

import java.util.function.Consumer;

public interface LevelRendererInjection {
    default Frustum getFrustum() {
        throw SporranHelper.createMixinException(LevelRendererInjection.class, "getFrustum");
    }

    default int getTicks() {
        throw SporranHelper.createMixinException(LevelRendererInjection.class, "getTicks");
    }

    default void iterateVisibleBlockEntities(Consumer<BlockEntity> blockEntityConsumer) {
        throw SporranHelper.createMixinException(LevelRendererInjection.class, "blockEntityConsumer");
    }

    default void requestOutlineEffect() {
        throw SporranHelper.createMixinException(LevelRendererInjection.class, "requestOutlineEffect");
    }
}
