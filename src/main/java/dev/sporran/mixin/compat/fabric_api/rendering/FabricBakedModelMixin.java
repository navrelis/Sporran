package dev.sporran.mixin.compat.fabric_api.rendering;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.FRAPIThreadedStorage;

import java.util.function.Supplier;

@Mixin(FabricBakedModel.class)
public interface FabricBakedModelMixin {
    @Inject(method = "emitBlockQuads", at = @At("HEAD"))
    private void sporran$storeVariables(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, CallbackInfo ci) {
        if (!(blockView instanceof ClientLevel level))
            return;

        FRAPIThreadedStorage.LEVEL.set(level);
        FRAPIThreadedStorage.POS.set(pos);
    }

    @Inject(method = "emitBlockQuads", at = @At("TAIL"))
    private void sporran$clearVariables(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, CallbackInfo ci) {
        FRAPIThreadedStorage.LEVEL.remove();
        FRAPIThreadedStorage.POS.remove();
    }
}
