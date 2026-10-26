/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import dev.sporran.mixin.ScreenEffectRendererAccessor;

import java.util.concurrent.atomic.AtomicReference;

public interface ScreenEffectRendererInjection {
    AtomicReference<ResourceLocation> currentTexture = new AtomicReference<>();

    static void renderFluid(Minecraft mc, PoseStack poseStack, ResourceLocation texture) {
        currentTexture.set(texture);
        ScreenEffectRendererAccessor.callRenderWater(mc, poseStack);
        currentTexture.set(ScreenEffectRendererAccessor.getUnderwaterLocation());
    }
}
