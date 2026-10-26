/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import dev.sporran.injections.server.level.GenerationChunkHolderInjection;

import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.level.chunk.LevelChunk;

@Mixin(GenerationChunkHolder.class)
public abstract class GenerationChunkHolderInject implements GenerationChunkHolderInjection {
    @Unique public LevelChunk currentlyLoading;

    @Override
    public LevelChunk sporran$getCurrentlyLoading() {
        return this.currentlyLoading;
    }

    @Override
    public void sporran$setCurrentlyLoading(LevelChunk chunk) {
        this.currentlyLoading = chunk;
    }
}
