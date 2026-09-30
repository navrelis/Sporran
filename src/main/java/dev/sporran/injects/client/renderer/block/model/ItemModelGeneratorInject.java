package dev.sporran.injects.client.renderer.block.model;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.sporran.injections.client.renderer.block.model.BlockModelInjection;

@Mixin(ItemModelGenerator.class)
public abstract class ItemModelGeneratorInject {
    @ModifyReturnValue(method = "generateBlockModel", at = @At("RETURN"))
    private BlockModel sporran$copyCustomBlockModelData(BlockModel original, @Local(argsOnly = true) BlockModel source) {
        ((BlockModelInjection) original).sporran$getCustomData().copyFrom(((BlockModelInjection) source).sporran$getCustomData());
        ((BlockModelInjection) original).sporran$getCustomData().setGui3d(false);

        return original;
    }
}
