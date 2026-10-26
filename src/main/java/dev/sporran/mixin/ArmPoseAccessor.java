/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.mixin;

import net.minecraft.client.model.HumanoidModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HumanoidModel.ArmPose.class)
public interface ArmPoseAccessor {
    @Invoker("<init>")
    static HumanoidModel.ArmPose createArmPose(String name, int id, boolean bl) {
        throw new UnsupportedOperationException();
    }

    @Accessor("$VALUES")
    static HumanoidModel.ArmPose[] getValues() {
        throw new IllegalStateException();
    }

    @Accessor("$VALUES")
    @Mutable
    static void setValues(HumanoidModel.ArmPose[] values) {
        throw new IllegalStateException();
    }
}
