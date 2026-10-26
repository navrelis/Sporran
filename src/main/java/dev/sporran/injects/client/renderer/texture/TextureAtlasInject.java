// TRACKED HASH: 62c27db64352c1bb41803757381fc0825f5315fe
/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.client.renderer.texture;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.injections.client.renderer.texture.TextureAtlasInjection;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasInject implements TextureAtlasInjection {
    @Shadow private Map<ResourceLocation, TextureAtlasSprite> texturesByName;

    @Inject(method = "upload", at = @At("TAIL"))
    private void sporran$callTextureStitchPostEvent(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        ClientHooks.onTextureAtlasStitched((TextureAtlas) (Object) this);
    }

    @Override
    public Map<ResourceLocation, TextureAtlasSprite> getTextures() {
        return Collections.unmodifiableMap(texturesByName);
    }
}