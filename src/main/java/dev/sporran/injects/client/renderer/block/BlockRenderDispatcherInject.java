// TRACKED HASH: 888fe9a96f9d5a8c2d781b57f5dd842300de67c8
/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.block;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.lighting.LightPipelineAwareModelBlockRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.injections.client.renderer.block.BlockRenderDispatcherInjection;
import dev.sporran.injections.client.renderer.block.ModelBlockRendererInjection;
import dev.sporran.helpers.BakedModelOverrides;

@Mixin(BlockRenderDispatcher.class)
public abstract class BlockRenderDispatcherInject implements BlockRenderDispatcherInjection {
    @Shadow @Final @Mutable private ModelBlockRenderer modelRenderer;

    @Shadow public abstract void renderBreakingTexture(BlockState blockState, BlockPos blockPos, BlockAndTintGetter blockAndTintGetter, PoseStack poseStack, VertexConsumer vertexConsumer);
    @Shadow public abstract void renderSingleBlock(BlockState blockState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j);

    @Shadow public abstract void renderBatched(BlockState blockState, BlockPos blockPos, BlockAndTintGetter blockAndTintGetter, PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl, RandomSource randomSource);

    @Inject(method = "<init>", at = @At("TAIL"))
    public void sporran$useForgeModelRenderer(BlockModelShaper blockModelShaper, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer, BlockColors blockColors, CallbackInfo ci) {
        this.modelRenderer = new LightPipelineAwareModelBlockRenderer(blockColors);
    }

    @Unique private final ThreadLocal<ModelData> sporran$modelData = ThreadLocal.withInitial(() -> ModelData.EMPTY);
    @Unique private final ThreadLocal<RenderType> sporran$renderType = new ThreadLocal<>();
    @Unique private final ThreadLocal<Boolean> sporran$queryModelSpecificData = ThreadLocal.withInitial(() -> true);

    @Override
    public void renderBreakingTexture(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, ModelData data) {
        sporran$modelData.set(data);
        this.renderBreakingTexture(state, pos, level, poseStack, consumer);
        sporran$modelData.remove();
    }

