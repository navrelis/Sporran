package dev.sporran.mixin.compat.owo;

import com.bawnorton.mixinsquared.TargetHandler;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded("owo")
@Mixin(value = ShaderInstance.class, priority = 1550)
public class ShaderInstanceMixin {
    @Unique
    private static final Class<?> sporran$owoShaderClass;

    static {
        try {
            sporran$owoShaderClass = Class.forName("io.wispforest.owo.shader.GlProgram$OwoShaderProgram");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*@TargetHandler(
            mixin = "dev.sporran.injects.client.renderer.ShaderInstanceInject",
            name = "sporran$addForgeSupportToFabricAPI"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), cancellable = true)
    private void sporran$checkOwoProgram(String id, CallbackInfoReturnable<String> cir, CallbackInfo ci) {
        if (sporran$owoShaderClass.isInstance(this))
            ci.cancel();
    }*/
}
