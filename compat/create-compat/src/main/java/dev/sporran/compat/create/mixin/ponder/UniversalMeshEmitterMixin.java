package dev.sporran.compat.create.mixin.ponder;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.RenderType;

@Mixin(targets = "net.createmod.catnip.impl.client.render.model.UniversalMeshEmitter")
public abstract class UniversalMeshEmitterMixin implements VertexConsumer {
    @Shadow private @UnknownNullability ShadeSeparatedBufferSource bufferSource;
    @Shadow private @UnknownNullability RenderType layer;

    @Unique
    private VertexConsumer sporran$getBuffer() {
        return this.bufferSource.getBuffer(this.layer, true);
    }

    @Inject(method = "addVertex", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$addVertex(float x, float y, float z, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().addVertex(x, y, z);
    }

    @Inject(method = "setColor", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$setColor(int red, int green, int blue, int alpha, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().setColor(red, green, blue, alpha);
    }

    @Inject(method = "setUv", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$setUv(float u, float v, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().setUv(u, v);
    }

    @Inject(method = "setUv1", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$setUv1(int u, int v, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().setUv1(u, v);
    }

    @Inject(method = "setUv2", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$setUv2(int u, int v, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().setUv2(u, v);
    }

    @Inject(method = "setNormal", at = @At("HEAD"), cancellable = true)
    private void sporran$ponder$setNormal(float x, float y, float z, CallbackInfoReturnable<VertexConsumer> cir) {
        cir.setReturnValue(this);
        this.sporran$getBuffer().setNormal(x, y, z);
    }
}
