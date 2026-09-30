package dev.sporran.injections.client.renderer.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import dev.sporran.util.SporranHelper;

import java.util.Map;
import java.util.Set;

public interface TextureAtlasInjection {
    default Map<ResourceLocation, TextureAtlasSprite> getTextures() {
        throw SporranHelper.createMixinException(TextureAtlasInjection.class, "getTextures");
    }
}
