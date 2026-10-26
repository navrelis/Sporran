/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sporran.util.SporranHelper;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public interface HumanoidArmorLayerInjection<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
    default void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        throw SporranHelper.createMixinException(HumanoidArmorLayerInjection.class, "renderArmorPiece");
    }
}
