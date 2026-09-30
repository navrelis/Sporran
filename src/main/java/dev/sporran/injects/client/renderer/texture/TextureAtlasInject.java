// TRACKED HASH: 62c27db64352c1bb41803757381fc0825f5315fe
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