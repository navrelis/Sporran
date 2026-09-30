package dev.sporran.injections.client.sounds;

import com.mojang.blaze3d.audio.Library;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

import java.util.UUID;

public interface ChannelAccessHandleInjection {
    void sporran$setPool(Library.Pool pool);
    void sporran$setSoundInstance(SoundInstance instance);
    void sporran$setSoundEngine(SoundEngine engine);
}
