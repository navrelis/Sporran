package dev.sporran.injects.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.joml.Vector3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LightTexture.class)
public abstract class LightTextureInject {
    @Inject(method = "updateLightTexture", at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 3))
    private void sporran$adjustLightmapColors(float partialTicks, CallbackInfo ci, @Local ClientLevel level,
                                @Local(ordinal = 1) float skyDarken, @Local(ordinal = 9) float skyLight,
                                @Local(ordinal = 10) float blockLight, @Local(ordinal = 0) int pixelX,
                                @Local(ordinal = 1) int pixelY, @Local(ordinal = 1) Vector3f colors) {
        if (SporranHelper.INSTANCE.hasMethodOverride(level.effects().getClass(), DimensionSpecialEffects.class, "adjustLightmapColors", ClientLevel.class, float.class, float.class, float.class, float.class, int.class, int.class, Vector3f.class)) {
            level.effects().adjustLightmapColors(level, partialTicks, skyDarken, skyLight, blockLight, pixelX, pixelY, colors);
        }
    }

    // Sporran: We probably shouldn't be fixing this but...
    @ModifyReturnValue(method = "block", at = @At("RETURN"))
    private static int sporran$fixMc169806(int original, @Local(argsOnly = true) int light) {
        return (light & 0xFFFF) >> 4;
    }
}
