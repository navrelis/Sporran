// TRACKED HASH: 0e6708ad54e01d212d8aaafd85dc2fd7b82ad930
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.resources.sounds;

import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.injections.client.resources.sounds.SoundInstanceInjection;

import java.util.concurrent.CompletableFuture;

@Mixin(SoundInstance.class)
public interface SoundInstanceInject extends SoundInstanceInjection, FabricSoundInstance {
    @Override
    default CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
        // Sporran: Redirect to Fabric's handling
        // TODO: redirect Fabric's calls to getAudioStream to also handle Forge's
        return this.getAudioStream(soundBuffers, sound.getPath(), looping);
    }
}