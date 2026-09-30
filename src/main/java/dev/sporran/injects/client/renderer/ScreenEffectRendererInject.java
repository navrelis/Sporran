// TRACKED HASH: 0352f8e699c8b1e9b6ac77b9927e3852ffaf311c
package dev.sporran.injects.client.renderer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateStatic;
import dev.sporran.injections.client.renderer.ScreenEffectRendererInjection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererInject implements ScreenEffectRendererInjection {
    @Shadow @Final private static ResourceLocation UNDERWATER_LOCATION;

    @Shadow
    @Nullable
    private static BlockState getViewBlockingState(Player player) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique private static final AtomicBoolean sporran$isNullResetHandled = new AtomicBoolean(false);
    @Unique private static final AtomicBoolean sporran$hasViewBlockingStateHandled = new AtomicBoolean(false);
    @Unique private static @Nullable BlockState sporran$overlayBlockState = null;
    @Unique private static @Nullable BlockPos sporran$overlayBlockPos = null;

    @WrapWithCondition(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderTex(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private static boolean sporran$tryRenderBlockOverlay(TextureAtlasSprite texture, PoseStack poseStack, @Local Player player, @Local BlockState state) {
        return !ClientHooks.renderBlockOverlay(player, poseStack, RenderBlockScreenEffectEvent.OverlayType.BLOCK, state, Objects.requireNonNullElse(sporran$overlayBlockPos, BlockPos.ZERO));
    }

    @WrapOperation(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getParticleIcon(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    private static TextureAtlasSprite sporran$tryUseCustomOverlayTexture(BlockModelShaper instance, BlockState state, Operation<TextureAtlasSprite> original, @Local(argsOnly = true) Minecraft minecraft) {
        if (sporran$overlayBlockPos != null && minecraft.level != null) {
            var data = minecraft.level.getModelDataManager().getAt(sporran$overlayBlockPos);
            var model = instance.getBlockModel(state);

            if (model.getModelData(minecraft.level, sporran$overlayBlockPos, state, data == null ? ModelData.EMPTY : data) != ModelData.EMPTY) {
                return instance.getTexture(state, minecraft.level, sporran$overlayBlockPos);
            }
        }

        return original.call(instance, state);
    }

    @WrapWithCondition(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderWater(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private static boolean sporran$checkRenderWaterOverlay(Minecraft minecraft, PoseStack poseStack, @Local Player player) {
        return !ClientHooks.renderWaterOverlay(player, poseStack);
    }

    @Definition(id = "minecraft", local = @Local(type = Minecraft.class, argsOnly = true))
    @Definition(id = "player", field = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;")
    @Definition(id = "isEyeInFluid", method = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z")
    @Definition(id = "WATER", field = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;")
    @Expression("minecraft.player.isEyeInFluid(WATER)")
    @ModifyExpressionValue(method = "renderScreenEffect", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean sporran$tryRenderCustomFluidOverlay(boolean original, @Local Player player, @Local(argsOnly = true) Minecraft mc, @Local(argsOnly = true) PoseStack poseStack) {
        if (!original) {
            if (!player.getEyeInFluidType().isAir()) {
                IClientFluidTypeExtensions.of(player.getEyeInFluidType())
                    .renderOverlay(mc, poseStack);
            }

            return false;
        }

        return true;
    }

    @WrapWithCondition(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderWater(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private static boolean sporran$checkRenderFireOverlay(Minecraft minecraft, PoseStack poseStack, @Local Player player) {
        return !ClientHooks.renderFireOverlay(player, poseStack);
    }

    @Inject(method = "getViewBlockingState", at = @At(value = "RETURN", ordinal = 0))
    private static void sporran$trySetCurrentBlockPos(Player player, CallbackInfoReturnable<BlockState> cir, @Local BlockPos.MutableBlockPos pos) {
        sporran$overlayBlockPos = pos.immutable();
    }

    @WrapMethod(method = "getViewBlockingState")
    private static BlockState sporran$funnyOverlayBlockWorkaround(Player player, Operation<BlockState> original) {
        sporran$overlayBlockPos = null;
        var state = original.call(player);
        sporran$hasViewBlockingStateHandled.set(true);
        sporran$overlayBlockState = state;

        var actualPair = getOverlayBlock(player);
        try {
            if (state == null && actualPair != null) {
                return actualPair.getLeft();
            } else if (state != null && actualPair == null) {
                return null;
            } else if (actualPair != null && state != actualPair.getLeft()) {
                return actualPair.getLeft();
            }

            return state;
        } finally {
            if (!sporran$isNullResetHandled.getAndSet(false)) {
                sporran$overlayBlockState = null;
            }

            sporran$hasViewBlockingStateHandled.set(false);
        }
    }

    @Nullable @Unique
    private static Pair<BlockState, BlockPos> getOverlayBlock(Player player) {
        // Sporran: We're doing a lot of funny workarounds here just to make @WrapMethod mixins to here work for us.
        if (!sporran$hasViewBlockingStateHandled.getAndSet(false)) {
            sporran$isNullResetHandled.set(true);
            var state = getViewBlockingState(player);
            try {
                if (state == null)
                    return null;

                return Pair.of(state, sporran$overlayBlockPos);
            } finally {
                sporran$overlayBlockState = null;
                sporran$overlayBlockPos = null;
            }
        }

        return Pair.of(sporran$overlayBlockState, sporran$overlayBlockPos);
    }

    @WrapOperation(method = "renderWater", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"))
    private static void sporran$useForgeWaterRender(int i, ResourceLocation resourceLocation, Operation<Void> original) {
        original.call(i, ScreenEffectRendererInjection.currentTexture.get());
    }

    @CreateStatic
    private static void renderFluid(Minecraft mc, PoseStack poseStack, ResourceLocation texture) {
        ScreenEffectRendererInjection.renderFluid(mc, poseStack, texture);
    }

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void sporran$setUnderwaterTexture(CallbackInfo ci) {
        ScreenEffectRendererInjection.currentTexture.set(UNDERWATER_LOCATION);
    }
}
