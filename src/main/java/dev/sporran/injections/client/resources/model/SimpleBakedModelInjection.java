/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.resources.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.RenderTypeGroup;
import dev.sporran.processor.FabricInjectedInterface;

import java.util.List;
import java.util.Map;

@FabricInjectedInterface(SimpleBakedModel.class)
public interface SimpleBakedModelInjection {
    static SimpleBakedModel create(List<BakedQuad> unculledFaces, Map<Direction, List<BakedQuad>> culledFaces, boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d, TextureAtlasSprite particleIcon, ItemTransforms transforms, ItemOverrides overrides, RenderTypeGroup renderTypeGroup) {
        var model = new SimpleBakedModel(unculledFaces, culledFaces, hasAmbientOcclusion, usesBlockLight, isGui3d, particleIcon, transforms, overrides);
        model.sporran$addRenderTypes(renderTypeGroup);

        return model;
    }

    default void sporran$addRenderTypes(RenderTypeGroup renderTypeGroup) {
        throw new IllegalStateException();
    }
}
