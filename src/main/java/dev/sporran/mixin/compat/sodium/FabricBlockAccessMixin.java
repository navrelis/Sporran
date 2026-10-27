/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-27; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin.compat.sodium;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AmbientOcclusionMode;
import net.caffeinemc.mods.sodium.client.services.SodiumModelData;
import net.caffeinemc.mods.sodium.client.util.DirectionUtil;
import net.caffeinemc.mods.sodium.fabric.block.FabricBlockAccess;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.extensions.IBlockEntityExtension;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.BakedModelOverrides;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

@IfModLoaded("sodium")
@Mixin(FabricBlockAccess.class)
public abstract class FabricBlockAccessMixin {
    // Sporran: Sodium's mesher asks these for every block, face and model on all of its worker threads, resolve the
    //  override checks only once.
    // Sporran: each hook below returns what Sodium's NeoForge build (NeoForgeBlockAccess) returns, but only for a block,
    //  block entity or model that really overrides the NeoForge method; for everything else Sodium's Fabric answer is
    //  kept, which is what the NeoForge default would return anyway (see the comment of each hook). The checks used to
    //  be against Block/BlockEntity/BakedModel, where the methods are defaults of the NeoForge extension interfaces, so
    //  they were true for everything (getLightEmission and hidesNeighborFace had wrong parameter lists and were never true).
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$GET_LIGHT_EMISSION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IBlockExtension.class, "getLightEmission", BlockState.class, BlockGetter.class, BlockPos.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$HIDES_NEIGHBOR_FACE_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IBlockExtension.class, "hidesNeighborFace", BlockGetter.class, BlockPos.class, BlockState.class, BlockState.class, Direction.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SHOULD_DISPLAY_FLUID_OVERLAY_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IBlockExtension.class, "shouldDisplayFluidOverlay", BlockState.class, BlockAndTintGetter.class, BlockPos.class, FluidState.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$HAS_CUSTOM_OUTLINE_RENDERING_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IBlockEntityExtension.class, "hasCustomOutlineRendering", Player.class);
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SHOULD_HIDE_ADJACENT_FLUID_FACE_OVERRIDE = SporranHelper.MethodOverrideCheck.of(IBlockExtension.class, "shouldHideAdjacentFluidFace", BlockState.class, Direction.class, FluidState.class);

    // Sporran: IBlockExtension#getLightEmission(state, level, pos) defaults to state.getLightEmission(), which is
    //  Sodium's Fabric answer.
    @Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoLightEmission(BlockState state, BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (sporran$GET_LIGHT_EMISSION_OVERRIDE.test(state.getBlock().getClass())) {
            cir.setReturnValue(state.getLightEmission(level, pos));
        }
    }

    // Sporran: NeoForge's answer is otherState.hidesNeighborFace(...) && selfState.supportsExternalFaceHiding().
    //  IBlockExtension#hidesNeighborFace defaults to false, so unless the neighbour's block overrides it the answer is
    //  false whatever supportsExternalFaceHiding says, and false is Sodium's Fabric answer. So only an override of
    //  hidesNeighborFace by the neighbour (otherState) takes over; an override of supportsExternalFaceHiding alone
    //  can't change the result. (The supportsExternalFaceHiding check used to be OR-ed in and was true for every block.)
    @Inject(method = "shouldSkipRender", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoSkipRender(BlockGetter level, BlockState selfState, BlockState otherState, BlockPos selfPos, BlockPos otherPos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$HIDES_NEIGHBOR_FACE_OVERRIDE.test(otherState.getBlock().getClass())) {
            cir.setReturnValue(otherState.hidesNeighborFace(level, otherPos, selfState, DirectionUtil.getOpposite(facing)) && selfState.supportsExternalFaceHiding());
        }
    }

    // Sporran: IBlockExtension#shouldDisplayFluidOverlay defaults to HalfTransparentBlock || LeavesBlock, which is also
    //  the default of Fabric's FluidRenderHandlerRegistry#isBlockTransparent that Sodium asks (and that one also knows
    //  the blocks Fabric mods registered).
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

    // Sporran: IBakedModelExtension#useAmbientOcclusion(state, data, renderType) defaults to
    //  useAmbientOcclusion() ? DEFAULT : FALSE, i.e. Sodium's Fabric answer (DEFAULT / DISABLED). MultiPart, Weighted and
    //  Fabric's ForwardingBakedModel only forward it to their part/variant/wrapped model, so BakedModelOverrides looks
    //  through them (against IBakedModelExtension alone they were always true).
    @Inject(method = "usesAmbientOcclusion", at = @At("HEAD"), cancellable = true)
    private void sporran$tryUseNeoAOCheck(BakedModel model, BlockState state, SodiumModelData data, RenderType renderType, BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<AmbientOcclusionMode> cir) {
        if (BakedModelOverrides.overridesAmbientOcclusion(model)) {
            cir.setReturnValue(switch (model.useAmbientOcclusion(state, (ModelData) (Object) data, renderType)) {
                case TRUE -> AmbientOcclusionMode.ENABLED;
                case DEFAULT -> AmbientOcclusionMode.DEFAULT;
                case FALSE -> AmbientOcclusionMode.DISABLED;
            });
        }
    }

    // Sporran: IBlockEntityExtension#hasCustomOutlineRendering defaults to false, Sodium's Fabric answer.
    @Inject(method = "shouldBlockEntityGlow", at = @At("HEAD"), cancellable = true)
    private void sporran$checkBlockEntityGlows(BlockEntity blockEntity, LocalPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$HAS_CUSTOM_OUTLINE_RENDERING_OVERRIDE.test(blockEntity.getClass())) {
            cir.setReturnValue(blockEntity.hasCustomOutlineRendering(player));
        }
    }

    // Sporran: IBlockExtension#shouldHideAdjacentFluidFace defaults to the same fluid type comparison that Sodium's
    //  Fabric shouldOccludeFluid does.
    @Inject(method = "shouldOccludeFluid", at = @At("HEAD"), cancellable = true)
    private void sporran$tryCheckNeoAdjacentFluid(Direction adjDirection, BlockState adjBlockState, FluidState fluid, CallbackInfoReturnable<Boolean> cir) {
        if (sporran$SHOULD_HIDE_ADJACENT_FLUID_FACE_OVERRIDE.test(adjBlockState.getBlock().getClass())) {
            cir.setReturnValue(adjBlockState.shouldHideAdjacentFluidFace(adjDirection, fluid));
        }
    }
}