    @WrapOperation(method = "renderBreakingTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JI)V"))
    private void sporran$tryUseForgeTesselate(ModelBlockRenderer instance, BlockAndTintGetter blockAndTintGetter, BakedModel bakedModel, BlockState blockState, BlockPos blockPos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl, RandomSource randomSource, long l, int i, Operation<Void> original) {
        if (sporran$modelData.get() == ModelData.EMPTY) {
            original.call(instance, blockAndTintGetter, bakedModel, blockState, blockPos, poseStack, vertexConsumer, bl, randomSource, l, i);
        } else {
            ((ModelBlockRendererInjection) instance).tesselateBlock(blockAndTintGetter, bakedModel, blockState, blockPos, poseStack, vertexConsumer, bl, randomSource, l, i, sporran$modelData.get(), null);
        }
    }

    @Override
    public void renderBatched(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, ModelData modelData, RenderType renderType) {
        renderBatched(state, pos, level, poseStack, consumer, checkSides, random, modelData, renderType, true);
    }

    @Override
    public void renderBatched(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, ModelData modelData, RenderType renderType, boolean queryModelSpecificData) {
        sporran$modelData.set(modelData);
        sporran$renderType.set(renderType);
        sporran$queryModelSpecificData.set(queryModelSpecificData);
        this.renderBatched(state, pos, level, poseStack, consumer, checkSides, random);
        sporran$modelData.remove();
        sporran$renderType.remove();
        sporran$queryModelSpecificData.remove();
    }

    @WrapOperation(method = "renderBatched", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JI)V"))
    private void sporran$tryUseForgeTesselateBatched(ModelBlockRenderer instance, BlockAndTintGetter blockAndTintGetter, BakedModel bakedModel, BlockState blockState, BlockPos blockPos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl, RandomSource randomSource, long l, int i, Operation<Void> original) {
        if (sporran$modelData.get() == ModelData.EMPTY && sporran$renderType.get() == null && sporran$queryModelSpecificData.get()) {
            original.call(instance, blockAndTintGetter, bakedModel, blockState, blockPos, poseStack, vertexConsumer, bl, randomSource, l, i);
        } else {
            ((ModelBlockRendererInjection) instance).tesselateBlock(blockAndTintGetter, bakedModel, blockState, blockPos, poseStack, vertexConsumer, bl, randomSource, l, i, sporran$modelData.get(), sporran$renderType.get(), sporran$queryModelSpecificData.get());
        }
    }

    @Override
    public void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
        sporran$modelData.set(modelData);
        sporran$renderType.set(renderType);
        this.renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay);
        sporran$modelData.remove();
        sporran$renderType.remove();
    }

    @WrapOperation(method = "renderSingleBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFII)V"))
    private void sporran$tryUseForgeRenderModel(ModelBlockRenderer instance, PoseStack.Pose pose, VertexConsumer vertexConsumer, BlockState blockState, BakedModel bakedModel, float f, float g, float h, int i, int j, Operation<Void> original) {
        var modelData = sporran$modelData.get();
        var mainRenderType = sporran$renderType.get();
        boolean neoRenderTypes = BakedModelOverrides.usesNeoBlockRenderTypes(bakedModel, blockState);

        // Sporran: a model without NeoForge render types (BakedModelOverrides) renders in the block's single
        //  ItemBlockRenderTypes layer, which is the buffer vanilla already took (getRenderType(state, false) is
        //  RenderTypeHelper.getEntityRenderType of that layer), so without model data and without a render type from
        //  the caller vanilla renders the same. The branches below are unchanged; the elif compares a chunk layer with
        //  an entity render type and so was taken for every such model.
        if (!neoRenderTypes && modelData == ModelData.EMPTY && mainRenderType == null) {
            original.call(instance, pose, vertexConsumer, blockState, bakedModel, f, g, h, i, j);
            return;
        }

        var singleRenderType = ItemBlockRenderTypes.getRenderType(blockState, false);
        var existingRenderTypes = bakedModel.getRenderTypes(blockState, RandomSource.create(42), modelData).asList();

        // Sporran: was a plain getRenderTypes override check against IBakedModelExtension, true for every SimpleBakedModel,
        //  MultiPartBakedModel and WeightedBakedModel (Sporran merges getRenderTypes into them).
        if (neoRenderTypes) {
            for (RenderType renderType : existingRenderTypes) {
                if (modelData == ModelData.EMPTY)
                    original.call(instance, pose, vertexConsumer, blockState, bakedModel, f, g, h, i, j);
                else
                    ((ModelBlockRendererInjection) instance).renderModel(pose, vertexConsumer, blockState, bakedModel, f, g, h, i, j, modelData, mainRenderType != null ? mainRenderType : RenderTypeHelper.getEntityRenderType(renderType, false));
            }
        } else if (existingRenderTypes.size() == 1 && existingRenderTypes.get(0) != singleRenderType) {
            ((ModelBlockRendererInjection) instance).renderModel(pose, vertexConsumer, blockState, bakedModel, f, g, h, i, j, modelData, mainRenderType != null ? mainRenderType : RenderTypeHelper.getEntityRenderType(existingRenderTypes.get(0), false));
        } else {
            original.call(instance, pose, vertexConsumer, blockState, bakedModel, f, g, h, i, j);
        }
    }

    @ModifyReceiver(method = "renderSingleBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockEntityWithoutLevelRenderer;renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
    private BlockEntityWithoutLevelRenderer sporran$tryUseForgeRenderItem(BlockEntityWithoutLevelRenderer instance, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (IClientItemExtensions.of(itemStack) == IClientItemExtensions.DEFAULT)
            return instance;

        return IClientItemExtensions.of(itemStack).getCustomRenderer();
    }
}