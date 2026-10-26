/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public interface HumanoidModelArmPoseInjection {

    default <T extends LivingEntity> void applyTransform(HumanoidModel<T> model, T entity, HumanoidArm arm) {
        throw new IllegalStateException();
    }
}
