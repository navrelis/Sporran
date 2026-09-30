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
