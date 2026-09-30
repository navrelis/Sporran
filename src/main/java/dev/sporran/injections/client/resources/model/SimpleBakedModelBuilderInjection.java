package dev.sporran.injections.client.resources.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.neoforged.neoforge.client.RenderTypeGroup;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(SimpleBakedModel.Builder.class)
public interface SimpleBakedModelBuilderInjection {
    default BakedModel build(RenderTypeGroup renderTypeGroup) {
        throw new IllegalStateException();
    }
}
