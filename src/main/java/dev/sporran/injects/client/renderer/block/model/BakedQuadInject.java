// TRACKED HASH: f7771377b9b1ccc4e36e12d9261c92039fef8d18
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.client.renderer.block.model.BakedQuadInjection;

@Mixin(BakedQuad.class)
public class BakedQuadInject implements BakedQuadInjection {
    public BakedQuadInject(int[] vertices, int tintIndex, Direction direction, TextureAtlasSprite sprite, boolean shade) {}

    @CreateInitializer
    public BakedQuadInject(int[] vertices, int tintIndex, Direction direction, TextureAtlasSprite sprite, boolean shade, boolean hasAmbientOcclusion) {
        this(vertices, tintIndex, direction, sprite, shade);
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }

    private boolean hasAmbientOcclusion = true;

    @Override
    public void sporran$setAmbientOcclusion(boolean hasAo) {
        hasAmbientOcclusion = hasAo;
    }

    @Override
    public boolean hasAmbientOcclusion() {
        return hasAmbientOcclusion;
    }
}