package dev.sporran.injections.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.BlockGeometryBakingContext;
import dev.sporran.util.SporranHelper;

import java.util.function.Function;

public interface BlockModelInjection {
    default ResourceLocation getParentLocation() {
        throw SporranHelper.createMixinException(BlockModelInjection.class, "getParentLocation");
    }

    default BlockGeometryBakingContext sporran$getCustomData() {
        throw SporranHelper.createMixinException(BlockModelInjection.class, "sporran$getCustomData");
    }

    default ItemOverrides getOverrides(ModelBaker baker, BlockModel blockModel, Function<Material, TextureAtlasSprite> spriteGetter) {
        throw SporranHelper.createMixinException(BlockModelInjection.class, "getOverrides");
    }

    default BakedModel bakeVanilla(ModelBaker baker, BlockModel model, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, boolean guiLight3d) {
        throw SporranHelper.createMixinException(BlockModelInjection.class, "bakeVanilla");
    }

    interface GuiLightInjection {
        default String getSerializedName() {
            throw SporranHelper.createMixinException(GuiLightInjection.class, "getSerializedName");
        }
    }
}
