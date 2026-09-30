package dev.sporran.injections.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import dev.sporran.util.SporranHelper;

public interface BlockRenderDispatcherInjection {
    default void renderBreakingTexture(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, ModelData data) {
        throw SporranHelper.createMixinException(BlockRenderDispatcherInjection.class, "renderBreakingTexture");
    }

    default void renderBatched(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, ModelData modelData, RenderType renderType) {
        throw SporranHelper.createMixinException(BlockRenderDispatcherInjection.class, "renderBatched");
    }

    default void renderBatched(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, ModelData modelData, RenderType renderType, boolean queryModelSpecificData) {
        throw SporranHelper.createMixinException(BlockRenderDispatcherInjection.class, "renderBatched");
    }

    default void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
        throw SporranHelper.createMixinException(BlockRenderDispatcherInjection.class, "renderSingleBlock");
    }
}
