/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.block.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;

public interface ItemTransformsInjection {
    ImmutableMap<ItemDisplayContext, ItemTransform> sporran$getModdedTransforms();
    void sporran$setModdedTransforms(ImmutableMap<ItemDisplayContext, ItemTransform> moddedTransforms);
}
