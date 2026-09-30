package dev.sporran.injections.client.renderer.block.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;

public interface ItemTransformsInjection {
    ImmutableMap<ItemDisplayContext, ItemTransform> sporran$getModdedTransforms();
    void sporran$setModdedTransforms(ImmutableMap<ItemDisplayContext, ItemTransform> moddedTransforms);
}
