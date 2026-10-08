package dev.sporran.mixin.compat.sodium;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AmbientOcclusionMode;
import net.caffeinemc.mods.sodium.client.services.SodiumModelData;
import net.caffeinemc.mods.sodium.client.util.DirectionUtil;
import net.caffeinemc.mods.sodium.fabric.block.FabricBlockAccess;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

@IfModLoaded("sodium")
@Mixin(FabricBlockAccess.class)
public abstract class FabricBlockAccessMixin {
    // Sporran: Sodium's mesher asks these for every block, face and model on all of its worker threads, resolve the
    //  override checks only once.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_LIGHT_EMISSION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Block.class, "getLightEmission", BlockState.class, BlockAndTintGetter.class, BlockPos.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$HIDES_NEIGHBOR_FACE_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Block.class, "hidesNeighborFace", BlockState.class, BlockGetter.class, BlockPos.class, BlockState.class, Direction.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SUPPORTS_EXTERNAL_FACE_HIDING_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Block.class, "supportsExternalFaceHiding", BlockState.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SHOULD_DISPLAY_FLUID_OVERLAY_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Block.class, "shouldDisplayFluidOverlay", BlockState.class, BlockAndTintGetter.class, BlockPos.class, FluidState.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$USE_AMBIENT_OCCLUSION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(BakedModel.class, "useAmbientOcclusion", BlockState.class, ModelData.class, RenderType.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$HAS_CUSTOM_OUTLINE_RENDERING_OVERRIDE = SporranHelper.MethodOverrideCheck.of(BlockEntity.class, "hasCustomOutlineRendering", Player.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SHOULD_HIDE_ADJACENT_FLUID_FACE_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Block.class, "shouldHideAdjacentFluidFace", BlockState.class, Direction.class, FluidState.class);

    @Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoLightEmission(BlockState state, BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (sporran$GET_LIGHT_EMISSION_OVERRIDE.test(state.getBlock().getClass())) {
            cir.setReturnValue(state.getLightEmission(level, pos));
        }
    }

    @Inject(method = "shouldSkipRender", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoSkipRender(BlockGetter level, BlockState selfState, BlockState otherState, BlockPos selfPos, BlockPos otherPos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$HIDES_NEIGHBOR_FACE_OVERRIDE.test(otherState.getBlock().getClass()) ||
            sporran$SUPPORTS_EXTERNAL_FACE_HIDING_OVERRIDE.test(selfState.getBlock().getClass())
        ) {
            cir.setReturnValue(otherState.hidesNeighborFace(level, otherPos, selfState, DirectionUtil.getOpposite(facing)) && selfState.supportsExternalFaceHiding());
        }
    }

    @Inject(method = "shouldShowFluidOverlay", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoFluidOverlayCheck(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$SHOULD_DISPLAY_FLUID_OVERLAY_OVERRIDE.test(state.getBlock().getClass())) {
            cir.setReturnValue(state.shouldDisplayFluidOverlay(level, pos, fluidState));
        }
    }

    @Redirect(method = "getNormalVectorShade", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/fabric/block/FabricBlockAccess;normalShade(Lnet/minecraft/world/level/BlockAndTintGetter;FFFZ)F"))
    private float sporran$tryUseNeoNormalShade(FabricBlockAccess instance, BlockAndTintGetter blockView, float normalX, float normalY, float normalZ, boolean hasShade) {
        return blockView.getShade(normalX, normalY, normalZ, hasShade);
    }

    @Inject(method = "usesAmbientOcclusion", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoAOCheck(BakedModel model, BlockState state, SodiumModelData data, RenderType renderType, BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<AmbientOcclusionMode> cir) {
        if (sporran$USE_AMBIENT_OCCLUSION_OVERRIDE.test(model.getClass())) {
            cir.setReturnValue(switch (model.useAmbientOcclusion(state, (ModelData) (Object) data, renderType)) {
                case TRUE -> AmbientOcclusionMode.ENABLED;
                case DEFAULT -> AmbientOcclusionMode.DEFAULT;
                case FALSE -> AmbientOcclusionMode.DISABLED;
            });
        }
    }

    @Inject(method = "shouldBlockEntityGlow", at = @At("HEAD"), cancellable = true)
    private void sporran$checkBlockEntityGlows(BlockEntity blockEntity, LocalPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$HAS_CUSTOM_OUTLINE_RENDERING_OVERRIDE.test(blockEntity.getClass())) {
            cir.setReturnValue(blockEntity.hasCustomOutlineRendering(player));
        }
    }

    @Inject(method = "shouldOccludeFluid", at = @At("HEAD"), cancellable = true)
    private void sporran$tryCheckNeoAdjacentFluid(Direction adjDirection, BlockState adjBlockState, FluidState fluid, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$SHOULD_HIDE_ADJACENT_FLUID_FACE_OVERRIDE.test(adjBlockState.getBlock().getClass())) {
            cir.setReturnValue(adjBlockState.shouldHideAdjacentFluidFace(adjDirection, fluid));
        }
    }
}
