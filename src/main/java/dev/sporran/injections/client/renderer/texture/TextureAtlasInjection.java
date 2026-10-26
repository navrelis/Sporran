/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
