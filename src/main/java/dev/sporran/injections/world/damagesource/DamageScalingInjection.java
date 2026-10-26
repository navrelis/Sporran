/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.world.damagesource;

import net.minecraft.world.damagesource.DamageScaling;
import net.neoforged.neoforge.common.damagesource.IScalingFunction;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(DamageScaling.class)
public interface DamageScalingInjection {
    default IScalingFunction getScalingFunction() {
        throw SporranHelper.createMixinException(DamageScalingInjection.class, "getScalingFunction");
    }
}
