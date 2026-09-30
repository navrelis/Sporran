package dev.sporran.mixin.client.sounds;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.Library;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.helpers.SoundConsumerStorage;
import dev.sporran.injections.client.sounds.ChannelAccessHandleInjection;

import java.util.function.Consumer;

@Mixin(ChannelAccess.ChannelHandle.class)
public abstract class ChannelAccessHandleMixin implements ChannelAccessHandleInjection {
    @Shadow @Nullable private Channel channel;
    @Unique private Library.Pool sporran$pool;
    @Unique private SoundEngine sporran$soundEngine;
    @Unique private SoundInstance sporran$soundInstance;

    @Override
    public void sporran$setPool(Library.Pool pool) {
        this.sporran$pool = pool;
    }

    @Override
    public void sporran$setSoundEngine(SoundEngine engine) {
        this.sporran$soundEngine = engine;
    }

    @Override
    public void sporran$setSoundInstance(SoundInstance instance) {
        this.sporran$soundInstance = instance;
    }

    @Inject(method = "method_19737", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", shift = At.Shift.AFTER))
    private void sporran$callPlaySoundEvents(Consumer<Channel> consumer, CallbackInfo ci) {
        if (this.channel != null && sporran$soundEngine != null && sporran$soundInstance != null && SoundConsumerStorage.soundConsumerChannels.remove(consumer)) {
            if (sporran$pool == Library.Pool.STATIC) {
                NeoForge.EVENT_BUS.post(new PlaySoundSourceEvent(sporran$soundEngine, sporran$soundInstance, this.channel));
            } else if (sporran$pool == Library.Pool.STREAMING) {
                NeoForge.EVENT_BUS.post(new PlayStreamingSourceEvent(sporran$soundEngine, sporran$soundInstance, this.channel));
            }
        }
    }
}
