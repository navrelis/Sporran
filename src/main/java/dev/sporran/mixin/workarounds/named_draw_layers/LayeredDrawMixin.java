package dev.sporran.mixin.workarounds.named_draw_layers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.injections.client.gui.LayeredDrawInjection;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

@Mixin(LayeredDraw.class)
public abstract class LayeredDrawMixin implements LayeredDrawInjection {
    @Shadow @Final private List<LayeredDraw.Layer> layers;

    @Unique private final GuiLayerManager sporran$layerManager = new GuiLayerManager();
    @Unique private final List<ResourceLocation> sporran$orderedLayerIds = new ArrayList<>();
    @Unique private final Map<ResourceLocation, LayeredDraw.Layer> sporran$layerMap = new HashMap<>();
    @Unique private final List<LayeredDraw> sporran$innerDraws = new ArrayList<>();

    @Override
    public GuiLayerManager sporran$getLayerManager() {
        return this.sporran$layerManager;
    }

    @Override
    public void sporran$addVanilla(ResourceLocation id, LayeredDraw.Layer layer) {
        this.sporran$orderedLayerIds.add(id);
        this.sporran$layerManager.sporran$addVanilla(id, layer);
        this.sporran$layerMap.put(id, layer);
    }

    @Override
    public void sporran$add(ResourceLocation id) {
        var layer = GuiLayerManager.SPORRAN_EMPTY_LAYER;
        this.sporran$orderedLayerIds.add(id);
        this.sporran$layerManager.sporran$addVanilla(id, layer);
        this.sporran$layerMap.put(id, layer);
        this.layers.add(layer);
    }

    @Override
    public Collection<LayeredDraw> sporran$getInnerDraws() {
        return this.sporran$innerDraws;
    }

    @Override
    public Collection<ResourceLocation> sporran$getOrderedLayerIds() {
        return this.sporran$orderedLayerIds;
    }

    @Override
    public Map<ResourceLocation, LayeredDraw.Layer> sporran$getNamedLayers() {
        return this.sporran$layerMap;
    }

    @Override
    public void sporran$updateInternalLayers() {
        for (LayeredDraw innerDraw : this.sporran$innerDraws) {
            innerDraw.sporran$updateInternalLayers();
        }

        var existing = this.sporran$layerManager.sporran$getLayers();

        int currentLayerIndex = 0;
        for (GuiLayerManager.NamedLayer namedLayer : existing) {
            // Set the index of where to start adding layers.
            var actualLayer = this.sporran$layerMap.get(namedLayer.name());
            if (actualLayer != null) {
                var index = this.layers.indexOf(actualLayer);
                if (index != -1) {
                    currentLayerIndex = index + 1;
                    continue;
                }
            }

            // Avoid double-adding Vanilla layers
            if (this.layers.contains(namedLayer.layer()))
                continue;

            // Otherwise, let's add the layer after <index>.
            if (currentLayerIndex < this.layers.size()) {
                this.layers.add(currentLayerIndex, namedLayer.layer());
            } else {
                this.layers.add(namedLayer.layer());
            }
        }
    }

    @Inject(method = "add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;", at = @At("HEAD"))
    private void sporran$registerInternalLayerManagerToOurs(LayeredDraw layeredDraw, BooleanSupplier renderInner, CallbackInfoReturnable<LayeredDraw> cir) {
        var externalManager = layeredDraw.sporran$getLayerManager();
        this.sporran$layerManager.add(externalManager, renderInner);
        this.sporran$layerMap.putAll(layeredDraw.sporran$getNamedLayers());
        this.sporran$innerDraws.addAll(layeredDraw.sporran$getInnerDraws());
        this.sporran$innerDraws.add(layeredDraw);
        this.sporran$orderedLayerIds.addAll(layeredDraw.sporran$getOrderedLayerIds());
    }

    @Unique
    private static boolean sporran$hasAnyInnerDraw(ResourceLocation id, Collection<LayeredDraw> draws) {
        for (LayeredDraw draw : draws) {
            if (draw.sporran$getOrderedLayerIds().contains(id))
                return true;

            if (sporran$hasAnyInnerDraw(id, draw.sporran$getInnerDraws()))
                return true;
        }

        return false;
    }

    @Override
    public int sporran$getLayerCount() {
        int count = this.layers.size();

        for (LayeredDraw draw : this.sporran$innerDraws) {
            count += draw.sporran$getLayerCount();
        }

        return count;
    }

    @WrapOperation(method = "renderInner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDraw$Layer;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void sporran$tryRenderLayer(LayeredDraw.Layer instance, GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        var layerManager = this.sporran$getLayerManager();
        var named = layerManager.sporran$findNamedLayer(instance);

        if (named != null) {
            // Locate named layer in child draws
            if (sporran$hasAnyInnerDraw(named.name(), this.sporran$innerDraws)) {
                // Negate the translation that occurs afterward, we can't exactly wrap blocks of code.
                guiGraphics.pose().translate(0f, 0f, -LayeredDraw.Z_SEPARATION);
                return;
            }

            if (!NeoForge.EVENT_BUS.post(new RenderGuiLayerEvent.Pre(guiGraphics, deltaTracker, named.name(), instance)).isCanceled()) {
                original.call(instance, guiGraphics, deltaTracker);
                NeoForge.EVENT_BUS.post(new RenderGuiLayerEvent.Post(guiGraphics, deltaTracker, named.name(), instance));
            }
        } else {
            original.call(instance, guiGraphics, deltaTracker);
        }
    }
}
