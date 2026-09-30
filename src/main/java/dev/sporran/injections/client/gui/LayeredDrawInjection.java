package dev.sporran.injections.client.gui;

import java.util.Collection;
import java.util.Map;

import net.neoforged.neoforge.client.gui.GuiLayerManager;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public interface LayeredDrawInjection {
    default GuiLayerManager sporran$getLayerManager() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$getLayerManager");
    }

    default Collection<ResourceLocation> sporran$getOrderedLayerIds() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$getOrderedLayerIds");
    }

    default Map<ResourceLocation, LayeredDraw.Layer> sporran$getNamedLayers() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$getNamedLayers");
    }

    default void sporran$add(ResourceLocation id) {
        this.sporran$addVanilla(id, GuiLayerManager.SPORRAN_EMPTY_LAYER);
    }

    default void sporran$addVanilla(ResourceLocation id, LayeredDraw.Layer layer) {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$addVanilla");
    }

    default Collection<LayeredDraw> sporran$getInnerDraws() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$getInnerDraws");
    }

    default void sporran$updateInternalLayers() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$updateInternalLayers");
    }

    default int sporran$getLayerCount() {
        throw SporranHelper.createMixinException(LayeredDrawInjection.class, "sporran$getLayerCount");
    }
}
