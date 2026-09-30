// TRACKED HASH: 0ce404d106018ee2fbf70e284692f9e57382cddd
package dev.sporran.injects.client.color.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.client.color.block.BlockColorsInjection;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockColors.class)
public class BlockColorsInject implements BlockColorsInjection {
    @Unique
    private Map<Block, BlockColor> sporran$blockColors;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sporran$createForgeBlockColorsWorkaround(CallbackInfo ci) {
        this.sporran$blockColors = new HashMap<>();
    }

    @Inject(at = @At("RETURN"), method = "createDefault")
    private static void sporran$initForgeBlockColors(CallbackInfoReturnable<BlockColors> cir) {
        ClientHooks.onBlockColorsInit(cir.getReturnValue());
    }

    @Override
    public Map<Block, BlockColor> sporran$getBlockColors() {
        return this.sporran$blockColors;
    }

    @Inject(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/IdMapper;addMapping(Ljava/lang/Object;I)V"))
    private void sporran$registerBlockToForgeColor(BlockColor blockColor, Block[] blocks, CallbackInfo ci, @Local Block block) {
        this.sporran$blockColors.put(block, blockColor);
    }
}
