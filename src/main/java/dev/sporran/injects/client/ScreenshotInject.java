// TRACKED HASH: 8a63553e7e8b516c065627b4127c8fb4678ebce1
package dev.sporran.injects.client;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotInject {
    private static final AtomicReference<ScreenshotEvent> sporran$target = new AtomicReference<>();

    @Inject(method = "_grab", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ExecutorService;execute(Ljava/lang/Runnable;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void sporran$runScreenshotEvent(File gameDirectory, String screenshotName, RenderTarget buffer, Consumer<Component> messageConsumer, CallbackInfo ci, NativeImage nativeImage, File file, File file2) {
        var event = ClientHooks.onScreenshot(nativeImage, file2);

        if (event.isCanceled()) {
            messageConsumer.accept(event.getCancelMessage());
            ci.cancel();
            return;
        }

        sporran$target.set(event);
    }

    @Inject(method = "method_1661", at = @At("TAIL"))
    private static void sporran$resetTarget(NativeImage nativeImage, File file, Consumer consumer, CallbackInfo ci) {
        sporran$target.set(null);
    }

    @ModifyReceiver(method = "method_1664", at = @At(value = "INVOKE", target = "Ljava/io/File;getAbsolutePath()Ljava/lang/String;"))
    private static File sporran$changePathTarget(File originalPath) {
        if (sporran$target.get() != null)
            return sporran$target.get().getScreenshotFile();
        else
            return originalPath;
    }

    @ModifyArg(method = "method_1661", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;writeToFile(Ljava/io/File;)V"))
    private static File sporran$useForgePath(File file) {
        if (sporran$target.get() != null)
            return sporran$target.get().getScreenshotFile();

        return file;
    }

    @WrapOperation(method = "method_1661", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static <T> void sporran$useForgeEventSuccess(Consumer<T> instance, T t, Operation<Void> original) {
        if (sporran$target.get() != null && sporran$target.get().getResultMessage() != null)
            original.call(instance, sporran$target.get().getResultMessage());
        else
            original.call(instance, t);
    }
}