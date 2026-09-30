package dev.sporran.injections.client.resources.model;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(ModelManager.class)
public interface ModelManagerInjection {
    ModelBakery getModelBakery();
}
