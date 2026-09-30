package dev.sporran.injects.client.sounds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.Library;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.sound.SoundEngineLoadEvent;
import net.neoforged.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.SoundConsumerStorage;
import dev.sporran.injections.client.sounds.ChannelAccessHandleInjection;

import java.util.function.Consumer;

@Mixin(SoundEngine.class)
public abstract class SoundEngineInject {
    @Inject(method = {"<init>", "reload"}, at = @At("TAIL"))
    private void sporran$callEngineLoadEvent(CallbackInfo ci) {
        ModLoader.postEvent(new SoundEngineLoadEvent((SoundEngine) (Object) this));
    }

    @WrapOperation(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;canPlaySound()Z"))
    private boolean sporran$checkCanPlaySound(SoundInstance instance, Operation<Boolean> original, @Local(argsOnly = true) LocalRef<SoundInstance> soundInstance) {
        soundInstance.set(ClientHooks.playSound((SoundEngine) (Object) this, instance));
        return soundInstance.get() != null && original.call(soundInstance.get());
    }

    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    private void sporran$prepareChannelInfo(SoundInstance soundInstance, CallbackInfo ci, @Local ChannelAccess.ChannelHandle channelHandle, @Local Sound sound) {
        var injection = ((ChannelAccessHandleInjection) channelHandle);

        if (sound.shouldStream())
            injection.sporran$setPool(Library.Pool.STREAMING);
        else
            injection.sporran$setPool(Library.Pool.STATIC);

        injection.sporran$setSoundInstance(soundInstance);
        injection.sporran$setSoundEngine((SoundEngine) (Object) this);
    }

    @ModifyArg(method = "method_19757", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V"))
    private static Consumer<Channel> sporran$storeSourceConsumer(Consumer<Channel> consumer) {
        SoundConsumerStorage.soundConsumerChannels.add(consumer);
        return consumer;
    }

    @ModifyArg(method = "method_19758", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V"))
    private static Consumer<Channel> sporran$storeStreamConsumer(Consumer<Channel> consumer) {
        SoundConsumerStorage.soundConsumerChannels.add(consumer);
        return consumer;
    }

    // Sporran: PlaySoundSourceEvent and PlayStreamingSourceEvent is handled in ChannelAccessHandleMixin
    // Sporran: soundInstance.getStream() redirect is handled by Fabric API
}
